package com.example;

/**
 * Produit du catalogue.
 */
public class Product {
    private final String nom;
    private final String categorie;
    private final int prix;

    public Product(String nom, String categorie, int prix) {
        this.nom = nom;
        this.categorie = categorie;
        this.prix = prix;
    }

    public String getNom() {
        return nom;
    }

    public String getCategorie() {
        return categorie;
    }

    public int getPrix() {
        return prix;
    }
}
