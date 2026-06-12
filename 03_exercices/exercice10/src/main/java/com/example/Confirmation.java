package com.example;

/**
 * Message de confirmation renvoyé après une opération réussie.
 */
public class Confirmation {
    private final String message;

    public Confirmation(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
