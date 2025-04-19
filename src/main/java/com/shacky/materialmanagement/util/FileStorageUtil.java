package com.shacky.materialmanagement.util;

public class FileStorageUtil {
    public static final String UPLOAD_DIR = System.getProperty("os.name").toLowerCase().contains("win")
            ? "C:/uploads" // Absolute path for Windows
            : "/data/uploads"; // Absolute path for Linux/Render
}

