package com.andre.core.services;

import com.andre.core.model.AuthenticationRequest;
import com.andre.core.services.dto.JWToken;
import java.util.Optional;

public interface ClientService {
  Optional<JWToken> authenticate(final AuthenticationRequest authenticationRequest);
  Optional<JWToken> renew(JWToken token);
  boolean logout(JWToken token);
}
