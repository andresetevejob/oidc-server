package com.andre.core.model.extensions;

import com.andre.core.model.AuthenticationRequest;
import com.andre.core.model.ClientId;

public record UPAuthenticationRequest(ClientId clientId, String password) implements
    AuthenticationRequest<UPCredentialsType> {

    @Override
    public ClientId getClientId() {
        return clientId;
    }

    @Override
    public UPCredentialsType getCredentialsType() {
        return new UPCredentialsType();
    }
}
