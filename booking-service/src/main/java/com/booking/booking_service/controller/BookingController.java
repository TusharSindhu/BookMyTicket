package com.booking.booking_service.controller;

import com.booking.booking_service.dto.BookingRequestDto;
import com.booking.booking_service.entity.BookingOrder;
import com.booking.booking_service.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    @Operation(summary = "Create a new booking", description = "Attempts to book a ticket for a customer.")
    public ResponseEntity<BookingOrder> createBooking(@RequestBody BookingRequestDto bookingRequest) {
        BookingOrder order = bookingService.createBooking(bookingRequest);
        if (order.getStatus() == BookingOrder.OrderStatus.CONFIRMED) {
            return ResponseEntity.ok(order);
        } else {
            // Return a client error status for failed or pending bookings
            return ResponseEntity.status(409).body(order);
        }
    }
}
