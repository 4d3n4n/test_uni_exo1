package com.example.reservationapi.repository;

import com.example.reservationapi.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryReservationRepository implements ReservationRepository {

    @Override
    public Reservation save(Reservation reservation) {
        throw new UnsupportedOperationException("Repository non implemente en phase RED");
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        throw new UnsupportedOperationException("Repository non implemente en phase RED");
    }

    @Override
    public List<Reservation> findByRoomId(Long roomId) {
        throw new UnsupportedOperationException("Repository non implemente en phase RED");
    }

    @Override
    public void deleteAll() {
        // No-op en phase RED : permet aux tests d'integration de demarrer leur parcours.
    }
}
