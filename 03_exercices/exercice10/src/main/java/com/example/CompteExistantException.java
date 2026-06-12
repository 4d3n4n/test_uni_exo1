package com.example;

/**
 * Levée lorsqu'on tente de créer un compte avec un identifiant déjà existant.
 */
public class CompteExistantException extends RuntimeException {
    public CompteExistantException(String username) {
        super("Un compte existe déjà pour le nom d'utilisateur : " + username);
    }
}
