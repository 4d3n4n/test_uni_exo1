package com.example.reservationapi.exception;

public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException(Long id) {
        super("Aucune salle trouvee avec l'identifiant " + id);
    }
}
