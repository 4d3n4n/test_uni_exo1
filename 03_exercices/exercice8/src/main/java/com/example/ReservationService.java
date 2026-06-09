package com.example;

import java.util.List;

public class ReservationService {

    private final SalleRepository salleRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;

    public ReservationService(SalleRepository salleRepository,
                              ReservationRepository reservationRepository,
                              NotificationService notificationService) {
        this.salleRepository = salleRepository;
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
    }

    public Confirmation reserver(Reservation reservation) {
        Salle salle = salleRepository.findByCode(reservation.getCodeSalle());
        if (salle == null) {
            throw new ReservationRefuseeException("Salle inconnue");
        }
        if (reservation.getNbParticipants() > salle.getCapaciteMax()) {
            throw new ReservationRefuseeException("Capacité insuffisante");
        }
        if (!reservation.getDateFin().isAfter(reservation.getDateDebut())) {
            throw new ReservationRefuseeException("Période invalide");
        }
        List<Reservation> existantes = reservationRepository.findByCodeSalle(reservation.getCodeSalle());
        for (Reservation existante : existantes) {
            if (chevauche(existante, reservation)) {
                throw new ReservationRefuseeException("Salle déjà réservée");
            }
        }
        notificationService.envoyerConfirmation(reservation.getEmail());
        return new Confirmation(reservation.getCodeSalle(), reservation.getEmail(), "Réservation confirmée");
    }

    private boolean chevauche(Reservation a, Reservation b) {
        return a.getDateDebut().isBefore(b.getDateFin())
                && b.getDateDebut().isBefore(a.getDateFin());
    }
}
