package com.andre.core.model;

import java.security.KeyPair;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Client {
    private final ClientId id;
    private final ProjectId projectId;
    private final String name;
    private final Map<Class<? extends CredentialsType>, Credentials> credentials;
    private final Map<RoleId, Role> roles;
    private final KeyPair keyPair;
    private final Long defaultTokenDuration;

    public Client(ClientId id, String name, ProjectId projectId, KeyPair keyPair,
        Long defaultTokenDuration) {
        this.id = id;
        this.name = name;
        this.keyPair = keyPair;
        this.defaultTokenDuration = defaultTokenDuration;
        this.credentials = new ConcurrentHashMap<>();
        this.roles = new ConcurrentHashMap<>();
        this.projectId = projectId;
    }

    public ClientId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public void addRole(Role role) {
        this.roles.put(role.getId(), role);
    }

    public void addCredentials(final Credentials credentials) {
        this.credentials.put(credentials.getType().getClass(), credentials);
    }
    public Optional<Credentials> getCredentials(Class<? extends CredentialsType> type) {
        return Optional.ofNullable(credentials.get(type));
    }
    public KeyPair getKeyPair() {
        return keyPair;
    }

    public Long getDefaultTokenDuration() {
        return defaultTokenDuration;
    }
    public Set<String> getRoles(){
        return this.roles.values().stream().map(role -> role.getId().getId()).collect(Collectors.toSet());
    }
}
