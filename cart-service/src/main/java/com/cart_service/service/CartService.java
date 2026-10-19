package com.cart_service.service;

import com.cart_service.entity.Cart;
import com.cart_service.entity.CartItem;

public interface CartService {
    Cart getCart(String userId);
    Cart addItemToCart(String userId, CartItem item);
    Cart removeItemFromCart(String userId, String productId);
    void clearCart(String userId);
}