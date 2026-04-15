package com.andre.core.model;


import com.andre.core.services.dto.JWToken;

public interface TokenCache {
    void addRevokedToken(JWToken jwToken);
    void purgeRevokedTokens();
    boolean isRevoked(JWToken jwToken);
}
