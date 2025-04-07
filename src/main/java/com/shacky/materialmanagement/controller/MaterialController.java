package com.shacky.materialmanagement.controller;

import com.shacky.materialmanagement.entity.Material;
import com.shacky.materialmanagement.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        List<Material> materials = materialService.getAllMaterials(); // Fetch all materials from the database
        model.addAttribute("materials", materials); // Add materials to the model
        return "admin"; // Return the name of the Thymeleaf template
    }


    // Upload a material (Admin)
    @PostMapping("/admin/upload")
    public String uploadMaterial(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) {
        // Upload the file (for simplicity, store the filename and path)
        String fileUrl = "/uploads/" + file.getOriginalFilename(); // Make sure to handle file storage

        Material material = new Material();
        material.setName(name);
        material.setUrl(fileUrl);
        materialService.saveMaterial(material);

        return "redirect:/admin";
    }

    // User Page
    @GetMapping("/user")
    public String userPage(Model model) {
        model.addAttribute("materials", materialService.getAllMaterials());
        return "user";
    }

    // Download Material
    @GetMapping("/download/{id}")
    public String downloadMaterial(@PathVariable Long id, Model model) {
        Material material = materialService.getMaterial(id);
        model.addAttribute("material", material);
        return "download";  // Create a download template
    }
}

