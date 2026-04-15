package com.andre.core.services.impl;

import com.andre.core.model.Model;
import com.andre.core.model.TokenCache;
import com.andre.core.services.ResourceServerService;
import com.andre.core.services.dto.JWToken;

public class ResourceServerServiceImpl implements ResourceServerService {

    private final Model model;
    private final TokenCache tokenCache;

    public ResourceServerServiceImpl(Model model, TokenCache tokenCache) {
        this.model = model;
        this.tokenCache = tokenCache;
    }

    @Override
    public boolean verify(JWToken token) {
        return false;
    }

}
