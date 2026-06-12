package com.example.supportapi.exception;

public class TicketStatusConflictException extends RuntimeException {

    public TicketStatusConflictException(String message) {
        super(message);
    }
}
