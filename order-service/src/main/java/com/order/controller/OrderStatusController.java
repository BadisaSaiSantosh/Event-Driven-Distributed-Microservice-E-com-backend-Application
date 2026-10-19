package com.order.controller;

import com.order.dto.OrderDto;
import com.order.entity.Order;
import com.order.order_service.OrderServiceImplementation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderStatusController {

    private final OrderServiceImplementation orderService;

    public OrderStatusController(OrderServiceImplementation orderService) {
        this.orderService = orderService;
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable("id") long id,
                                                     @RequestParam("status") Order.Status status) {
        OrderDto updated = orderService.updateOrderStatus(id, status);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }
}
