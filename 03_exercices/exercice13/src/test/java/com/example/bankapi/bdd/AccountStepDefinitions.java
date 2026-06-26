package com.example.bankapi.bdd;

import com.example.bankapi.model.Account;
import com.example.bankapi.repository.AccountRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Step definitions BDD : pilotent l'API réelle via MockMvc, sans logique métier.
 */
public class AccountStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository repository;

    private ResultActions lastResult;

    @Given("aucun compte n existe dans l API")
    public void aucunCompteNExiste() {
        repository.deleteAll();
    }

    @Given("un compte numero {string} pour {string} avec un solde de {int}")
    public void unCompteAvecSolde(String numero, String titulaire, int solde) {
        repository.save(new Account(numero, titulaire, BigDecimal.valueOf(solde)));
    }

    @When("je cree un compte numero {string} pour {string}")
    public void jeCreeUnCompte(String numero, String titulaire) throws Exception {
        lastResult = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"number\":\"" + numero + "\",\"holder\":\"" + titulaire + "\"}"));
    }

    @When("je depose {int} sur le compte {string}")
    public void jeDepose(int montant, String numero) throws Exception {
        lastResult = performAmount("/accounts/" + numero + "/deposit", montant);
    }

    @When("je retire {int} du compte {string}")
    public void jeRetire(int montant, String numero) throws Exception {
        lastResult = performAmount("/accounts/" + numero + "/withdraw", montant);
    }

    @When("je vire {int} du compte {string} vers le compte {string}")
    public void jeVire(int montant, String source, String destination) throws Exception {
        lastResult = mockMvc.perform(post("/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"from\":\"" + source + "\",\"to\":\"" + destination
                        + "\",\"amount\":" + montant + "}"));
    }

    @Then("la reponse HTTP doit etre {int}")
    public void laReponseHttpDoitEtre(int statutAttendu) throws Exception {
        lastResult.andExpect(status().is(statutAttendu));
    }

    @Then("la reponse contient le solde {int}")
    public void laReponseContientLeSolde(int soldeAttendu) throws Exception {
        lastResult.andExpect(jsonPath("$.balance").value(soldeAttendu));
    }

    @Then("la reponse contient un message d erreur")
    public void laReponseContientUnMessageDErreur() throws Exception {
        lastResult.andExpect(jsonPath("$.message").exists());
    }

    private ResultActions performAmount(String url, int montant) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":" + montant + "}"));
    }
}
