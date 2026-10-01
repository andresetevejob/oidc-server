package com.andre.core.services.admin;

import com.andre.core.model.*;

import java.util.Collection;
import java.util.Optional;

public interface ProjectManagerService {
    boolean create(OrganizationId id, ProjectId projectId, String name) throws PKIException;
    Collection<Project> getAll(OrganizationId id);
    Optional<Project> get(OrganizationId id, ProjectId projectId);
    boolean remove(OrganizationId id, ProjectId projectId);
    boolean addRole(OrganizationId id, ProjectId projectId, Role role);
    boolean removeRole(OrganizationId id, ProjectId projectId, RoleId roleId);
    Collection<Role> getRoles(OrganizationId id, ProjectId projectId);
}
