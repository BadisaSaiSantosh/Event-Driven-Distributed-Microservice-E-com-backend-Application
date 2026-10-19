package com.cart_service.service;

import com.cart_service.entity.Cart;
import com.cart_service.entity.CartItem;
import com.cart_service.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    public Cart getCart(String userId) {
        return cartRepository.findById(userId)
                .orElse(Cart.builder().userId(userId).build());
    }

    @Override
    public Cart addItemToCart(String userId, CartItem item) {
        Cart cart = getCart(userId);
        
        // If product already exists in cart, just update the quantity
        if (cart.getItems().containsKey(item.getProductId())) {
            CartItem existingItem = cart.getItems().get(item.getProductId());
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
        } else {
            cart.getItems().put(item.getProductId(), item);
        }
        
        recalculateTotal(cart);
        return cartRepository.save(cart);
    }

    @Override
    public Cart removeItemFromCart(String userId, String productId) {
        Cart cart = getCart(userId);
        cart.getItems().remove(productId);
        recalculateTotal(cart);
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(String userId) {
        cartRepository.deleteById(userId);
    }

    private void recalculateTotal(Cart cart) {
        double total = cart.getItems().values().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        cart.setTotalPrice(total);
    }
}