package com.example.backend.product.service;

import com.example.backend.product.model.Product;
import com.example.backend.product.model.ProductImage;
import com.example.backend.product.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductImageService {
    private final ProductImageRepository productImageRepository;

    public void create(List<String> uploadFilePaths, Product product) {
        for(String uploadFilePath: uploadFilePaths) {

            productImageRepository.save(
                    ProductImage.builder()
                            .url(uploadFilePath)
                            .product(product)
                            .build())
            ;
        }
    }
}
