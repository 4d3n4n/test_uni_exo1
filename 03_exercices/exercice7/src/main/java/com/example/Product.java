package com.example;

public class Product {

    private final String reference;
    private final String nom;
    private final double prixUnitaire;
    private final int stockDisponible;

    public Product(String reference, String nom, double prixUnitaire, int stockDisponible) {
        this.reference = reference;
        this.nom = nom;
        this.prixUnitaire = prixUnitaire;
        this.stockDisponible = stockDisponible;
    }

    public String getReference() {
        return reference;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }
}
