package com.example;

public class Confirmation {

    private final String codeSalle;
    private final String email;
    private final String message;

    public Confirmation(String codeSalle, String email, String message) {
        this.codeSalle = codeSalle;
        this.email = email;
        this.message = message;
    }

    public String getCodeSalle() {
        return codeSalle;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }
}
