package com.example.bankapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Corps des opérations de dépôt et de retrait.
 */
public record AmountRequest(
        @NotNull(message = "Le montant est obligatoire")
        @Positive(message = "Le montant doit etre strictement positif")
        BigDecimal amount
) {
}
