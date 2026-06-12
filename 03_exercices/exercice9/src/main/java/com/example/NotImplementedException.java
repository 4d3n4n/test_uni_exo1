package com.example;

/**
 * Levée par les méthodes non encore implémentées (phase RED du TDD).
 */
public class NotImplementedException extends RuntimeException {
    public NotImplementedException() {
        super("Not implemented yet");
    }
}
