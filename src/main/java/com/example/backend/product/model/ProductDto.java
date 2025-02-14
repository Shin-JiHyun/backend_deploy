package com.example.backend.product.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ProductDto {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ProductRequest {
        private String name;
        private int price;

        public Product toEntity(){
            return Product.builder()
                    .name(name)
                    .price(price)
                    .build();
        }

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ProductResponse {
        private String name;
        private int price;

        public static ProductResponse from(Product product){
            return ProductResponse.builder()
                    .name(product.getName())
                    .price(product.getPrice())
                    .build();
        }

    }

    @Getter
    public static class CreateReq {
        private String name;
        private int price;

        public Product toEntity() {
            return Product.builder()
                    .name(name)
                    .price(price)
                    .build();
        }
    }

    @Getter @Builder
    public static class ProductRes {
        private Long idx;
        private String name;
        private int price;
        private List<String> imageUrls;

        public static ProductRes of(Product entity) {
            return ProductRes.builder()
                    .idx(entity.getIdx())
                    .name(entity.getName())
                    .price(entity.getPrice())
                    .imageUrls(entity.getImages().stream().map(image ->(image.getUrl())).toList())
                    .build();
        }
    }
}

