package com.andre.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public class ModelImpl implements Model {
  private final Map<OrganizationId, Organization> organizations;

  public ModelImpl() {
    this.organizations = new ConcurrentHashMap<>();
  }

  @Override
  public void add(Organization organization) {
    organizations.put(organization.getId(), organization);
  }

  @Override
  public Collection<Organization> getOrganizations() {
    return new ArrayList<>(organizations.values());
  }

  @Override
  public Optional<Organization> getOrganization(OrganizationId organizationId) {
    return Optional.ofNullable(organizations.get(organizationId));
  }

  @Override
  public boolean remove(OrganizationId organizationId) {
    return organizations.remove(organizationId) != null;
  }

  @Override
  public Optional<Client> getClient(final OrganizationId organizationId, final ProjectId projectId, final ClientId clientId) {
    final Organization organization = organizations.get(organizationId);
    if (organization != null) {
      final Optional<Project> project = organization.getProject(projectId);
      if (project.isPresent()) {
        return project.get().getClient(clientId);
      }
    }
    return Optional.empty();
  }

}
