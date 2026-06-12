package com.example;

/**
 * Levée lorsqu'une opération cible une commande qui n'existe pas.
 */
public class CommandeInexistanteException extends RuntimeException {
    public CommandeInexistanteException(String orderId) {
        super("La commande n'existe pas : " + orderId);
    }
}
