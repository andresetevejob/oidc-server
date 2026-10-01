package com.andre.core.services.impl.admin;

import com.andre.core.model.Model;
import com.andre.core.model.Organization;
import com.andre.core.model.OrganizationId;
import com.andre.core.model.PKIException;
import com.andre.core.services.admin.OrganizationManagerService;

import java.util.Collection;
import java.util.Optional;

public class OrganizationManagerServiceImpl implements OrganizationManagerService {

    private final Model model;

    public OrganizationManagerServiceImpl(Model model) {
        this.model = model;
    }

    @Override
    public boolean create(OrganizationId id, String name) throws PKIException {
        if (model.getOrganization(id).isPresent()) {
            return false;
        } else {
            model.add(new Organization(id, name));
            return true;
        }
    }

    @Override
    public Collection<Organization> getAll() {
        return model.getOrganizations();
    }

    @Override
    public Optional<Organization> get(OrganizationId id) {
        return model.getOrganization(id);
    }

    @Override
    public boolean remove(OrganizationId id) {
        return model.remove(id);
    }

}
