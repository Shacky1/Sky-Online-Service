package com.shacky.materialmanagement.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ServiceOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private OnlineService service;

    private String status; // e.g., "Pending", "Completed"

    private LocalDateTime datePlaced; // <-- new field

    // No-args constructor
    public ServiceOrder() {
    }

    // All-args constructor
    public ServiceOrder(Long id, Customer customer, OnlineService service, String status, LocalDateTime datePlaced) {
        this.id = id;
        this.customer = customer;
        this.service = service;
        this.status = status;
        this.datePlaced = datePlaced;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public OnlineService getService() {
        return service;
    }

    public void setService(OnlineService service) {
        this.service = service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDatePlaced() {
        return datePlaced;
    }

    public void setDatePlaced(LocalDateTime datePlaced) {
        this.datePlaced = datePlaced;
    }
}
