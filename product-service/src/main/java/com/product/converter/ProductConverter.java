package com.product.converter;

import com.product.entity.Product;
import com.product.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {
    public static ProductDto entityToDto(Product product) {
        if (product == null) return null;
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .category(product.getCategory())
                .attributes(product.getAttributes())
                .build();
    }

    public static Product dtoToEntity(ProductDto dto) {
        if (dto == null) return null;
        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .stockQuantity(dto.getStockQuantity())
                .category(dto.getCategory())
                .attributes(dto.getAttributes())
                .build();
    }
}