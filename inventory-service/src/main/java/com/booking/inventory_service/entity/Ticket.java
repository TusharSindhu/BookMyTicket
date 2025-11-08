package com.booking.inventory_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventName;
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private String reservedByCustomerId; // To track who reserved the ticket

    /**
     * The @Version annotation is the key to enabling optimistic locking.
     * JPA uses this field to track concurrent modifications to an entity.
     * When we try to update an entity, JPA checks if the version in the database
     * matches the version of the entity we have in memory. If they don't match,
     * it means another transaction has updated it, and it throws an
     * ObjectOptimisticLockingFailureException.
     */
    @Version
    private Long version;

    public enum TicketStatus {
        AVAILABLE,
        RESERVED,
        SOLD
    }
}
