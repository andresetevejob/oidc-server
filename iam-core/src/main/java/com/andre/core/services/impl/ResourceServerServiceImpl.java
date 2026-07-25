package com.andre.core.services.impl;

import com.andre.core.model.Client;
import com.andre.core.model.ClientId;
import com.andre.core.model.Model;
import com.andre.core.model.OrganizationId;
import com.andre.core.model.ProjectId;
import com.andre.core.model.TokenCache;
import com.andre.core.model.TokenUtils;
import com.andre.core.services.ResourceServerService;
import com.andre.core.services.dto.JWToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.impl.DefaultClaims;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceServerServiceImpl implements ResourceServerService {

    private final Model model;
    private final TokenCache tokenCache;
    private static final Logger LOG = LoggerFactory.getLogger(ResourceServerServiceImpl.class);

    public ResourceServerServiceImpl(Model model, TokenCache tokenCache) {
        this.model = model;
        this.tokenCache = tokenCache;
    }

    @Override
    public boolean verify(final OrganizationId organizationId, final ProjectId projectId,final JWToken token) {
        final boolean isRevoked = this.tokenCache.isRevoked(token);
        if (!isRevoked) {
            final DefaultClaims defaultClaims = TokenUtils.extractClaims(token);
            final ClientId clientId = ClientId.from(defaultClaims.getSubject());
            final Optional<Client> client = this.model.getClient(organizationId, projectId, clientId);
            if (client.isPresent()) {
                final Optional<Jws<Claims>> claimsJws = TokenUtils.verify(token,
                    client.get().getKeyPair());
                LOG.info("JWT verified={}", claimsJws.isPresent());
                return claimsJws.isPresent();
            }else {
                LOG.info("JWT subject {} not found", clientId);
            }
        }
        return false;
    }

}
