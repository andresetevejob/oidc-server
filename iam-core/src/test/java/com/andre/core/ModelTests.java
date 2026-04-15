package com.andre.core;

import com.andre.core.model.Model;
import com.andre.core.model.ModelUtils;
import com.andre.core.model.TokenCache;
import com.andre.core.model.TokenCacheImpl;
import com.andre.core.services.ClientService;
import com.andre.core.services.ResourceServerService;
import com.andre.core.services.impl.ClientServiceImpl;
import com.andre.core.services.impl.ResourceServerServiceImpl;
import org.junit.jupiter.api.BeforeAll;

public class ModelTests {
    private static Model model;
    private static ClientService clientService;
    private static ResourceServerService resourceServerService;
    private static TokenCache tokenCache;

    @BeforeAll
    public static void init() {
        model = ModelUtils.createDefaultModel();
        tokenCache = new TokenCacheImpl();
        clientService = new ClientServiceImpl(model, tokenCache);
        resourceServerService = new ResourceServerServiceImpl(model, tokenCache);
    }
}
