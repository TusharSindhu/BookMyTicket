package com.booking.notification_service.service;

import com.booking.notification_service.dto.OrderConfirmationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendBookingConfirmationEmail(OrderConfirmationDto confirmation) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("no-reply@ticketbooking.com");
            message.setTo(confirmation.getCustomerEmail());
            message.setSubject("Your Booking is Confirmed! Order #" + confirmation.getOrderId());

            String emailBody = String.format(
                    "Hello!\n\n" +
                            "Your ticket booking has been successfully confirmed.\n\n" +
                            "Order ID: %d\n" +
                            "Event: %s\n" +
                            "Seat: %s\n\n" +
                            "Thank you for booking with us!",
                    confirmation.getOrderId(),
                    confirmation.getEventName(),
                    confirmation.getSeatNumber()
            );
            message.setText(emailBody);

            mailSender.send(message);
            LOGGER.info("Confirmation email sent successfully to {}", confirmation.getCustomerEmail());
        } catch (Exception e) {
            LOGGER.error("Error sending email for order ID {}: {}", confirmation.getOrderId(), e.getMessage());
        }
    }
}
