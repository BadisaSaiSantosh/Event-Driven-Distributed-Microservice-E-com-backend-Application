package com.product.service;

import com.product.converter.ProductConverter;
import com.product.dto.ProductDto;
import com.product.entity.Product;
import com.product.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = ProductConverter.dtoToEntity(productDto);
        Product savedProduct = productRepo.save(product);
        return ProductConverter.entityToDto(savedProduct);
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    public ProductDto getProductById(String id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ProductConverter.entityToDto(product);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepo.findAll().stream()
                .map(ProductConverter::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @CachePut(value = "products", key = "#id")
    public ProductDto updateProduct(String id, ProductDto productDto) {
        Product existingProduct = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setStockQuantity(productDto.getStockQuantity());
        existingProduct.setCategory(productDto.getCategory());
        existingProduct.setAttributes(productDto.getAttributes());
        
        Product updatedProduct = productRepo.save(existingProduct);
        return ProductConverter.entityToDto(updatedProduct);
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(String id) {
        productRepo.deleteById(id);
    }
}