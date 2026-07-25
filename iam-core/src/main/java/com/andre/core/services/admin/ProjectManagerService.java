package com.andre.core.services.admin;

import com.andre.core.model.OrganizationId;
import com.andre.core.model.Project;
import com.andre.core.model.ProjectId;

import java.util.Collection;
import java.util.Optional;

public interface ProjectManagerService {
    boolean create(OrganizationId id, ProjectId projectId, String name);
    Collection<Project> getAll(OrganizationId id);
    Optional<Project> get(OrganizationId id, ProjectId projectId);
    boolean remove(OrganizationId id, ProjectId projectId);
}
