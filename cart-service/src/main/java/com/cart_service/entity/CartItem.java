package com.cart_service.entity;

import lombok.*;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem implements Serializable {
    private String productId;
    private String productName;
    private Integer quantity;
    private Double price;
}