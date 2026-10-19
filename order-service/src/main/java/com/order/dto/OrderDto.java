package com.order.dto;

import com.order.entity.Order;
import java.util.List;

public class OrderDto {

    private Long id;
    private List<OrderItemDto> items;
    private double totalPrice;
    private Order.Status status;

    public OrderDto() {
    }

    public OrderDto(Long id, List<OrderItemDto> items, double totalPrice, Order.Status status) {
        this.id = id;
        this.items = items;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Order.Status getStatus() {
        return status;
    }

    public void setStatus(Order.Status status) {
        this.status = status;
    }
}
