package com.example.supportapi.model;

public record Ticket(
        Long id,
        String title,
        Priority priority,
        TicketStatus status
) {
}
