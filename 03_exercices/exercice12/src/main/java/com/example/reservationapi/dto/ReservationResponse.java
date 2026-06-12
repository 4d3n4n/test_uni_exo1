package com.example.reservationapi.dto;

import com.example.reservationapi.model.Reservation;
import com.example.reservationapi.model.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long roomId,
        String requester,
        LocalDateTime start,
        LocalDateTime end,
        ReservationStatus status
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.roomId(),
                reservation.requester(),
                reservation.start(),
                reservation.end(),
                reservation.status());
    }
}
