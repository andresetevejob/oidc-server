package com.andre.core.model;

public final class ProjectId extends Id {
    public ProjectId(final String id) {
        super(id);
    }
    public static ProjectId from(final String id) {
        return new ProjectId(id);
    }
}
