package com.booking.booking_service.service;

import com.booking.booking_service.dto.OrderConfirmationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationProducer.class);
    private static final String TOPIC = "order-confirmations";

    @Autowired
    private KafkaTemplate<String, OrderConfirmationDto> kafkaTemplate;

    public void sendOrderConfirmation(OrderConfirmationDto confirmation) {
        LOGGER.info("Sending order confirmation to Kafka topic: {}", TOPIC);
        kafkaTemplate.send(TOPIC, confirmation);
        LOGGER.info("Successfully sent message for order ID: {}", confirmation.getOrderId());
    }
}