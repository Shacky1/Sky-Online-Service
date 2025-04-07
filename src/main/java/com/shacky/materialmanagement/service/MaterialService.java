package com.shacky.materialmanagement.service;

import com.shacky.materialmanagement.entity.Material;
import com.shacky.materialmanagement.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
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
}
