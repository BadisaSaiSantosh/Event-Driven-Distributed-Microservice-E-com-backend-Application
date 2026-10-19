package com.product.dto;

import java.util.Map;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String id;
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private String category;
    private Map<String, String> attributes;
}