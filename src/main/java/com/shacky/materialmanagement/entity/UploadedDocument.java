package com.shacky.materialmanagement.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class UploadedDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "service_order_id")
    private ServiceOrder serviceOrder; // Relationship to ServiceOrder

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer; // Explicit relationship to Customer

    private String documentPath; // Path to the stored document
    private String description; // Description of the document (e.g., NIDA number)

    private LocalDateTime uploadDate; // Timestamp when the document was uploaded

    // No-args constructor
    public UploadedDocument() {
    }

    // All-args constructor
    public UploadedDocument(Long id, ServiceOrder serviceOrder, Customer customer, String documentPath, String description, LocalDateTime uploadDate) {
        this.id = id;
        this.serviceOrder = serviceOrder;
        this.customer = customer;
        this.documentPath = documentPath;
        this.description = description;
        this.uploadDate = uploadDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServiceOrder getServiceOrder() {
        return serviceOrder;
    }

    public void setServiceOrder(ServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
}
