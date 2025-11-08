package com.booking.notification_service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderConfirmationDto {
    private Long orderId;
    private Long customerId;
    private String customerEmail;
    private String eventName;
    private String seatNumber;
}
