package com.andre.core.services.impl;

import com.andre.core.model.AuthenticationRequest;
import com.andre.core.model.Client;
import com.andre.core.model.Model;
import com.andre.core.model.TokenCache;
import com.andre.core.services.ClientService;
import com.andre.core.services.dto.JWToken;
import java.util.Optional;

public class ClientServiceImpl implements ClientService {

    private final Model model;
    private final TokenCache tokenCache;

    public ClientServiceImpl(Model model, TokenCache tokenCache) {
        this.model = model;
        this.tokenCache = tokenCache;
    }

    @Override
    public Optional<JWToken> authenticate(AuthenticationRequest authenticationRequest) {
        Optional<Client> client = model.getClient(authenticationRequest.clientId());
        if (client.isPresent()) {

        }
        return Optional.empty();
    }

    @Override
    public Optional<JWToken> renew(JWToken token) {
        return Optional.empty();
    }

    @Override
    public boolean logout(JWToken token) {
        //TODO: validate if toke is already not expired
        tokenCache.addRevokedToken(token);
        return true;
    }
}