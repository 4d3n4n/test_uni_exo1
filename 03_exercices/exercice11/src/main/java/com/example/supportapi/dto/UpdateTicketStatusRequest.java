package com.example.supportapi.dto;

import com.example.supportapi.model.TicketStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateTicketStatusRequest(
        @NotNull(message = "Le statut est obligatoire")
        TicketStatus status
) {
}
