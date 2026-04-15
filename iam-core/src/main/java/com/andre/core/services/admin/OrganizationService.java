package com.andre.core.services.admin;

import com.andre.core.model.Organization;
import com.andre.core.model.OrganizationId;
import com.andre.core.services.dto.CreateOrganizationRequest;
import java.util.Collection;

public interface OrganizationService {
    OrganizationId createOrganization(CreateOrganizationRequest createOrganizationRequest);
    Collection<Organization> getAll();
    void remove(OrganizationId organizationId);
}
