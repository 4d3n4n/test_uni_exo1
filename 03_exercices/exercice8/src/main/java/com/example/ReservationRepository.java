package com.example;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findByCodeSalle(String codeSalle);
}
