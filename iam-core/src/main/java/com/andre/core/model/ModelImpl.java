package com.andre.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ModelImpl implements Model {
  private final Map<OrganizationId, Organization> organizations;
  private final Map<ProjectId, Project> projects;
  private final Map<ClientId, Client> clients;

  public ModelImpl() {
    this.organizations = new ConcurrentHashMap<>();
    this.projects = new ConcurrentHashMap<>();
    this.clients = new ConcurrentHashMap<>();
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
  public void remove(OrganizationId organizationId) {
    organizations.remove(organizationId);
  }

  @Override
  public Optional<Client> getClient(ClientId clientId) {
    return Optional.ofNullable(clients.get(clientId));
  }

  Map<ProjectId, Project> getProjects() {
    return projects;
  }

  Map<ClientId, Client> getClients() {
    return clients;
  }
}
