package com.example.bankapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Corps d'un virement entre deux comptes.
 */
public record TransferRequest(
        @NotBlank(message = "Le compte emetteur est obligatoire")
        String from,

        @NotBlank(message = "Le compte destinataire est obligatoire")
        String to,

        @NotNull(message = "Le montant est obligatoire")
        @Positive(message = "Le montant doit etre strictement positif")
        BigDecimal amount
) {
}
