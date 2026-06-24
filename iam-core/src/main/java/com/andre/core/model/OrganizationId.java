package com.andre.core.model;

public final class OrganizationId extends Id {
    public OrganizationId(final String id) {
        super(id);
    }
    public static OrganizationId from(final String id) {
        return new OrganizationId(id);
    }
}
