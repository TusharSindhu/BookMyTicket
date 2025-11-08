package com.booking.inventory_service.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import jakarta.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {


    // This handler is specifically for the ObjectOptimisticLockingFailureException.
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Object> handleOptimisticLockingFailure(ObjectOptimisticLockingFailureException ex, WebRequest request) {
        System.err.println("OPTIMISTIC LOCKING CONFLICT: " + ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Failed to reserve ticket. Another user just booked it. Please try another one.");
        body.put("error", "Concurrent modification detected.");
        return new ResponseEntity<>(body, HttpStatus.CONFLICT); // HTTP 409
    }

    // Handles the case where a ticket is not available (e.g., already reserved or sold).
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalState(IllegalStateException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "The requested operation cannot be completed.");
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST); // HTTP 400
    }

    // ExceptionHandler(EntityNotFoundException.class)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "The requested resource was not found.");
        body.put("error", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND); // HTTP 404
    }

    // A generic handler for any other exceptions that might occur.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        System.err.println("UNEXPECTED ERROR: " + ex.getMessage());
        ex.printStackTrace();
        Map<String, Object> body = new HashMap<>();
        body.put("message", "An unexpected error occurred.");
        body.put("error", ex.getLocalizedMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR); // HTTP 500
    }
}
