package com.example.backend.product.service;

import com.example.backend.product.model.Product;
import com.example.backend.product.model.ProductDto;
import com.example.backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductImageService productImageService;
    private final ProductRepository productRepository;
    private final ImageService imageService;


    public  void create(ProductDto.CreateReq dto, MultipartFile[] files) {
        Product product =  productRepository.save(dto.toEntity());

        List<String> uploadFilePaths = imageService.upload(files);

        productImageService.create(uploadFilePaths, product);

        return ;
    }

    public List<ProductDto.ProductResponse> getAllProducts(int page, int size) {
        Page<Product> products = productRepository.findAll(PageRequest.of(page, size));
        return products.stream()
                .map(ProductDto.ProductResponse::from)
                .collect(Collectors.toList());
    }

    public ProductDto.ProductResponse get(Long productIdx) {
        Product product = productRepository.findById(productIdx).orElse(null);
        return ProductDto.ProductResponse.from(product);
    }
}
