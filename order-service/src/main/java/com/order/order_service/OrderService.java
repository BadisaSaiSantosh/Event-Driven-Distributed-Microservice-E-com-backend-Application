package com.order.order_service;

import com.order.dto.OrderDto;
import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrders();
    OrderDto getOrderById(long orderId);
    OrderDto createOrder(OrderDto orderDto);
}
