package com.example.supportapi.dto;

import com.example.supportapi.model.Priority;
import com.example.supportapi.model.Ticket;
import com.example.supportapi.model.TicketStatus;

public record TicketResponse(
        Long id,
        String title,
        Priority priority,
        TicketStatus status
) {
    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(ticket.id(), ticket.title(), ticket.priority(), ticket.status());
    }
}
