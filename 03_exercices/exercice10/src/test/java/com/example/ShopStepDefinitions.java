package com.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Step definitions de la boutique en ligne.
 * Les repositories sont des mocks Mockito : les services sont ainsi isolés
 * de toute persistance, on contrôle leurs réponses dans les Given.
 */
public class ShopStepDefinitions {

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    private AccountService accountService;
    private CatalogService catalogService;
    private OrderService orderService;

    private final Map<String, Order> orders = new HashMap<>();

    private Confirmation confirmation;
    private List<Product> searchResults;
    private RuntimeException caughtException;

    @Before
    public void setUp() {
        userRepository = mock(UserRepository.class);
        productRepository = mock(ProductRepository.class);
        orderRepository = mock(OrderRepository.class);

        accountService = new AccountService(userRepository);
        catalogService = new CatalogService(productRepository);
        orderService = new OrderService(orderRepository);

        orders.clear();
        confirmation = null;
        searchResults = null;
        caughtException = null;
    }

    // ---------- Création de compte ----------

    @Given("aucun compte n'existe pour le nom d'utilisateur {string}")
    public void aucunCompte(String username) {
        when(userRepository.findByUsername(username)).thenReturn(null);
    }

    @Given("un compte existe déjà pour le nom d'utilisateur {string}")
    public void compteExistant(String username) {
        when(userRepository.findByUsername(username))
                .thenReturn(new User(username + "@mail.com", username, "secret"));
    }

    @When("l'utilisateur s'inscrit avec l'email {string}, le nom d'utilisateur {string} et le mot de passe {string}")
    public void inscription(String email, String username, String password) {
        try {
            confirmation = accountService.register(email, username, password);
        } catch (RuntimeException e) {
            caughtException = e;
        }
    }

    @Then("l'inscription est confirmée pour {string}")
    public void inscriptionConfirmee(String username) {
        assertNull(caughtException);
        assertNotNull(confirmation);
        assertTrue(confirmation.getMessage().contains(username));
    }

    @Then("l'inscription est refusée")
    public void inscriptionRefusee() {
        assertInstanceOf(CompteExistantException.class, caughtException);
    }

    // ---------- Connexion ----------

    @Given("un compte existe avec le nom d'utilisateur {string} et le mot de passe {string}")
    public void compteAvecMotDePasse(String username, String password) {
        when(userRepository.findByUsername(username))
                .thenReturn(new User(username + "@mail.com", username, password));
    }

    @When("{string} se connecte avec le mot de passe {string}")
    public void seConnecte(String username, String password) {
        try {
            confirmation = accountService.login(username, password);
        } catch (RuntimeException e) {
            caughtException = e;
        }
    }

    @Then("la connexion réussit et l'utilisateur est redirigé vers la page d'accueil")
    public void connexionReussie() {
        assertNull(caughtException);
        assertNotNull(confirmation);
    }

    @Then("la connexion échoue avec un message d'erreur")
    public void connexionEchouee() {
        assertInstanceOf(AuthenticationException.class, caughtException);
        assertFalse(caughtException.getMessage().isEmpty());
    }

    // ---------- Recherche / Navigation ----------

    @Given("le catalogue contient les produits :")
    public void leCatalogueContient(DataTable dataTable) {
        List<Product> products = new ArrayList<>();
        for (Map<String, String> row : dataTable.asMaps()) {
            products.add(new Product(
                    row.get("nom"),
                    row.get("categorie"),
                    Integer.parseInt(row.get("prix"))));
        }
        when(productRepository.findAll()).thenReturn(products);
    }

    @When("l'utilisateur recherche les produits contenant {string}")
    public void rechercheParMotCle(String keyword) {
        searchResults = catalogService.searchByKeyword(keyword);
    }

    @When("l'utilisateur recherche les produits à un prix maximum de {int}")
    public void rechercheParPrix(int maxPrice) {
        searchResults = catalogService.searchByMaxPrice(maxPrice);
    }

    @When("l'utilisateur sélectionne la catégorie {string}")
    public void selectionneCategorie(String category) {
        searchResults = catalogService.findByCategory(category);
    }

    @Then("les résultats contiennent {string}")
    public void resultatsContiennent(String nom) {
        assertTrue(searchResults.stream().anyMatch(p -> p.getNom().equals(nom)));
    }

    @Then("les résultats ne contiennent pas {string}")
    public void resultatsNeContiennentPas(String nom) {
        assertTrue(searchResults.stream().noneMatch(p -> p.getNom().equals(nom)));
    }

    // ---------- Commande ----------

    @Given("le produit {string} {string} au prix de {int}")
    public void leProduit(String productId, String nom, int prix) {
        // Le produit est disponible pour être commandé ; les scénarios de commande
        // ne manipulent que son identifiant.
    }

    @Given("une commande {string} vide")
    public void uneCommandeVide(String orderId) {
        Order order = new Order(orderId);
        orders.put(orderId, order);
        when(orderRepository.findById(orderId)).thenReturn(order);
    }

    @Given("la commande {string} contient déjà {int} fois le produit {string}")
    public void laCommandeContientDeja(String orderId, int quantity, String productId) {
        Order order = orders.get(orderId);
        for (int i = 0; i < quantity; i++) {
            order.addProduct(productId);
        }
    }

    @When("l'utilisateur ajoute le produit {string} à la commande {string}")
    public void ajouteProduit(String productId, String orderId) {
        try {
            confirmation = orderService.addProduct(orderId, productId);
        } catch (RuntimeException e) {
            caughtException = e;
        }
    }

    @When("l'utilisateur supprime le produit {string} de la commande {string}")
    public void supprimeProduit(String productId, String orderId) {
        try {
            orderService.removeProduct(orderId, productId);
        } catch (RuntimeException e) {
            caughtException = e;
        }
    }

    @When("l'utilisateur valide la commande {string}")
    public void valideCommande(String orderId) {
        try {
            confirmation = orderService.validate(orderId);
        } catch (RuntimeException e) {
            caughtException = e;
        }
    }

    @Then("l'ajout est confirmé")
    public void ajoutConfirme() {
        assertNull(caughtException);
        assertNotNull(confirmation);
    }

    @Then("la commande est confirmée")
    public void commandeConfirmee() {
        assertNull(caughtException);
        assertNotNull(confirmation);
    }

    @Then("la commande {string} contient {int} fois le produit {string}")
    public void laCommandeContient(String orderId, int quantity, String productId) {
        assertEquals(quantity, orders.get(orderId).quantityOf(productId));
    }

    @Then("la commande {string} ne contient pas le produit {string}")
    public void laCommandeNeContientPas(String orderId, String productId) {
        assertEquals(0, orders.get(orderId).quantityOf(productId));
    }

    @Then("une erreur de commande inexistante est renvoyée")
    public void erreurCommandeInexistante() {
        assertInstanceOf(CommandeInexistanteException.class, caughtException);
    }

    @Then("une erreur de produit absent est renvoyée")
    public void erreurProduitAbsent() {
        assertInstanceOf(ProduitAbsentException.class, caughtException);
    }
}
