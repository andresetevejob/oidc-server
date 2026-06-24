package com.andre.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.andre.core.model.ClientId;
import com.andre.core.model.Model;
import com.andre.core.model.ModelUtils;
import com.andre.core.model.TokenCache;
import com.andre.core.model.TokenCacheImpl;
import com.andre.core.model.extensions.UPAuthenticationRequest;
import com.andre.core.services.ClientService;
import com.andre.core.services.ResourceServerService;
import com.andre.core.services.dto.JWToken;
import com.andre.core.services.impl.ClientServiceImpl;
import com.andre.core.services.impl.ResourceServerServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClientTests {
    private static Model model;
    private static ClientService clientService;
    private static ResourceServerService resourceServerService;
    private static TokenCache tokenCache;
    private static JWToken token;

    @BeforeAll
    static void init(){
        model = ModelUtils.createDefaultModel();
        tokenCache = new TokenCacheImpl(model);
        clientService = new ClientServiceImpl(model, tokenCache);
        resourceServerService = new ResourceServerServiceImpl(model, tokenCache);
    }
    @Test
    @Order(1)
    void authenticateTest() {
        final UPAuthenticationRequest authenticationRequest = new UPAuthenticationRequest(ClientId.from("iam-admin-id"), "iam-secret-77");
        final Optional<JWToken> tokenOptional = clientService.authenticate(authenticationRequest);
        assertTrue(tokenOptional.isPresent());
        token = tokenOptional.get();
    }
    @Test
    @Order(2)
    void verifyValidTokenTest() {
        final boolean result = resourceServerService.verify(token);
        assertTrue(result);
    }

    @Test
    @Order(3)
    void renewTokenTest() {
        final Optional<JWToken> tokenOptional = clientService.renew(token);
        final boolean result = resourceServerService.verify(token);
        assertFalse(result);
        assertTrue(tokenOptional.isPresent());
        assertNotEquals(token, tokenOptional.get());
        token = tokenOptional.get();
    }

    @Test
    @Order(4)
    void logoutTest() {
        final boolean result = clientService.logout(token);
        assertTrue(result);
    }

    @Test
    @Order(5)
    void verifyInvalidTokenTest() {
        final boolean result = resourceServerService.verify(token);
        assertFalse(result);
    }

    @Test
    @Order(6)
    void verifyInvalidTokenRenewTest() {
        final Optional<JWToken> tokenOptional = clientService.renew(token);
        assertTrue(tokenOptional.isEmpty());
    }
}
