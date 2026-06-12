package com.example.reservationapi.exception;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(Long id) {
        super("Aucune reservation trouvee avec l'identifiant " + id);
    }
}
