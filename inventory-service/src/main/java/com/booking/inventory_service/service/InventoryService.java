package com.booking.inventory_service.service;

import com.booking.inventory_service.entity.Ticket;
import com.booking.inventory_service.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private TicketRepository ticketRepository;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + ticketId));
    }


    //  This method handles the core logic for reserving a ticket.
    @Transactional
    public Ticket reserveTicket(Long ticketId, String customerId) {
        System.out.println("Attempting to reserve ticket " + ticketId + " for customer " + customerId);

        // 1. Find the ticket by ID. This will fetch the latest version from the database.
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + ticketId));

        System.out.println("Found ticket: " + ticket.getSeatNumber() + " with status: " + ticket.getStatus() + " and version: " + ticket.getVersion());

        // 2. Check if the ticket is available for reservation.
        if (ticket.getStatus() != Ticket.TicketStatus.AVAILABLE) {
            System.out.println("Ticket " + ticketId + " is not available. Current status: " + ticket.getStatus());
            throw new IllegalStateException("Ticket is not available for reservation.");
        }

        // 3. Update the ticket status and assign it to the customer.
        ticket.setStatus(Ticket.TicketStatus.RESERVED);
        ticket.setReservedByCustomerId(customerId);

        // 4. Save the ticket.
        Ticket savedTicket = ticketRepository.save(ticket);
        System.out.println("Successfully reserved ticket " + ticketId + ". New version is " + savedTicket.getVersion());
        return savedTicket;
    }

    @Transactional
    public void addSampleTickets() {
        if (ticketRepository.count() == 0) {
            ticketRepository.save(new Ticket(null, "Event A", "A1", Ticket.TicketStatus.AVAILABLE, null, 0L));
            ticketRepository.save(new Ticket(null, "Event A", "A2", Ticket.TicketStatus.AVAILABLE, null, 0L));
            ticketRepository.save(new Ticket(null, "Event B", "B5", Ticket.TicketStatus.AVAILABLE, null, 0L));
            ticketRepository.save(new Ticket(null, "Event B", "B6", Ticket.TicketStatus.AVAILABLE, null, 0L));
            ticketRepository.save(new Ticket(null, "Event C", "C1", Ticket.TicketStatus.AVAILABLE, null, 0L));
            ticketRepository.save(new Ticket(null, "Event C", "C2", Ticket.TicketStatus.AVAILABLE, null, 0L));
            ticketRepository.save(new Ticket(null, "Event C", "C3", Ticket.TicketStatus.AVAILABLE, null, 0L));
            ticketRepository.save(new Ticket(null, "Event D", "D4", Ticket.TicketStatus.AVAILABLE, null, 0L));
            ticketRepository.save(new Ticket(null, "Event E", "E8", Ticket.TicketStatus.AVAILABLE, null, 0L));
            ticketRepository.save(new Ticket(null, "Event E", "E9", Ticket.TicketStatus.AVAILABLE, null, 0L));
            System.out.println("Added 10 sample tickets to the database.");
        }
    }
}
