package com.shacky.materialmanagement.repository;

import com.shacky.materialmanagement.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material,Long> {
    List<Material> findByValidUntilBefore(LocalDateTime dateTime);
    List<Material> findByValidUntilAfter(LocalDateTime dateTime); // For showing only valid materials


    Optional<Material> findByFileName(String fileName);
}
