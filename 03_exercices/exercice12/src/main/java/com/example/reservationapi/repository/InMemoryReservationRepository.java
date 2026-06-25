package com.example.reservationapi.repository;

import com.example.reservationapi.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Stockage en mémoire des réservations, avec recherche par salle.
 */
@Repository
public class InMemoryReservationRepository implements ReservationRepository {

    private final AtomicLong sequence = new AtomicLong(0);
    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();

    @Override
    public Reservation save(Reservation reservation) {
        Long id = reservation.id() != null ? reservation.id() : sequence.incrementAndGet();
        Reservation stored = new Reservation(
                id,
                reservation.roomId(),
                reservation.requester(),
                reservation.start(),
                reservation.end(),
                reservation.status());
        reservations.put(id, stored);
        return stored;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(reservations.get(id));
    }

    @Override
    public List<Reservation> findByRoomId(Long roomId) {
        return new ArrayList<>(reservations.values())
                .stream()
                .filter(reservation -> reservation.roomId().equals(roomId))
                .sorted(Comparator.comparing(Reservation::id))
                .toList();
    }

    @Override
    public void deleteAll() {
        reservations.clear();
        sequence.set(0);
    }
}
