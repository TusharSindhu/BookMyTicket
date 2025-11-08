package com.booking.notification_service.service;

import com.booking.notification_service.dto.OrderConfirmationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationConsumer.class);

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "order-confirmations", groupId = "notification-group")
    public void consume(OrderConfirmationDto confirmation) {
        LOGGER.info("Consumed message -> {}", confirmation);

        // Once the message is consumed, delegate to the EmailService to send the notification
        emailService.sendBookingConfirmationEmail(confirmation);
    }
}
