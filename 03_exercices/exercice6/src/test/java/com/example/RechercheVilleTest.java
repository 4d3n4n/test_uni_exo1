package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RechercheVilleTest {

    private RechercheVille rechercheVille;

    @BeforeEach
    void setUp() {
        rechercheVille = new RechercheVille(List.of(
                "Paris", "Budapest", "Skopje", "Rotterdam", "Valence",
                "Vancouver", "Amsterdam", "Vienne", "Sydney", "New York",
                "Londres", "Bangkok", "Hong Kong", "Dubaï", "Rome", "Istanbul"
        ));
    }

    // Etape 1 : moins de 2 caractères → NotFoundException
    @Test
    void shouldThrowNotFoundExceptionWhenSearchTextIsEmpty() {
        assertThrows(NotFoundException.class, () -> rechercheVille.Rechercher(""));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenSearchTextIsOneChar() {
        assertThrows(NotFoundException.class, () -> rechercheVille.Rechercher("P"));
    }

    // Etape 2 : recherche exacte par début de nom
    @Test
    void shouldReturnValenceAndVancouverWhenSearchingVa() {
        List<String> result = rechercheVille.Rechercher("Va");
        assertEquals(List.of("Valence", "Vancouver"), result);
    }

    @Test
    void shouldReturnParisWhenSearchingPar() {
        List<String> result = rechercheVille.Rechercher("Par");
        assertEquals(List.of("Paris"), result);
    }

    // Etape 3 : insensible à la casse
    @Test
    void shouldReturnValenceAndVancouverWhenSearchingLowercaseVa() {
        List<String> result = rechercheVille.Rechercher("va");
        assertEquals(List.of("Valence", "Vancouver"), result);
    }

    @Test
    void shouldReturnParisWhenSearchingUppercasePAR() {
        List<String> result = rechercheVille.Rechercher("PAR");
        assertEquals(List.of("Paris"), result);
    }

    // Etape 4 : recherche partielle dans le nom
    @Test
    void shouldReturnBudapestWhenSearchingApe() {
        List<String> result = rechercheVille.Rechercher("ape");
        assertEquals(List.of("Budapest"), result);
    }

    @Test
    void shouldReturnAmsterdamAndBangkokWhenSearchingAm() {
        List<String> result = rechercheVille.Rechercher("am");
        assertTrue(result.contains("Amsterdam"));
        assertTrue(result.contains("Rotterdam"));
    }

    // Etape 5 : * retourne toutes les villes
    @Test
    void shouldReturnAllCitiesWhenSearchingAsterisk() {
        List<String> result = rechercheVille.Rechercher("*");
        assertEquals(16, result.size());
    }
}
