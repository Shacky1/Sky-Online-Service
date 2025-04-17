package com.shacky.materialmanagement.repository;

import com.shacky.materialmanagement.entity.UploadedDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadedDocumentRepository extends JpaRepository<UploadedDocument, Long> {
    List<UploadedDocument> findByServiceOrderId(Long serviceOrderId);
    List<UploadedDocument> findByCustomerId(Long customerId);
}
