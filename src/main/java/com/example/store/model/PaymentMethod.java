package com.example.store.model;

public enum PaymentMethod {
    VISA("Visa"),
    MASTERCARD("Mastercard");

    private final String value;

    private PaymentMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}