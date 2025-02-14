package com.example.backend.product.controller;

import com.example.backend.product.model.ProductDto;
import com.example.backend.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?> addProduct(@RequestPart ProductDto.CreateReq dto, MultipartFile[] images) {
             productService.create(dto, images);
            return ResponseEntity.ok().build();
    }


    @GetMapping("/list")
    public ResponseEntity<List<ProductDto.ProductResponse>> getAllProducts(int page, int size) {
        return ResponseEntity.ok(productService.getAllProducts(page, size));
    }

    @GetMapping("/read/{productIdx}")
    public ResponseEntity<ProductDto.ProductResponse> getProduct(@PathVariable Long productIdx) {
        ProductDto.ProductResponse response = productService.get(productIdx);
        return ResponseEntity.ok(response);
    }
}
