package com.shacky.materialmanagement.repository;

import com.shacky.materialmanagement.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material,Long> {
}
