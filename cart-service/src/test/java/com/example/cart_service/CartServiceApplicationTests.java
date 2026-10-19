package com.example.cart_service;

import com.cart_service.entity.Cart;
import com.cart_service.entity.CartItem;
import com.cart_service.repository.CartRepository;
import com.cart_service.service.CartServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private String userId;
    private CartItem testItem;
    private Cart testCart;

    @BeforeEach
    void setUp() {
        userId = "user123";
        
        testItem = CartItem.builder()
                .productId("prod-1")
                .productName("Wireless Mouse")
                .price(25.0)
                .quantity(2)
                .build();

        testCart = Cart.builder()
                .userId(userId)
                .items(new HashMap<>())
                .totalPrice(0.0)
                .build();
    }

    @Test
    void testAddItemToCart_NewItem() {
        when(cartRepository.findById(userId)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        Cart updatedCart = cartService.addItemToCart(userId, testItem);

        assertNotNull(updatedCart);
        assertTrue(updatedCart.getItems().containsKey("prod-1"));
        assertEquals(50.0, updatedCart.getTotalPrice()); 
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testRemoveItemFromCart() {
        testCart.getItems().put("prod-1", testItem);
        testCart.setTotalPrice(50.0);
        
        when(cartRepository.findById(userId)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        Cart updatedCart = cartService.removeItemFromCart(userId, "prod-1");

        assertFalse(updatedCart.getItems().containsKey("prod-1"));
        assertEquals(0.0, updatedCart.getTotalPrice());
    }

    @Test
    void testClearCart() {
        cartService.clearCart(userId);
        verify(cartRepository, times(1)).deleteById(userId);
    }
}