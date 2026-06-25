package com.example.reservationapi.bdd;

import com.example.reservationapi.model.Reservation;
import com.example.reservationapi.model.ReservationStatus;
import com.example.reservationapi.model.Room;
import com.example.reservationapi.repository.ReservationRepository;
import com.example.reservationapi.repository.RoomRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Step definitions BDD : pilotent l'API réelle via MockMvc, sans logique métier.
 */
public class ReservationStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private ResultActions lastResult;
    private Long lastRoomId;

    @Given("aucune salle n existe dans l API")
    public void aucuneSalleNExiste() {
        reservationRepository.deleteAll();
        roomRepository.deleteAll();
        lastRoomId = null;
    }

    @Given("une salle {string} d une capacite de {int} existe")
    public void uneSalleExiste(String nom, int capacite) {
        Room saved = roomRepository.save(new Room(null, nom, capacite));
        lastRoomId = saved.id();
    }

    @Given("une reservation confirmee existe pour cette salle de {string} a {string}")
    public void uneReservationConfirmeeExiste(String debut, String fin) {
        reservationRepository.save(new Reservation(
                null, lastRoomId, "Occupant",
                LocalDateTime.parse(debut), LocalDateTime.parse(fin),
                ReservationStatus.CONFIRMED));
    }

    @When("je reserve la salle existante pour {string} de {string} a {string}")
    public void jeReserveLaSalleExistante(String reservant, String debut, String fin) throws Exception {
        lastResult = performReservation(lastRoomId, reservant, debut, fin);
    }

    @When("je reserve la salle {long} pour {string} de {string} a {string}")
    public void jeReserveLaSalle(Long roomId, String reservant, String debut, String fin) throws Exception {
        lastResult = performReservation(roomId, reservant, debut, fin);
    }

    @Then("la reponse HTTP doit etre {int}")
    public void laReponseHttpDoitEtre(int statutAttendu) throws Exception {
        lastResult.andExpect(status().is(statutAttendu));
    }

    @Then("la reponse contient le statut {string}")
    public void laReponseContientLeStatut(String statutAttendu) throws Exception {
        lastResult.andExpect(jsonPath("$.status").value(statutAttendu));
    }

    @Then("la reponse contient un message d erreur")
    public void laReponseContientUnMessageDErreur() throws Exception {
        lastResult.andExpect(jsonPath("$.message").exists());
    }

    private ResultActions performReservation(Long roomId, String reservant, String debut, String fin)
            throws Exception {
        String body = "{\"roomId\":" + roomId
                + ",\"requester\":\"" + reservant + "\""
                + ",\"start\":\"" + debut + "\""
                + ",\"end\":\"" + fin + "\"}";
        return mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }
}
