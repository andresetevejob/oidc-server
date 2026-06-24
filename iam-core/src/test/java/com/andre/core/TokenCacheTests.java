package com.andre.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.andre.core.model.Model;
import com.andre.core.model.ModelUtils;
import com.andre.core.model.TokenCache;
import com.andre.core.model.TokenCacheImpl;
import com.andre.core.model.TokenUtils;
import com.andre.core.services.dto.JWToken;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TokenCacheTests {

    private static final String SUBJECT = "subject";
    private static final String ISSUER = "issuer";
    private static final String AUDIENCE = "audience";
    private static final Set<String> ROLES = Set.of("role-a", "role-b", "role-c");
    private static final Long DURATION = 3L;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private static KeyPair keyPair;
    private static Model model;
    private static TokenCache tokenCache;
    private static JWToken jwToken;

    @BeforeAll
    static void init() throws NoSuchAlgorithmException {
        keyPair = TokenUtils.generateKeyPairV2();
        model = ModelUtils.createDefaultModel();
        tokenCache = new TokenCacheImpl(model);
        jwToken = TokenUtils.issueToken(SUBJECT, ISSUER, DURATION, TIME_UNIT, AUDIENCE, ROLES, keyPair);
    }

    @Test
    @Order(1)
    void afterInitializationTest() {
        int size = tokenCache.size();
        assertEquals(0, size);
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

}
