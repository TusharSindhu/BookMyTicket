package com.booking.booking_service.dto;

import lombok.Data;

@Data
public class BookingRequestDto {
    private Long customerId;
    private Long ticketId;
}