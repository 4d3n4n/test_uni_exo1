package com.example;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommandeStepDefinitions {

    private ProductRepository productRepository;
    private OrderService orderService;

    private Recu recu;
    private CommandeRefuseeException exception;

    @Before
    public void setUp() {
        productRepository = mock(ProductRepository.class);
        orderService = new OrderService(productRepository);
        recu = null;
        exception = null;
    }

    @Given("le produit {string} {string} au prix de {double} euros avec un stock de {int}")
    public void leProduitExiste(String reference, String nom, double prix, int stock) {
        Product product = new Product(reference, nom, prix, stock);
        when(productRepository.findByReference(reference)).thenReturn(product);
    }

    @Given("le produit {string} n'existe pas")
    public void leProduitNExistePas(String reference) {
        when(productRepository.findByReference(reference)).thenReturn(null);
    }

    @When("un client {string} commande {int} unité(s) du produit {string}")
    public void unClientCommande(String profil, int quantite, String reference) {
        try {
            recu = orderService.passerCommande(reference, quantite, ClientType.valueOf(profil));
        } catch (CommandeRefuseeException e) {
            exception = e;
        }
    }

    @Then("la commande est acceptée")
    public void laCommandeEstAcceptee() {
        assertNull(exception, "La commande aurait dû être acceptée");
        assertNotNull(recu);
    }

    @And("le montant total est de {double} euros")
    public void leMontantTotalEstDe(double montant) {
        assertEquals(montant, recu.getMontantTotal());
    }

    @Then("la commande est refusée avec le message {string}")
    public void laCommandeEstRefusee(String message) {
        assertNotNull(exception, "La commande aurait dû être refusée");
        assertEquals(message, exception.getMessage());
    }
}
