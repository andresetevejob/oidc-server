package com.andre.core.model;

import com.andre.core.services.dto.JWToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.impl.DefaultClaims;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TokenCacheImpl implements TokenCache {

    private final Model model;
    private Set<JWToken> revokedTokens;

    public TokenCacheImpl(Model model) {
        this.model = model;
        this.revokedTokens = new HashSet<>();
    }

    @Override
    public void addRevokedToken(JWToken jwToken) {
        this.revokedTokens.add(jwToken);
    }

    @Override
    public int purgeRevokedTokens() {
        int size = this.revokedTokens.size();
        this.revokedTokens = this.revokedTokens.stream()
            .filter(this::validateToken)
            .collect(Collectors.toSet());
        return size - this.revokedTokens.size();
    }

    @Override
    public boolean isRevoked(JWToken jwToken) {
        return this.revokedTokens.contains(jwToken);
    }

    @Override
    public int size() {
        return this.revokedTokens.size();
    }

    private boolean validateToken(JWToken jwToken) {
        final DefaultClaims defaultClaims = TokenUtils.extractClaims(jwToken);
        final OrganizationId organizationId = OrganizationId.from(defaultClaims.getIssuer());
        final ProjectId projectId = ProjectId.from(defaultClaims.getAudience());
        final ClientId clientId = ClientId.from(defaultClaims.getSubject());
        final Optional<Client> client = this.model.getClient(organizationId, projectId, clientId);
        if (client.isPresent()) {
            final Optional<Jws<Claims>> verify = TokenUtils.verify(jwToken, client.get().getCertificate().getPublicKey());
            return verify.isPresent();
        }
        return false;
    }
}
