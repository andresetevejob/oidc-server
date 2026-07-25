package com.andre.core;

import com.andre.core.model.*;
import com.andre.core.services.admin.OrganizationManagerService;
import com.andre.core.services.admin.ProjectManagerService;
import com.andre.core.services.impl.admin.OrganizationManagerServiceImpl;
import com.andre.core.services.impl.admin.ProjectManagerServiceImpl;
import org.junit.jupiter.api.*;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProjectManagerServiceTests {

    private static Model model;
    private static OrganizationManagerService organizationManagerService;
    private static ProjectManagerService projectManagerService;
    private static OrganizationId oid001 = OrganizationId.from("organization-001");
    private static OrganizationId oid002 = OrganizationId.from("organization-002");
    private static ProjectId pId001 = ProjectId.from("projectid-001");

    @BeforeAll
    static void init() throws NoSuchAlgorithmException {
        model = new ModelImpl();
        organizationManagerService = new OrganizationManagerServiceImpl(model);
        projectManagerService = new ProjectManagerServiceImpl(model);
    }

    @Test
    @Order(1)
    void checkEmptyModelTest() {
        final Collection<Organization> all = organizationManagerService.getAll();
        assertTrue(all.isEmpty());
        Optional<Project> projectOptional = projectManagerService.get(oid001, pId001);
        assertTrue(projectOptional.isEmpty());
        projectOptional = projectManagerService.get(oid002, pId001);
        assertTrue(projectOptional.isEmpty());
    }

    @Test
    @Order(2)
    void createOrganizationsTest() {
        boolean result = organizationManagerService.create(oid001, "org-001");
        assertTrue(result);
        result = organizationManagerService.create(oid002, "org-002");
        assertTrue(result);
        Collection<Organization> all = organizationManagerService.getAll();
        assertTrue(all.size() == 2);
    }

    @Test
    @Order(3)
    void createFirstProjectTest() {
        boolean result = projectManagerService.create(oid001, pId001, "p001");
        assertTrue(result);
        Collection<Project> all = projectManagerService.getAll(oid001);
        assertTrue(all.size() == 1);
        all = projectManagerService.getAll(oid002);
        assertTrue(all.size() == 0);
    }

    @Test
    @Order(4)
    void createSecondProjectTest() {
        boolean result = projectManagerService.create(oid002, pId001, "p001");
        assertTrue(result);
        Collection<Project> all = projectManagerService.getAll(oid001);
        assertTrue(all.size() == 1);
        all = projectManagerService.getAll(oid002);
        assertTrue(all.size() == 1);
    }

}