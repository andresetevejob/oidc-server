package com.andre.core.model;

public class Permission {
    private final PermissionId id;
    private final String service;
    private final String resource;
    private final String action;

    public Permission(String id, String service, String resource, String action) {
        this.id = PermissionId.from(service + "." + resource + "." + action);
        this.service = service;
        this.resource = resource;
        this.action = action;
    }

    public PermissionId getId() {
        return id;
    }

    public String getService() {
        return service;
    }

    public String getAction() {
        return action;
    }

    public String getResource() {
        return resource;
    }
}
