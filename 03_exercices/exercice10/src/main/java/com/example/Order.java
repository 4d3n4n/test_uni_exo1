package com.example;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Commande : associe des produits (par identifiant) à une quantité.
 */
public class Order {
    private final String id;
    private final Map<String, Integer> quantities = new LinkedHashMap<>();

    public Order(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /** Ajoute une unité du produit (le crée avec une quantité de 1 s'il est absent). */
    public void addProduct(String productId) {
        quantities.merge(productId, 1, Integer::sum);
    }

    /** Retire une unité : décrémente si la quantité est &gt; 1, sinon retire le produit. */
    public void removeProduct(String productId) {
        int quantity = quantities.get(productId);
        if (quantity > 1) {
            quantities.put(productId, quantity - 1);
        } else {
            quantities.remove(productId);
        }
    }

    public boolean contains(String productId) {
        return quantities.containsKey(productId);
    }

    public int quantityOf(String productId) {
        return quantities.getOrDefault(productId, 0);
    }
}
