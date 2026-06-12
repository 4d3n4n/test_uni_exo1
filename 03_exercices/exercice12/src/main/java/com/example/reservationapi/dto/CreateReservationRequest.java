package com.example.reservationapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateReservationRequest(
        @NotNull(message = "L'identifiant de salle est obligatoire")
        Long roomId,

        @NotBlank(message = "Le nom du reservant est obligatoire")
        String requester,

        @NotNull(message = "La date de debut est obligatoire")
        LocalDateTime start,

        @NotNull(message = "La date de fin est obligatoire")
        LocalDateTime end
) {
}
