package com.andre.core.services.impl;

import com.andre.core.model.*;
import com.andre.core.services.ResourceServerService;
import com.andre.core.services.dto.ClientInfo;
import com.andre.core.services.dto.JWToken;
import com.andre.core.services.dto.ProjectInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.impl.DefaultClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

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
                    client.get().getCertificate().getPublicKey());
                LOG.info("JWT verified={}", claimsJws.isPresent());
                return claimsJws.isPresent();
            }else {
                LOG.info("JWT subject {} not found", clientId);
            }
        }
        return false;
    }
    @Override
    public Optional<ProjectInfo> getProjectInfo(OrganizationId organizationId, ProjectId projectId) {
        Optional<Organization> organizationOptional = model.getOrganization(organizationId);
        if (organizationOptional.isPresent()) {
            Optional<Project> projectOptional = organizationOptional.get().getProject(projectId);
            if (projectOptional.isPresent()) {
                Project project = projectOptional.get();
                ProjectInfo projectInfo = new ProjectInfo(project.getId(), project.getOrganizationId(),
                        project.getName(), organizationOptional.get().getCertificate(), project.getCertificate());
                return Optional.of(projectInfo);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<ClientInfo> getClientInfo(OrganizationId organizationId, ProjectId projectId, ClientId clientId) {
        Optional<Organization> organizationOptional = model.getOrganization(organizationId);
        if (organizationOptional.isPresent()) {
            Optional<Project> projectOptional = organizationOptional.get().getProject(projectId);
            if (projectOptional.isPresent()) {
                Optional<Client> clientOptional = projectOptional.get().getClient(clientId);
                if (clientOptional.isPresent()) {
                    ClientInfo clientInfo = new ClientInfo(clientId, projectId, organizationId,
                            clientOptional.get().getName(), clientOptional.get().getCertificate());
                    return Optional.of(clientInfo);
                }
            }
        }
        return Optional.empty();
    }
}
