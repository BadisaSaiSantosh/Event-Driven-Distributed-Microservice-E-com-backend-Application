package com.example.order_service;

import com.order.OrderServiceApplication;
import com.order.dto.OrderDto;
import com.order.entity.Order;
import com.order.order_service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = OrderServiceApplication.class)
class OrderServiceIntegrationTests {

    @Autowired
    private OrderService orderService;

    @Test
    void shouldCreateAndReadOrder() {
        OrderDto request = new OrderDto();
        // request.setProductId(100L);
        // request.setQuantity(2);
        request.setTotalPrice(49.98);
        request.setStatus(Order.Status.CONFIRMED);

        OrderDto created = orderService.createOrder(request);

        assertThat(created).isNotNull();
        // assertThat(created.getId()).isGreaterThan(0);
        // assertThat(created.getProductId()).isEqualTo(100L);
        // assertThat(created.getQuantity()).isEqualTo(2);
        assertThat(created.getStatus()).isEqualTo(Order.Status.CONFIRMED);

        OrderDto fetched = orderService.getOrderById(created.getId());
        assertThat(fetched).isNotNull();
        assertThat(fetched.getId()).isEqualTo(created.getId());
        assertThat(fetched.getTotalPrice()).isEqualTo(49.98);

        List<OrderDto> allOrders = orderService.getAllOrders();
        assertThat(allOrders).extracting(OrderDto::getId).contains(created.getId());
    }
}
