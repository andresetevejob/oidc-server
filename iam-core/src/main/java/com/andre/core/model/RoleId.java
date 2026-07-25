package com.andre.core.model;

public final class RoleId extends Id {

    public RoleId(String id) {
        super(id);
    }

    public static RoleId from(String id) {
        return new RoleId(id);
    }

    public static final class PermissionId extends Id {
        public PermissionId(String id) {
            super(id);
        }
        public static PermissionId from(String id) {
            return new PermissionId(id);
        }
    }
}
