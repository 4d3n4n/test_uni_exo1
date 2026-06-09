package com.example;

public enum ClientType {
    STANDARD(0.0),
    PREMIUM(0.10),
    VIP(0.20);

    private final double remise;

    ClientType(double remise) {
        this.remise = remise;
    }

    public double getRemise() {
        return remise;
    }
}
