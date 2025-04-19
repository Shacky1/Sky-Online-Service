package com.shacky.materialmanagement.service;

import com.shacky.materialmanagement.entity.Material;
import com.shacky.materialmanagement.repository.MaterialRepository;
import com.shacky.materialmanagement.util.FileStorageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    public List<Material> getAllMaterials() {
        return materialRepository.findByValidUntilAfter(LocalDateTime.now());
    }

    public Material getMaterial(Long id) {
        Optional<Material> material = materialRepository.findById(id);
        if (material.isPresent()) {
            return material.get();
        }
        return null;  // Ensure this is correctly handled in your controller
    }


    public Material saveMaterial(Material material) {
        return materialRepository.save(material);
    }
    public void removeOutdatedMaterials() {
        LocalDateTime now = LocalDateTime.now();
        List<Material> outdatedMaterials = materialRepository.findByValidUntilBefore(now);

        for (Material material : outdatedMaterials) {
            File file = new File(FileStorageUtil.UPLOAD_DIR, material.getUrl());
            if (file.exists()) {
                file.delete();
            }

            materialRepository.delete(material);
        }
    }

}
