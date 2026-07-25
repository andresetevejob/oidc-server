package com.andre.core.services.admin;

import com.andre.core.model.Organization;
import com.andre.core.model.OrganizationId;
import java.util.Collection;
import java.util.Optional;

public interface OrganizationManagerService {
    boolean create(OrganizationId id, String name);
    Collection<Organization> getAll();
    Optional<Organization> get(OrganizationId id);
    boolean remove(OrganizationId id);
}
