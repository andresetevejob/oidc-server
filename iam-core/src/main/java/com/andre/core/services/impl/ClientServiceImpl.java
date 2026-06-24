package com.andre.core.services.impl;

import com.andre.core.model.AuthenticationRequest;
import com.andre.core.model.Client;
import com.andre.core.model.ClientId;
import com.andre.core.model.Credentials;
import com.andre.core.model.Model;
import com.andre.core.model.Project;
import com.andre.core.model.TokenCache;
import com.andre.core.model.TokenUtils;
import com.andre.core.services.ClientService;
import com.andre.core.services.dto.JWToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.impl.DefaultClaims;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientServiceImpl implements ClientService {

    private final Model model;
    private final TokenCache tokenCache;
    private static final Logger LOG = LoggerFactory.getLogger(ClientServiceImpl.class);


    public ClientServiceImpl(Model model, TokenCache tokenCache) {
        this.model = model;
        this.tokenCache = tokenCache;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<JWToken> authenticate(AuthenticationRequest authenticationRequest) {
        Optional<Client> clientOptional = model.getClient(authenticationRequest.clientId());
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            Optional<Credentials> credentials = client.getCredentials(authenticationRequest.getCredentialsType().getClass());
            if (credentials.isPresent()) {
                boolean valid = credentials.get().verify(authenticationRequest);
                if (valid) {
                    String subject = client.getId().getId();
                    String issuer = client.getProjectId().getId();
                    Optional<Project> projectOptional = model.getProject(client.getProjectId());
                    String projectName = projectOptional.isPresent() ? projectOptional.get().getName() : "";
                    JWToken token = TokenUtils.issueToken(subject, issuer,
                        client.getDefaultTokenDuration(), TimeUnit.MILLISECONDS, projectName,
                        client.getRoles(), client.getKeyPair());
                    return Optional.of(token);
                }
            }
        } else {
            LOG.info("JWT subject {} not found", authenticationRequest.clientId());
        }
        return Optional.empty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<JWToken> renew(JWToken token) {
        if (!tokenCache.isRevoked(token)) {
            DefaultClaims defaultClaims = TokenUtils.extractClaims(token);
            String subject = defaultClaims.getSubject();
            Optional<Client> client = model.getClient(ClientId.from(subject));
            if (client.isPresent()) {
                Optional<Jws<Claims>> claimsOptional = TokenUtils.verify(token, client.get().getKeyPair());
                LOG.info("JWT verified={}", claimsOptional.isPresent());
                if (claimsOptional.isPresent()) {
                    Claims claims = claimsOptional.get().getBody();
                    List<String> roles = (List<String>) claims.get(TokenUtils.ROLES_CLAIM);
                    JWToken renewedToken = TokenUtils.issueToken(claims.getSubject(), claims.getIssuer(),
                        client.get().getDefaultTokenDuration(), TimeUnit.MILLISECONDS, claims.getAudience(),
                        Set.copyOf(roles), client.get().getKeyPair());
                    tokenCache.addRevokedToken(token);
                    return Optional.of(renewedToken);
                }
            } else {
                LOG.info("JWT subject {} not found", subject);
            }
        } else {
            LOG.info("JWT is revoked {}", token);
        }
        return Optional.empty();
    }


    @Override
    public boolean logout(JWToken token) {
        DefaultClaims defaultClaims = TokenUtils.extractClaims(token);
        String subject = defaultClaims.getSubject();
        Optional<Client> client = model.getClient(ClientId.from(subject));
        if (client.isPresent()) {
            Optional<Jws<Claims>> claims = TokenUtils.verify(token, client.get().getKeyPair());
            LOG.info("JWT verified={}", claims.isPresent());
            if (claims.isPresent()) {
                tokenCache.addRevokedToken(token);
                return true;
            }
        }else{
            LOG.info("JWT subject {} not found", subject);
        }
        return false;
    }
}