package com.example.bankapi.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String number) {
        super("Aucun compte trouve avec le numero " + number);
    }
}
