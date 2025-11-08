package com.booking.inventory_service.controller;

import com.booking.inventory_service.dto.ReserveTicketRequestDto;
import com.booking.inventory_service.entity.Ticket;
import com.booking.inventory_service.service.InventoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Inventory API", description = "Endpoints for managing ticket inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/tickets")
    @Operation(summary = "Get all tickets", description = "Returns a list of all tickets in the inventory.")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(inventoryService.getAllTickets());
    }

    @GetMapping("/tickets/{id}")
    @Operation(summary = "Get a single ticket", description = "Returns a ticket by its ID.")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getTicketById(id));
    }

    @PutMapping("/tickets/{id}/reserve")
    @Operation(summary = "Reserve a ticket", description = "Attempts to reserve a ticket for a customer. This uses optimistic locking to handle concurrency.")
    @ApiResponse(responseCode = "200", description = "Ticket successfully reserved")
    @ApiResponse(responseCode = "404", description = "Ticket not found")
    @ApiResponse(responseCode = "409", description = "Conflict - ticket was already reserved by another request (optimistic lock failure)")
    @ApiResponse(responseCode = "400", description = "Bad Request - Ticket is not available")
    public ResponseEntity<Ticket> reserveTicket(@PathVariable Long id, @RequestBody ReserveTicketRequestDto request) {
        Ticket reservedTicket = inventoryService.reserveTicket(id, request.getCustomerId());
        return ResponseEntity.ok(reservedTicket);
    }
}