package com.example.backend.product.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@Service
public class LocalImageService implements ImageService {

    @Value("${project.upload.path}")
    private String defaultUpladPath;

    private String makeDir() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String uploadPath = defaultUpladPath + "/" + date;

        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        return "/" + date;
    }

    @Override
    public List<String> upload(MultipartFile[] files) {
        List<String> uploadedFilePaths = new ArrayList<>();
        String uploadPath = makeDir();
        for(MultipartFile file: files){
            String originalFilename = file.getOriginalFilename();
            String uploadFilePath = defaultUpladPath + uploadPath + "/" + UUID.randomUUID().toString() + "." + originalFilename;
            File uploadFile = new File(uploadFilePath);
            uploadedFilePaths.add(uploadFilePath);

            try {

                file.transferTo(uploadFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
        }
            }
        return uploadedFilePaths;
    }
}
