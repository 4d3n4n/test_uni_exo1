package com.example.reservationapi.model;

import java.time.LocalDateTime;

/**
 * Réservation d'une salle sur un créneau, pour une personne, avec un statut.
 */
public record Reservation(
        Long id,
        Long roomId,
        String requester,
        LocalDateTime start,
        LocalDateTime end,
        ReservationStatus status
) {
}
