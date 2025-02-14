package com.example.backend.product.service;

import com.example.backend.product.model.Product;
import com.example.backend.product.model.ProductDto;
import com.example.backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductImageService productImageService;
    private final ProductRepository productRepository;
    private final ImageService imageService;
    private final PreSignedCloudImageService preSignedCloudImageService;


    public ProductDto.ProductRes preSigned(ProductDto.CreateReq2 dto) {
        // 상품 정보 DB에 저장
        Product product = productRepository.save(dto.toEntity());

        List<String> uploadFilePaths = new ArrayList();
        List<String> preSignedUrls = new ArrayList();
        for (String file : dto.getFiles()) {
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd/"));
            String fileName = date + UUID.randomUUID() + "_" + file;
            String preSignedUrl = preSignedCloudImageService.upload(fileName, "image/png");
            preSignedUrls.add(preSignedUrl);
            uploadFilePaths.add(fileName);
        }
        // 이미지 저장 정보를 DB에 저장
        productImageService.create(uploadFilePaths, product);

        ProductDto.ProductRes response =  ProductDto.ProductRes.of(product);
        response.setImageUrls(preSignedUrls);
        return response;
    }


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
