package com.andre.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.andre.core.model.ClientId;
import com.andre.core.model.Model;
import com.andre.core.model.ModelUtils;
import com.andre.core.model.OrganizationId;
import com.andre.core.model.ProjectId;
import com.andre.core.model.TokenCache;
import com.andre.core.model.TokenCacheImpl;
import com.andre.core.model.TokenUtils;
import com.andre.core.services.dto.JWToken;
import java.security.KeyPair;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TokenCacheTests {

    private static final OrganizationId ORGANIZATION_ID = OrganizationId.from("unique-organization-id");
    private static final ProjectId PROJECT_ID = ProjectId.from("unique-project-id");
    private static final ClientId CLIENT_ID = ClientId.from("unique-client-id");
    private static final Set<String> ROLES = Set.of("role-a", "role-b", "role-c");
    private static final Long DURATION = 3L;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private static KeyPair keyPair;
    private static Model model;
    private static TokenCache tokenCache;
    private static JWToken jwToken;

    @BeforeAll
    static void init() {
        keyPair = TokenUtils.generateKeyPairV2();
        model = ModelUtils.createDefaultModel("top-secret");
        tokenCache = new TokenCacheImpl(model);
        jwToken = TokenUtils.issueToken(ORGANIZATION_ID, PROJECT_ID, CLIENT_ID, DURATION, TIME_UNIT, ROLES, keyPair);
    }

    @Test
    @Order(1)
    void afterInitializationTest() {
        int size = tokenCache.size();
        assertEquals(0, size);
        assertFalse(tokenCache.isRevoked(jwToken));
    }

    @Test
    @Order(2)
    void addRevokedTokenTest() {
        assertFalse(tokenCache.isRevoked(jwToken));
        tokenCache.addRevokedToken(jwToken);
        assertEquals(1, tokenCache.size());
        assertTrue(tokenCache.isRevoked(jwToken));
    }

    @Test
    @Order(3)
    void waitForTokensToExpireTest() throws InterruptedException {
        while(tokenCache.purgeRevokedTokens() > 0) {
            Thread.sleep(1000);
        }
        int size = tokenCache.size();
        assertEquals(0, size);
    }

    @Test
    @Order(4)
    void afterCachePurgeTest() {
        assertFalse(tokenCache.isRevoked(jwToken));
    }

}