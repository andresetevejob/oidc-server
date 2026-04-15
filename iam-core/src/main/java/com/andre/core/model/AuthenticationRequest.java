package com.andre.core.model;

public interface AuthenticationRequest<CT extends CredentialsType> {
  ClientId clientId();
  CT getCredentials();
}
