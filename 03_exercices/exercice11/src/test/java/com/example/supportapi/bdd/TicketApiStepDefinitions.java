package com.example.supportapi.bdd;

import com.example.supportapi.model.Priority;
import com.example.supportapi.model.Ticket;
import com.example.supportapi.model.TicketStatus;
import com.example.supportapi.repository.TicketRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Step definitions BDD : pilotent l'API réelle via MockMvc.
 * Aucune logique métier ici, on traverse l'application comme un vrai client HTTP.
 */
public class TicketApiStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository repository;

    private ResultActions lastResult;
    private Long lastTicketId;

    @Given("aucun ticket n existe dans l API")
    public void aucunTicketNExiste() {
        repository.deleteAll();
        lastTicketId = null;
    }

    @Given("un ticket ouvert existe avec le titre {string}")
    public void unTicketOuvertExiste(String titre) {
        Ticket saved = repository.save(new Ticket(null, titre, Priority.MEDIUM, TicketStatus.OPEN));
        lastTicketId = saved.id();
    }

    @Given("un ticket resolu existe avec le titre {string}")
    public void unTicketResoluExiste(String titre) {
        Ticket saved = repository.save(new Ticket(null, titre, Priority.MEDIUM, TicketStatus.RESOLVED));
        lastTicketId = saved.id();
    }

    @When("je cree un ticket avec le titre {string} et la priorite {string}")
    public void jeCreeUnTicket(String titre, String priorite) throws Exception {
        lastResult = mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"" + titre + "\",\"priority\":\"" + priorite + "\"}"));
    }

    @When("je passe le ticket au statut {string}")
    public void jePasseLeTicketAuStatut(String statut) throws Exception {
        lastResult = mockMvc.perform(patch("/api/tickets/" + lastTicketId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"" + statut + "\"}"));
    }

    @When("je demande le ticket avec l identifiant {long}")
    public void jeDemandeLeTicket(Long id) throws Exception {
        lastResult = mockMvc.perform(get("/api/tickets/" + id));
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
}
