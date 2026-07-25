package com.andre.core;

import com.andre.core.model.Model;
import com.andre.core.model.ModelImpl;
import com.andre.core.model.Organization;
import com.andre.core.model.OrganizationId;
import com.andre.core.services.admin.OrganizationManagerService;
import com.andre.core.services.impl.admin.OrganizationManagerServiceImpl;
import org.junit.jupiter.api.*;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrganizationManagerServiceTests {
    private static OrganizationManagerService organizationManagerService;
    private static final OrganizationId oid001 = OrganizationId.from("organization-001");
    private static final OrganizationId oid002 = OrganizationId.from("organization-002");

    @BeforeAll
    public static void init() throws NoSuchAlgorithmException {
        Model model = new ModelImpl();
        organizationManagerService = new OrganizationManagerServiceImpl(model);
    }

    @Test
    @Order(1)
    public void checkEmptyModelTest() {
        Collection<Organization> all = organizationManagerService.getAll();
        assertTrue(all.isEmpty());
    }

    @Test
    @Order(2)
    public void createFirstOrganizationTest() {
        boolean result = organizationManagerService.create(oid001, "org-001");
        assertTrue(result);
        Collection<Organization> all = organizationManagerService.getAll();
        assertEquals(1, all.size());
        Optional<Organization> organization = organizationManagerService.get(oid001);
        assertTrue(organization.isPresent());
        organization = organizationManagerService.get(oid002);
        assertTrue(organization.isEmpty());
    }

    @Test
    @Order(2)
    public void createSecondOrganizationTest() {
        boolean result = organizationManagerService.create(oid002, "org-002");
        assertTrue(result);
        Collection<Organization> all = organizationManagerService.getAll();
        assertEquals(2, all.size());
        Optional<Organization> organization = organizationManagerService.get(oid001);
        assertTrue(organization.isPresent());
        organization = organizationManagerService.get(oid002);
        assertTrue(organization.isPresent());
    }

    @Test
    @Order(3)
    public void createExistingOrganizationTest() {
        boolean result = organizationManagerService.create(oid001, "org-001");
        assertFalse(result);
        result = organizationManagerService.create(oid002, "org-002");
        assertFalse(result);
        Collection<Organization> all = organizationManagerService.getAll();
        assertEquals(2, all.size());
        Optional<Organization> organization = organizationManagerService.get(oid001);
        assertTrue(organization.isPresent());
        organization = organizationManagerService.get(oid002);
        assertTrue(organization.isPresent());
    }

    @Test
    @Order(4)
    public void removeFirstOrganizationTest() {
        boolean removed = organizationManagerService.remove(oid001);
        assertTrue(removed);
        Collection<Organization> all = organizationManagerService.getAll();
        assertEquals(1, all.size());
        Optional<Organization> organization = organizationManagerService.get(oid001);
        assertTrue(organization.isEmpty());
        organization = organizationManagerService.get(oid002);
        assertTrue(organization.isPresent());
    }

    @Test
    @Order(5)
    public void removeSecondOrganizationTest() {
        boolean removed = organizationManagerService.remove(oid002);
        assertTrue(removed);
        Collection<Organization> all = organizationManagerService.getAll();
        assertEquals(0, all.size());
        Optional<Organization> organization = organizationManagerService.get(oid001);
        assertTrue(organization.isEmpty());
        organization = organizationManagerService.get(oid002);
        assertTrue(organization.isEmpty());
    }

    @Test
    @Order(6)
    public void removeNotExistingOrganizationTest() {
        boolean removed = organizationManagerService.remove(oid001);
        assertFalse(removed);
        removed = organizationManagerService.remove(oid002);
        assertFalse(removed);
    }

    @Test
    @Order(7)
    public void checkFinalEmptyModelTest() {
        Collection<Organization> all = organizationManagerService.getAll();
        assertEquals(0, all.size());
    }
}
