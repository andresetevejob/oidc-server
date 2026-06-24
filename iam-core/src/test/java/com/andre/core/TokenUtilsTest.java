package com.andre.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.andre.core.services.dto.JWToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.impl.DefaultClaims;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import com.andre.core.model.TokenUtils;

class TokenUtilsTest {

    private static final String SUBJECT = "subject";
    private static final String ISSUER = "issuer";
    private static final String AUDIENCE = "audience";
    private static final Set<String> ROLES = Set.of("role-a", "role-b", "role-c");

    @Test
    void keyPairGenerateTest() {
        KeyPair keyPair = TokenUtils.generateKeyPairV2();
        assertNotNull(keyPair);
    }

    @Test
    @SuppressWarnings("unchecked")
    void jwTokenValidityTest() {
        final Long duration = 60L;
        final TimeUnit timeUnit = TimeUnit.SECONDS;
        final KeyPair keyPair = TokenUtils.generateKeyPairV2();
        assertNotNull(keyPair);
        final JWToken jwt = TokenUtils.issueToken(SUBJECT, ISSUER, duration, timeUnit, AUDIENCE,
            ROLES, keyPair);
        assertNotNull(jwt);
        Optional<Jws<Claims>> claimsJws = TokenUtils.verify(jwt, keyPair);
        assertTrue(claimsJws.isPresent());
        assertEquals(SUBJECT, claimsJws.get().getBody().getSubject());
        assertEquals(ISSUER, claimsJws.get().getBody().getIssuer());
        assertEquals(AUDIENCE, claimsJws.get().getBody().getAudience());
        List<String> claimedRoles = (List<String>) claimsJws.get().getBody()
            .get(TokenUtils.ROLES_CLAIM);
        assertNotNull(claimedRoles);
        assertEquals(ROLES.size(), claimedRoles.size());
        for (String role : claimedRoles) {
            assertTrue(ROLES.contains(role));
        }
    }
    @Test
    void jwTokenExpiredTest() throws InterruptedException {
        Long duration = 1L;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        KeyPair keyPair = TokenUtils.generateKeyPairV2();
        JWToken jwt = TokenUtils.issueToken(SUBJECT, ISSUER, duration, timeUnit, AUDIENCE, ROLES, keyPair);
        Thread.sleep(3*1000L);
        Optional<Jws<Claims>> claimsJws = TokenUtils.verify(jwt, keyPair);
        assertTrue(claimsJws.isEmpty());
    }

    @Test
    void jwTokenInvalidKeyTest() {
        Long duration = 60L;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        KeyPair keyPair = TokenUtils.generateKeyPairV2();
        JWToken jwt = TokenUtils.issueToken(SUBJECT, ISSUER, duration, timeUnit, AUDIENCE, ROLES, keyPair);
        keyPair = TokenUtils.generateKeyPairV2();
        Optional<Jws<Claims>> claimsJws = TokenUtils.verify(jwt, keyPair);
        assertTrue(claimsJws.isEmpty());
    }

    @Test
    void extractTokenTest() {
        Long duration = 60L;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        KeyPair keyPair = TokenUtils.generateKeyPairV2();
        JWToken jwt = TokenUtils.issueToken(SUBJECT, ISSUER, duration, timeUnit, AUDIENCE, ROLES, keyPair);
        DefaultClaims defaultClaims = TokenUtils.extractClaims(jwt);
        assertEquals(SUBJECT, defaultClaims.getSubject());
        assertEquals(ISSUER, defaultClaims.getIssuer());
        assertEquals(AUDIENCE, defaultClaims.getAudience());
    }
    @Test
    void identicalTokenGenerationTest() {
        KeyPair keyPair = TokenUtils.generateKeyPairV2();
        final Date issuedAt = new Date();
        final Date notBefore = issuedAt;
        final Date expirationTime = new Date(issuedAt.getTime() + 3600*1000L);
        final JWToken jwt1 = TokenUtils.issueToken(SUBJECT, ISSUER, AUDIENCE, expirationTime, notBefore, issuedAt, ROLES, keyPair);
        final JWToken jwt2 = TokenUtils.issueToken(SUBJECT, ISSUER, AUDIENCE, expirationTime, notBefore, issuedAt, ROLES, keyPair);
        assertNotEquals(jwt1, jwt2);
    }
}
