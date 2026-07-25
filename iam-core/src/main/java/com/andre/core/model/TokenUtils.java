package com.andre.core.model;

import com.andre.core.services.dto.JWToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TokenUtils {

    public static final String ROLES_CLAIM = "roles";
    public static final String NONCE = "nonce";

    // Empêche l'instanciation de la classe utilitaire
    private TokenUtils() {
    }

    /**
     * V1 - Version originale : utilise explicitement "NativePRNG" pour SecureRandom. Attention : ce
     * nom d'algorithme n'est pas garanti sur toutes les plateformes (p.ex. Windows) et peut lancer
     * NoSuchAlgorithmException. Gardée ici pour compatibilité / comparaison.
     */
    public static KeyPair generateKeyPairV1() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = SecureRandom.getInstance("NativePRNG");
        keyPairGenerator.initialize(2048, secureRandom);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * V2 - Version recommandée : plus robuste et portable. - Essaie
     * SecureRandom.getInstanceStrong() puis retombe sur new SecureRandom() si indisponible. - Ne
     * lance pas NoSuchAlgorithmException en sortie (encapsulée si RSA absent).
     */
    public static KeyPair generateKeyPairV2() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom secureRandom;
            try {
                secureRandom = SecureRandom.getInstanceStrong();
            } catch (NoSuchAlgorithmException e) {
                secureRandom = new SecureRandom();
            }
            keyPairGenerator.initialize(2048, secureRandom);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("RSA KeyPairGenerator not available", e);
        }
    }

    public static JWToken issueToken(OrganizationId organizationId, ProjectId projectId, ClientId clientId, Long duration, TimeUnit timeUnit, Set<String> roles, KeyPair keyPair) {
        Date issuedAt = new Date();
        Date notBefore = issuedAt;
        Date expirationTime = new Date(issuedAt.getTime() + timeUnit.toMillis(duration));
        return issueToken(clientId.getId(), organizationId.getId(), projectId.getId(), expirationTime, notBefore, issuedAt, roles, keyPair);
    }

    public static JWToken issueToken(String subject, String issuer, String audience, Date expirationTime, Date notBefore, Date issuedAt, Set<String> roles, KeyPair keyPair) {
        String jwToken = Jwts.builder()
                .setSubject(subject)
                .signWith(keyPair.getPrivate())
                .setExpiration(expirationTime)
                .setIssuer(issuer)
                .setIssuedAt(issuedAt)
                .setNotBefore(notBefore)
                .setAudience(audience)
                .claim(ROLES_CLAIM, roles)
                .setId(UUID.randomUUID().toString())
                .compact();
        return JWToken.from(jwToken);
    }

    public static Optional<Jws<Claims>> verify(JWToken jwToken, KeyPair keyPair) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(keyPair.getPrivate()).build().parseClaimsJws(jwToken.token());
            return Optional.of(claimsJws);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    public static DefaultClaims extractClaims(JWToken jwToken) {
        String token = jwToken.token();
        String jwTokenWithoutSignature = token.substring(0, token.lastIndexOf('.') + 1);
        Jwt jwt = Jwts.parserBuilder().build().parse(jwTokenWithoutSignature);
        return (DefaultClaims) jwt.getBody();
    }
}
