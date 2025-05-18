package com.shacky.materialmanagement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String fileName;
    private String contentType;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileData;
    // for postgresql
    // @Lob
    // @Column(columnDefinition = "BYTEA")
    // private byte[] fileData;

    @Column(nullable = false)
    private LocalDateTime validUntil;

    private LocalDateTime uploadTime;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    @Transient
    public boolean isImage() {
        if (fileName == null) return false;
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        return List.of("jpg", "jpeg", "png", "gif", "bmp", "webp").contains(extension);
    }
}
