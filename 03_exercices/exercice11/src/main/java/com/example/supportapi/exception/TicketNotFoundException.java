package com.example.supportapi.exception;

public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException(Long id) {
        super("Aucun ticket trouve avec l'identifiant " + id);
    }
}
