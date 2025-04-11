package com.shacky.materialmanagement.controller;

import com.shacky.materialmanagement.entity.Admin;
import com.shacky.materialmanagement.entity.Material;
import com.shacky.materialmanagement.entity.OnlineService;
import com.shacky.materialmanagement.repository.AdminRepository;
import com.shacky.materialmanagement.service.AdminService;
import com.shacky.materialmanagement.service.MaterialService;
import com.shacky.materialmanagement.service.OnlineServiceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;


import java.net.MalformedURLException;
import java.nio.file.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private OnlineServiceService onlineServiceService;

    @GetMapping("/")
    public String homePage(Model model) {
        List<Material> materials = materialService.getAllMaterials();
        model.addAttribute("materials", materials);

        List<OnlineService> services = onlineServiceService.getAllServices();
        model.addAttribute("services", services);

        return "home";
    }

    // Login form
    @GetMapping("/admin/login")
    public String showLoginPage() {
        return "login";
    }

    // Show admin upload page (only if logged in)
    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        List<Material> materials = materialService.getAllMaterials();
        model.addAttribute("materials", materials);

        List<OnlineService> services = onlineServiceService.getAllServices();
        model.addAttribute("services", services);

        return "admin";
    }

    // Handle adding online service
    @PostMapping("/admin/services/add")
    public String addOnlineService(@RequestParam("title") String title,
                                   @RequestParam("requirements") String requirements,
                                   RedirectAttributes redirectAttributes) {
        try {
            OnlineService onlineService = new OnlineService();
            onlineService.setTitle(title);
            onlineService.setRequirements(requirements);
            onlineServiceService.saveService(onlineService);

            redirectAttributes.addFlashAttribute("serviceSuccess", "Service added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("serviceError", "Error adding service.");
        }
        return "redirect:/admin";
    }

    // New method to handle "See More" functionality
    @GetMapping("/service/{id}")
    public String viewServiceDetails(@PathVariable Long id, Model model) {
        OnlineService onlineService = onlineServiceService.getService(id);
        if (onlineService != null) {
            model.addAttribute("service", onlineService);
            return "service-details";  // A new view to display service details
        } else {
            model.addAttribute("errorMessage", "Service not found.");
            return "error";
        }
    }

    // Handle file upload
    @PostMapping("/admin/upload")
    public String uploadMaterial(@RequestParam("file") MultipartFile file,
                                 @RequestParam("name") String name,
                                 @RequestParam("validDays") int validDays,
                                 RedirectAttributes redirectAttributes) {
        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) uploadPath.mkdirs();

            File dest = new File(uploadPath, file.getOriginalFilename());
            file.transferTo(dest);

            Material material = new Material();
            material.setName(name);
            material.setUrl(dest.getAbsolutePath());
            material.setUploadTime(LocalDateTime.now());
            material.setValidUntil(LocalDateTime.now().plusDays(validDays));
            materialService.saveMaterial(material);

            redirectAttributes.addFlashAttribute("successMessage", "Announcement uploaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload Announcement.");
        }

        return "redirect:/admin";
    }

    // Handle file download
    @GetMapping("/download/{id}")
    public void downloadMaterial(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Material material = materialService.getMaterial(id);
        if (material == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Material not found");
            return;
        }

        File file = new File(material.getUrl());
        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
            return;
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        response.setContentLengthLong(file.length());

        try (InputStream inputStream = new FileInputStream(file);
             OutputStream outputStream = response.getOutputStream()) {
            inputStream.transferTo(outputStream);
        }
    }
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(System.getProperty("user.dir"), "uploads", filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    // Delete a service
    @GetMapping("/admin/services/delete/{id}")
    public String deleteOnlineService(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            onlineServiceService.deleteService(id);
            redirectAttributes.addFlashAttribute("serviceSuccess", "Service deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("serviceError", "Failed to delete service.");
        }
        return "redirect:/admin";
    }

    // Show edit form for a service
    @GetMapping("/admin/services/edit/{id}")
    public String editOnlineService(@PathVariable Long id, Model model) {
        OnlineService service = onlineServiceService.getService(id);
        if (service != null) {
            model.addAttribute("editService", service);
            model.addAttribute("services", onlineServiceService.getAllServices()); // So the list still shows
            return "admin"; // Reuse same page
        } else {
            model.addAttribute("serviceError", "Service not found.");
            return "redirect:/admin";
        }
    }

    // Handle POST update
    @PostMapping("/admin/services/update")
    public String updateOnlineService(@RequestParam Long id,
                                      @RequestParam String title,
                                      @RequestParam String requirements,
                                      RedirectAttributes redirectAttributes) {
        try {
            OnlineService existing = onlineServiceService.getService(id);
            if (existing != null) {
                existing.setTitle(title);
                existing.setRequirements(requirements);
                onlineServiceService.saveService(existing);
                redirectAttributes.addFlashAttribute("serviceSuccess", "Service updated successfully.");
            } else {
                redirectAttributes.addFlashAttribute("serviceError", "Service not found.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("serviceError", "Error updating service.");
        }
        return "redirect:/admin";
    }
    @GetMapping("/services")
    public String viewServices(Model model) {
        List<OnlineService> services = onlineServiceService.getAllServices();
        model.addAttribute("services", services);
        return "services";
    }
    @PostMapping("/admin/change-password")
    public String changePassword(HttpServletRequest request,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 RedirectAttributes redirectAttributes) {

        String username = request.getUserPrincipal().getName(); // Current logged-in admin
        Optional<Admin> optionalAdmin = adminService.findByUsername(username);

        if (optionalAdmin.isEmpty()) {
            redirectAttributes.addFlashAttribute("passwordError", "Admin not found.");
            return "redirect:/admin";
        }

        Admin admin = optionalAdmin.get();

        if (!passwordEncoder.matches(currentPassword, admin.getPassword())) {
            redirectAttributes.addFlashAttribute("passwordError", "Current password is incorrect.");
            return "redirect:/admin";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("passwordError", "New passwords do not match.");
            return "redirect:/admin";
        }

        admin.setPassword(passwordEncoder.encode(newPassword));
        adminService.save(admin);

        redirectAttributes.addFlashAttribute("passwordSuccess", "Password changed successfully.");
        return "redirect:/admin";
    }


    // logout
    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}
