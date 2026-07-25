package com.andre.core.model;

public interface AuthenticationRequest<C extends CredentialsType> {
  ClientId getClientId();
  C getCredentialsType();
}
