package com.example.bankapi.model;

import java.math.BigDecimal;

/**
 * Compte bancaire : numéro unique, titulaire et solde.
 */
public record Account(String number, String holder, BigDecimal balance) {
}
