package com.order.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventListener.class);

    @KafkaListener(topics = "order-status-events", groupId = "order-service-group")
    public void listen(String message) {
        logger.info("Order event received: {}", message);
    }
}
