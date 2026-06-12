package com.example.reservationapi.repository;

import com.example.reservationapi.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    Optional<Reservation> findById(Long id);

    List<Reservation> findByRoomId(Long roomId);

    void deleteAll();
}
