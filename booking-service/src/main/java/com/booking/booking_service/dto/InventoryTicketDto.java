package com.booking.booking_service.dto;

import lombok.Data;

/**
 * DTO to represent the ticket data received from the Inventory Service.
 * This decouples the Booking Service from the Inventory Service's internal domain model.
 */
@Data
public class InventoryTicketDto {
    private Long id;
    private String eventName;
    private String seatNumber;
    private String status;
    private String reservedByCustomerId;
    private Long version;
}