package com.shacky.materialmanagement.service;

import com.shacky.materialmanagement.entity.UploadedDocument;
import com.shacky.materialmanagement.repository.UploadedDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UploadedDocumentService {

    @Autowired
    private UploadedDocumentRepository uploadedDocumentRepository;

    public UploadedDocument saveDocument(UploadedDocument document) {
        document.setUploadDate(LocalDateTime.now());
        return uploadedDocumentRepository.save(document);
    }

    public List<UploadedDocument> getDocumentsByServiceOrder(Long serviceOrderId) {
        return uploadedDocumentRepository.findByServiceOrderId(serviceOrderId);
    }

    public List<UploadedDocument> getDocumentsByCustomer(Long customerId) {
        return uploadedDocumentRepository.findByCustomerId(customerId);
    }
}
