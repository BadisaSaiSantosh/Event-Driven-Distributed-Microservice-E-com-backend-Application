package com.product.service;

import com.product.dto.ProductDto;
import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);
    ProductDto getProductById(String id);
    List<ProductDto> getAllProducts();
    ProductDto updateProduct(String id, ProductDto productDto);
    void deleteProduct(String id);
}