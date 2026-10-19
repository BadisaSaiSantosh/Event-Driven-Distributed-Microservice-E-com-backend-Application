package com.cart_service.controller;

import com.cart_service.entity.Cart;
import com.cart_service.entity.CartItem;
import com.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart/v1")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Any authenticated user can view their own cart
    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Cart> getCart(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Cart> addItem(
            @RequestHeader("X-User-Id") String userId, 
            @RequestBody CartItem item) {
        return ResponseEntity.ok(cartService.addItemToCart(userId, item));
    }

    @DeleteMapping("/remove/{productId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Cart> removeItem(
            @RequestHeader("X-User-Id") String userId, 
            @PathVariable String productId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(userId, productId));
    }

    @DeleteMapping("/clear")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<Void> clearCart(@RequestHeader("X-User-Id") String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}