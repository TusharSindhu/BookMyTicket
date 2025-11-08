package com.booking.booking_service.service;

import com.booking.booking_service.dto.BookingRequestDto;
import com.booking.booking_service.dto.InventoryTicketDto;
import com.booking.booking_service.dto.OrderConfirmationDto;
import com.booking.booking_service.entity.BookingOrder;
import com.booking.booking_service.entity.Customer;
import com.booking.booking_service.repository.BookingOrderRepository;
import com.booking.booking_service.repository.CustomerRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class BookingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private BookingOrderRepository bookingOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationProducer notificationProducer;

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    @Transactional
    public BookingOrder createBooking(BookingRequestDto request) {
        LOGGER.info("Attempting to create booking for customer {} and ticket {}", request.getCustomerId(), request.getTicketId());

        // 1. Verify customer exists
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + request.getCustomerId()));

        try {
            // 2. Call Inventory Service to reserve the ticket
            InventoryTicketDto reservedTicket = reserveTicketInInventory(request);

            // 3. Create and save a confirmed order
            BookingOrder order = BookingOrder.builder()
                    .customerId(request.getCustomerId())
                    .ticketId(request.getTicketId())
                    .eventName(reservedTicket.getEventName())
                    .seatNumber(reservedTicket.getSeatNumber())
                    .status(BookingOrder.OrderStatus.CONFIRMED)
                    .build();
            BookingOrder savedOrder = bookingOrderRepository.save(order);

            // 4. Send notification for successful booking
            notificationProducer.sendOrderConfirmation(OrderConfirmationDto.builder()
                    .orderId(savedOrder.getId())
                    .customerId(customer.getId())
                    .customerEmail(customer.getEmail())
                    .eventName(savedOrder.getEventName())
                    .seatNumber(savedOrder.getSeatNumber())
                    .build());

            LOGGER.info("Booking successful. Order ID: {}", savedOrder.getId());
            return savedOrder;

        } catch (HttpClientErrorException e) {
            // Handle specific client errors from inventory service (e.g., 409 Conflict)
            LOGGER.error("Error from inventory service: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return handleFailedBooking(request, "Failed: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            // Handle other errors (e.g., network issues, circuit breaker open)
            LOGGER.error("An unexpected error occurred during booking: {}", e.getMessage());
            return handleFailedBooking(request, "Failed due to an unexpected error: " + e.getMessage());
        }
    }

    // This method calls the inventory service. It's protected by a Circuit Breaker.
    @CircuitBreaker(name = "inventoryServiceCircuitBreaker", fallbackMethod = "bookingFallback")
    public InventoryTicketDto reserveTicketInInventory(BookingRequestDto request) {
        String url = inventoryServiceUrl + "/tickets/" + request.getTicketId() + "/reserve";
        Map<String, Long> requestBody = Map.of("customerId", request.getCustomerId());

        LOGGER.info("Calling inventory service at URL: {}", url);
        HttpEntity<Map<String, Long>> entity = new HttpEntity<>(requestBody);

        ResponseEntity<InventoryTicketDto> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                InventoryTicketDto.class
        );

        return response.getBody();
    }

    // Fallback method for the circuit breaker.
    public InventoryTicketDto bookingFallback(BookingRequestDto request, Throwable t) {
        LOGGER.warn("Fallback initiated for booking request: {}. Reason: {}", request, t.getMessage());
        handleFailedBooking(request, "Pending: Inventory service is unavailable.");
        // We throw an exception to ensure the main flow knows the call failed.
        throw new RuntimeException("Inventory service is currently unavailable. Please try again later.");
    }

    // Helper method to create and save a FAILED booking order record.
    private BookingOrder handleFailedBooking(BookingRequestDto request, String reason) {
        BookingOrder failedOrder = BookingOrder.builder()
                .customerId(request.getCustomerId())
                .ticketId(request.getTicketId())
                .status(reason.startsWith("Pending") ? BookingOrder.OrderStatus.PENDING : BookingOrder.OrderStatus.FAILED)
                .eventName("N/A")
                .seatNumber(reason) // Store failure reason here for now
                .build();
        return bookingOrderRepository.save(failedOrder);
    }
}
