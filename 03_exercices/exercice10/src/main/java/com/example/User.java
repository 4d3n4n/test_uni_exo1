package com.example;

/**
 * Compte utilisateur de la boutique.
 */
public class User {
    private final String email;
    private final String username;
    private final String password;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
