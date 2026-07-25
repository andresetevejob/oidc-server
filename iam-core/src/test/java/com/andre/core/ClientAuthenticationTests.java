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
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClientAuthenticationTests {

    private static final String adminPassword = "top-secret";

    private static Model model;
    private static ClientService clientService;
    private static ResourceServerService resourceServerService;
    private static TokenCache tokenCache;
    private static JWToken token;

    @BeforeAll
    static void init() {
        model = ModelUtils.createDefaultModel(adminPassword);
        tokenCache = new TokenCacheImpl(model);
        clientService = new ClientServiceImpl(model, tokenCache);
        resourceServerService = new ResourceServerServiceImpl(model, tokenCache);
    }

    @Test
    @Order(1)
    void authenticateTest() {
        UPAuthenticationRequest authenticationRequest = new UPAuthenticationRequest(ModelUtils.IAM_ADMIN_CLIENT, adminPassword);
        Optional<JWToken> tokenOptional = clientService.authenticate(ModelUtils.IAM_ADMINS_ORG, ModelUtils.IAM_ADMINS_PROJECT, authenticationRequest);
        assertTrue(tokenOptional.isPresent());
        token = tokenOptional.get();
    }

    @Test
    @Order(2)
    void verifyValidTokenTest() {
        boolean result = resourceServerService.verify(ModelUtils.IAM_ADMINS_ORG, ModelUtils.IAM_ADMINS_PROJECT, token);
        assertTrue(result);
    }

    @Test
    @Order(3)
    void renewTokenTest() {
        Optional<JWToken> tokenOptional = clientService.renew(ModelUtils.IAM_ADMINS_ORG, ModelUtils.IAM_ADMINS_PROJECT, token);
        boolean result = resourceServerService.verify(ModelUtils.IAM_ADMINS_ORG, ModelUtils.IAM_ADMINS_PROJECT, token);
        assertFalse(result);
        assertTrue(tokenOptional.isPresent());
        assertFalse(token.equals(tokenOptional.get()));
        token = tokenOptional.get();
    }

    @Test
    @Order(4)
    void verifyValidRenewedTokenTest() {
        boolean result = resourceServerService.verify(ModelUtils.IAM_ADMINS_ORG, ModelUtils.IAM_ADMINS_PROJECT, token);
        assertTrue(result);
    }

    @Test
    @Order(5)
    void logoutTest() {
        boolean result = clientService.logout(ModelUtils.IAM_ADMINS_ORG, ModelUtils.IAM_ADMINS_PROJECT, token);
        assertTrue(result);
    }

    @Test
    @Order(6)
    void verifyInvalidTokenTest() {
        boolean result = resourceServerService.verify(ModelUtils.IAM_ADMINS_ORG, ModelUtils.IAM_ADMINS_PROJECT, token);
        assertFalse(result);
    }

    @Test
    @Order(7)
    void verifyInvalidTokenRenewTest() {
        Optional<JWToken> tokenOptional = clientService.renew(ModelUtils.IAM_ADMINS_ORG, ModelUtils.IAM_ADMINS_PROJECT, token);
        assertTrue(tokenOptional.isEmpty());
    }

}