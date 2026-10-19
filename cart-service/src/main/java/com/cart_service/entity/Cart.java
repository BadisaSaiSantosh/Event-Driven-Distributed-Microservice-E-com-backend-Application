package com.cart_service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import lombok.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// Stores carts in Redis. Automatically deletes them after 7 days (604800 seconds) if inactive.
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "Cart", timeToLive = 604800)
public class Cart implements Serializable {
    
    @Id
    private String userId; // The Cart ID is simply the User's ID
    
    // Using a Map (ProductId -> CartItem) makes updating quantities O(1) time complexity
    @Builder.Default
    private Map<String, CartItem> items = new HashMap<>();
    
    private Double totalPrice;
}