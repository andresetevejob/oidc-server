package com.andre.core.services.dto;

public record JWToken(String token) {

    @Override
    public String toString() {
        return token;
    }
}
