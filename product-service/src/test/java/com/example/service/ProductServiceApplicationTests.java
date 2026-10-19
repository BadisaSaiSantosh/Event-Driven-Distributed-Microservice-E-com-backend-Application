package com.example.service;

import com.product.dto.ProductDto;
import com.product.entity.Product;
import com.product.repository.ProductRepo;
import com.product.service.ProductServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepo productRepo; 

    @InjectMocks
    private ProductServiceImpl productService; 

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        // Setup dummy data before each test
        product = Product.builder()
                .id("123")
                .name("Gaming Laptop")
                .price(1500.00)
                .category("Electronics")
                .build();

        productDto = ProductDto.builder()
                .name("Gaming Laptop")
                .price(1500.00)
                .category("Electronics")
                .build();
    }

    @Test
    void testCreateProduct() {
        // When the repo is asked to save, return our dummy product
        when(productRepo.save(any(Product.class))).thenReturn(product);

        // Call the real service method
        ProductDto savedProduct = productService.createProduct(productDto);

        // Assert the results are correct
        assertNotNull(savedProduct);
        assertEquals("Gaming Laptop", savedProduct.getName());
        assertEquals("123", savedProduct.getId());
        
        // Verify the database save method was called exactly once
        verify(productRepo, times(1)).save(any(Product.class));
    }

    @Test
    void testGetProductById_Success() {
        // Mock the DB finding the product
        when(productRepo.findById("123")).thenReturn(Optional.of(product));

        ProductDto foundProduct = productService.getProductById("123");

        assertNotNull(foundProduct);
        assertEquals("123", foundProduct.getId());
        assertEquals("Electronics", foundProduct.getCategory());
    }

    @Test
    void testGetProductById_NotFound() {
        // Mock the DB NOT finding the product
        when(productRepo.findById("999")).thenReturn(Optional.empty());

        // Expect a RuntimeException when searching for a bad ID
        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.getProductById("999");
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void testGetAllProducts() {
        // Mock the DB returning a list of products
        when(productRepo.findAll()).thenReturn(Arrays.asList(product));

        List<ProductDto> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Gaming Laptop", products.get(0).getName());
    }

    @Test
    void testDeleteProduct() {
        // Call delete method
        productService.deleteProduct("123");

        // Verify the repo delete method was called exactly once with ID "123"
        verify(productRepo, times(1)).deleteById("123");
    }
}