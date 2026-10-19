package com.order.converter;

import com.order.dto.OrderDto;
import com.order.dto.OrderItemDto;
import com.order.entity.Order;
import com.order.entity.OrderItem;

import java.util.stream.Collectors;

public class OrderConverter {
    public static OrderDto orderToDto(Order order) {
        if (order == null) {
            return null;
        }
        return new OrderDto(
                order.getId(),
                order.getItems().stream().map(OrderConverter::itemToDto).collect(Collectors.toList()),
                order.getTotalPrice(),
                order.getStatus());
    }

    public static Order dtoToOrder(OrderDto orderDto) {
        if (orderDto == null) {
            return null;
        }
        Order order = new Order();
        order.setStatus(orderDto.getStatus());
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setItems(orderDto.getItems().stream().map(itemDto -> dtoToItem(order, itemDto)).collect(Collectors.toList()));
        return order;
    }

    private static OrderItemDto itemToDto(OrderItem item) {
        return new OrderItemDto(item.getProductId(), item.getQuantity(), item.getUnitPrice());
    }

    private static OrderItem dtoToItem(Order order, OrderItemDto itemDto) {
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProductId(itemDto.getProductId());
        item.setQuantity(itemDto.getQuantity());
        item.setUnitPrice(itemDto.getUnitPrice());
        item.setTotalPrice(itemDto.getUnitPrice() * itemDto.getQuantity());
        return item;
    }
}
