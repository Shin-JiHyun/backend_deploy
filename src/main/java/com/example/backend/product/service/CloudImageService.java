package com.example.backend.product.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CloudImageService implements ImageService {
    private final S3Client s3Client;
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public List<String> upload(MultipartFile[] files) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        List<String> uploadPaths = new ArrayList<>();
        try {

        for(MultipartFile file : files) {
            String fileName = date + UUID.randomUUID().toString()+"_" + file.getOriginalFilename();
            PutObjectResponse response = s3Client.putObject(
                    PutObjectRequest.builder()
                            .contentType(file.getContentType())
                            .key(fileName)
                            .bucket(bucket)
                            .build()
                    , RequestBody.fromBytes(file.getBytes())
            );
            uploadPaths.add("/"+fileName);
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    return uploadPaths;
    }
}
