package com.example.bankapi.exception;

public class AccountAlreadyExistsException extends RuntimeException {

    public AccountAlreadyExistsException(String number) {
        super("Un compte existe deja avec le numero " + number);
    }
}
