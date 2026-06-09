package com.example;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ReservationStepDefinitions {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private SalleRepository salleRepository;
    private ReservationRepository reservationRepository;
    private NotificationService notificationService;
    private ReservationService reservationService;

    private List<Reservation> existantes;
    private Reservation reservationDemandee;
    private Confirmation confirmation;
    private ReservationRefuseeException exception;

    @Before
    public void setUp() {
        salleRepository = mock(SalleRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        notificationService = mock(NotificationService.class);
        reservationService = new ReservationService(salleRepository, reservationRepository, notificationService);
        existantes = new ArrayList<>();
        reservationDemandee = null;
        confirmation = null;
        exception = null;
    }

    @Given("la salle {string} {string} d'une capacité de {int} personnes")
    public void laSalleExiste(String code, String nom, int capacite) {
        Salle salle = new Salle(code, nom, capacite);
        when(salleRepository.findByCode(code)).thenReturn(salle);
    }

    @Given("la salle {string} n'existe pas")
    public void laSalleNExistePas(String code) {
        when(salleRepository.findByCode(code)).thenReturn(null);
    }

    @Given("une réservation existante pour la salle {string} de {string} à {string}")
    public void uneReservationExistante(String codeSalle, String debut, String fin) {
        existantes.add(new Reservation(null, codeSalle, 0, parse(debut), parse(fin)));
        when(reservationRepository.findByCodeSalle(codeSalle)).thenReturn(existantes);
    }

    @When("{string} réserve la salle {string} pour {int} personnes de {string} à {string}")
    public void reserve(String email, String codeSalle, int participants, String debut, String fin) {
        reservationDemandee = new Reservation(email, codeSalle, participants, parse(debut), parse(fin));
        try {
            confirmation = reservationService.reserver(reservationDemandee);
        } catch (ReservationRefuseeException e) {
            exception = e;
        }
    }

    @Then("la réservation est acceptée")
    public void laReservationEstAcceptee() {
        assertNull(exception, "La réservation aurait dû être acceptée");
        assertNotNull(confirmation);
        assertEquals(reservationDemandee.getCodeSalle(), confirmation.getCodeSalle());
        assertEquals(reservationDemandee.getEmail(), confirmation.getEmail());
        assertEquals("Réservation confirmée", confirmation.getMessage());
    }

    @Then("la réservation est refusée avec le message {string}")
    public void laReservationEstRefusee(String message) {
        assertNotNull(exception, "La réservation aurait dû être refusée");
        assertEquals(message, exception.getMessage());
    }

    @And("une notification de confirmation est envoyée à {string}")
    public void uneNotificationEstEnvoyee(String email) {
        verify(notificationService).envoyerConfirmation(email);
    }

    @And("aucune notification n'est envoyée")
    public void aucuneNotificationEnvoyee() {
        verify(notificationService, never()).envoyerConfirmation(anyString());
    }

    private LocalDateTime parse(String dateTime) {
        return LocalDateTime.parse(dateTime, FORMAT);
    }
}
