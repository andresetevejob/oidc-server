package com.andre.core.services.dto;

public record JWToken(String token) {

    public static JWToken from(String jwt) {
        return new JWToken(jwt);
    }
    @Override
    public String toString() {
        return token;
    }
}
