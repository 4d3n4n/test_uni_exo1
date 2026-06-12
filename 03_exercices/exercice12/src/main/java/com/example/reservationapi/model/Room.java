package com.example.reservationapi.model;

/**
 * Salle de réunion : identifiant, nom et capacité.
 */
public record Room(Long id, String name, int capacity) {
}
