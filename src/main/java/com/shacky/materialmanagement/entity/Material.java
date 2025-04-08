package com.shacky.materialmanagement.entity;

import jakarta.persistence.*;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Entity
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String url;

    @Column(nullable = false)
    private LocalDateTime validUntil;

    private LocalDateTime uploadTime;

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    @Transient
    public String getFileName() {
        // Ensure url is not null
        if (this.url == null) return "";
        return Paths.get(this.url).getFileName().toString();
    }

    @Transient
    public boolean isImage() {
        if (url == null) return false;
        String extension = url.substring(url.lastIndexOf('.') + 1).toLowerCase();
        return List.of("jpg", "jpeg", "png", "gif", "bmp", "webp").contains(extension);
    }

}
