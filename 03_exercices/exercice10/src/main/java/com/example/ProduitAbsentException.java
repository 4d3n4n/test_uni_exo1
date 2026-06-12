package com.example;

/**
 * Levée lorsqu'on tente de retirer un produit absent de la commande.
 */
public class ProduitAbsentException extends RuntimeException {
    public ProduitAbsentException(String productId) {
        super("Le produit n'est pas présent dans la commande : " + productId);
    }
}
