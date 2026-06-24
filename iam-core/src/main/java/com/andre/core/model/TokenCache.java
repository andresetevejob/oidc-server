package com.andre.core.model;


import com.andre.core.services.dto.JWToken;

public interface TokenCache {
    void addRevokedToken(JWToken jwToken);
    int purgeRevokedTokens();
    boolean isRevoked(JWToken jwToken);
    int size();
}
