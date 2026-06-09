package com.example;

import java.time.LocalDateTime;

public class Reservation {

    private final String email;
    private final String codeSalle;
    private final int nbParticipants;
    private final LocalDateTime dateDebut;
    private final LocalDateTime dateFin;

    public Reservation(String email, String codeSalle, int nbParticipants,
                       LocalDateTime dateDebut, LocalDateTime dateFin) {
        this.email = email;
        this.codeSalle = codeSalle;
        this.nbParticipants = nbParticipants;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public String getEmail() {
        return email;
    }

    public String getCodeSalle() {
        return codeSalle;
    }

    public int getNbParticipants() {
        return nbParticipants;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }
}
