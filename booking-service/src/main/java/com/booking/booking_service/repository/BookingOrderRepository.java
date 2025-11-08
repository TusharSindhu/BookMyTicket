package com.booking.booking_service.repository;

import com.booking.booking_service.entity.BookingOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingOrderRepository extends JpaRepository<BookingOrder, Long> {
}
