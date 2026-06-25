package com.example.reservationapi.service;

import com.example.reservationapi.exception.ReservationConflictException;
import com.example.reservationapi.exception.ReservationNotFoundException;
import com.example.reservationapi.exception.RoomNotFoundException;
import com.example.reservationapi.model.Reservation;
import com.example.reservationapi.model.ReservationStatus;
import com.example.reservationapi.repository.ReservationRepository;
import com.example.reservationapi.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Règles métier des réservations : salle existante, réservant obligatoire,
 * créneau valide, absence de chevauchement, annulation unique.
 */
@Service
public class ReservationService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    public Reservation create(Long roomId, String requester, LocalDateTime start, LocalDateTime end) {
        if (roomRepository.findById(roomId).isEmpty()) {
            throw new RoomNotFoundException(roomId);
        }
        if (requester == null || requester.isBlank()) {
            throw new IllegalArgumentException("Le nom du reservant est obligatoire");
        }
        if (start == null || end == null || !end.isAfter(start)) {
            throw new IllegalArgumentException("La date de fin doit etre strictement apres la date de debut");
        }
        if (overlapsExistingReservation(roomId, start, end)) {
            throw new ReservationConflictException("Le creneau chevauche une reservation existante");
        }
        return reservationRepository.save(
                new Reservation(null, roomId, requester.trim(), start, end, ReservationStatus.CONFIRMED));
    }

    public Reservation getById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    public Reservation cancel(Long id) {
        Reservation reservation = getById(id);
        if (reservation.status() == ReservationStatus.CANCELLED) {
            throw new ReservationConflictException("La reservation est deja annulee");
        }
        return reservationRepository.save(new Reservation(
                reservation.id(),
                reservation.roomId(),
                reservation.requester(),
                reservation.start(),
                reservation.end(),
                ReservationStatus.CANCELLED));
    }

    /**
     * Deux créneaux se chevauchent si début1 &lt; fin2 et début2 &lt; fin1.
     * Seules les réservations confirmées de la salle sont prises en compte.
     */
    private boolean overlapsExistingReservation(Long roomId, LocalDateTime start, LocalDateTime end) {
        return reservationRepository.findByRoomId(roomId).stream()
                .filter(existing -> existing.status() == ReservationStatus.CONFIRMED)
                .anyMatch(existing -> start.isBefore(existing.end()) && existing.start().isBefore(end));
    }
}
