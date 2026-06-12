package com.example;

/**
 * Levée lorsque la connexion échoue (identifiants invalides).
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
