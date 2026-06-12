package com.example.reservationapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateRoomRequest(
        @NotBlank(message = "Le nom de la salle est obligatoire")
        String name,

        @Min(value = 1, message = "La capacite doit etre superieure ou egale a 1")
        int capacity
) {
}
