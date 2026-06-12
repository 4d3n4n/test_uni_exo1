package com.example.reservationapi.service;

import com.example.reservationapi.model.Reservation;
import com.example.reservationapi.repository.ReservationRepository;
import com.example.reservationapi.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservationService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    public Reservation create(Long roomId, String requester, LocalDateTime start, LocalDateTime end) {
        throw new UnsupportedOperationException("Service non implemente en phase RED");
    }

    public Reservation getById(Long id) {
        throw new UnsupportedOperationException("Service non implemente en phase RED");
    }

    public Reservation cancel(Long id) {
        throw new UnsupportedOperationException("Service non implemente en phase RED");
    }
}
