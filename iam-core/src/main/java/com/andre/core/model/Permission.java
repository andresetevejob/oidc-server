package com.andre.core.model;

public class Permission {
    private final String id;
    private final String service;
    private final String resource;
    private final String action;

    public Permission(String id, String service, String resource, String action) {
        this.id = id;
        this.service = service;
        this.resource = resource;
        this.action = action;
    }

    public String getId() {
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
