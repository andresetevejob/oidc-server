package com.andre.core.model;

import com.andre.core.services.dto.JWToken;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenCacheImpl implements TokenCache {

    private final Map<ClientId, JWToken> revokedTokens;

    public TokenCacheImpl() {
        this.revokedTokens = new ConcurrentHashMap<>();
    }

    @Override
    public void addRevokedToken(JWToken jwToken) {

    }

    @Override
    public void purgeRevokedTokens() {

    }

    @Override
    public boolean isRevoked(JWToken jwToken) {
        return false;
    }
}
