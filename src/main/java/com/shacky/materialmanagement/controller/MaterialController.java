package com.shacky.materialmanagement.controller;

import com.shacky.materialmanagement.entity.Material;
import com.shacky.materialmanagement.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Controller
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    // Home page displays all materials
    @GetMapping("/")
    public String homePage(Model model) {
        List<Material> materials = materialService.getAllMaterials();
        model.addAttribute("materials", materials);
        return "home"; // Loads home.html with materials list
    }

    // Admin page for uploading materials
    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        List<Material> materials = materialService.getAllMaterials();
        model.addAttribute("materials", materials);
        return "admin";
    }

    // Upload material and save to disk
    @PostMapping("/admin/upload")
    public String uploadMaterial(@RequestParam("file") MultipartFile file,
                                 @RequestParam("name") String name) {
        try {
            // Save file to a specific folder relative to your project root
            String uploadDir = System.getProperty("user.dir") + "/uploads/";  // e.g., C:/Users/Shacky/YourProject/uploads/
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) uploadPath.mkdirs();

            File dest = new File(uploadPath, file.getOriginalFilename());
            file.transferTo(dest);

            // Save metadata in DB
            Material material = new Material();
            material.setName(name);
            material.setUrl(dest.getAbsolutePath()); // full path for download later
            materialService.saveMaterial(material);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/admin";
    }

    // Download material directly (no separate view)
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

}
