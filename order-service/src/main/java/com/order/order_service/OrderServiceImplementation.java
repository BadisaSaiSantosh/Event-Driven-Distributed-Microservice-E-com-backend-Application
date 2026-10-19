package com.order.order_service;

import com.order.converter.OrderConverter;
import com.order.dto.OrderDto;
import com.order.entity.Order;
import com.order.kafka.OrderEvent;
import com.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImplementation implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImplementation.class);
    private static final String TOPIC = "order-status-events";

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderServiceImplementation(OrderRepository orderRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderConverter::orderToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderById(long orderId) {
        return orderRepository.findById(orderId)
                .map(OrderConverter::orderToDto)
                .orElse(null);
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = OrderConverter.dtoToOrder(orderDto);
        order.setId(null);
        order.getItems().forEach(item -> item.setOrder(order));
        order.setTotalPrice(calculateTotal(order));
        Order savedOrder = orderRepository.save(order);
        sendOrderEvent(savedOrder.getId(), savedOrder.getStatus().name());
        return OrderConverter.orderToDto(savedOrder);
    }

    public OrderDto updateOrderStatus(long orderId, Order.Status status) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setStatus(status);
                    Order updatedOrder = orderRepository.save(order);
                    sendOrderEvent(updatedOrder.getId(), status.name());
                    return OrderConverter.orderToDto(updatedOrder);
                })
                .orElse(null);
    }

    private double calculateTotal(Order order) {
        return order.getItems().stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
    }

    private void sendOrderEvent(long orderId, String status) {
        if (status == null) {
            return;
        }
        if (status.equalsIgnoreCase("CANCELLED") || status.equalsIgnoreCase("SHIPPED") || status.equalsIgnoreCase("DELIVERED") || status.equalsIgnoreCase("CONFIRMED")) {
            String message = String.format("Order %d status changed to %s", orderId, status);
            kafkaTemplate.send(TOPIC, String.valueOf(orderId), message);
            logger.info("Sent Kafka order event: {}", message);
        }
    }
}
