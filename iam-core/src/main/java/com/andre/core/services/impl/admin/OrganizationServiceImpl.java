package com.andre.core.services.impl.admin;

import com.andre.core.model.ModelImpl;
import com.andre.core.model.ModelUtils;
import com.andre.core.model.Organization;
import com.andre.core.model.OrganizationId;
import com.andre.core.services.admin.OrganizationService;
import com.andre.core.services.dto.CreateOrganizationRequest;
import java.util.Collection;

public class OrganizationServiceImpl implements OrganizationService {

    private final ModelImpl model;

    public OrganizationServiceImpl(ModelImpl model) {
        this.model = model;
    }

    @Override
    public OrganizationId createOrganization(CreateOrganizationRequest createOrganizationRequest) {
        OrganizationId organizationId = ModelUtils.createOrganizationId();
        model.add(new Organization(organizationId, createOrganizationRequest.name(), model));
        return organizationId;
    }

    @Override
    public Collection<Organization> getAll() {
        return model.getOrganizations();
    }

    @Override
    public void remove(OrganizationId organizationId) {
        model.remove(organizationId);
    }

}
