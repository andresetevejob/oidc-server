package com.andre.core.services.admin;

import com.andre.core.model.Client;
import com.andre.core.model.ClientId;
import com.andre.core.model.OrganizationId;
import com.andre.core.model.ProjectId;

import java.util.Collection;
import java.util.Optional;

public interface ClientManagerService {
    boolean create(OrganizationId id, ProjectId projectId, ClientId clientId, String name);
    Collection<Client> getAll(OrganizationId id, ClientId clientId);
    Optional<Client> get(OrganizationId id, ProjectId projectId, ClientId clientId);
    boolean remove(OrganizationId id, ProjectId projectId, ClientId clientId);
}
