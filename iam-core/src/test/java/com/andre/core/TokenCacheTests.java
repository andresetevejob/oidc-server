package com.andre.core;

import com.andre.core.model.TokenCache;
import com.andre.core.model.TokenCacheImpl;
import org.junit.jupiter.api.BeforeAll;

public class TokenCacheTests {

    private static TokenCache tokenCache;

    @BeforeAll
    public static void init() {
        tokenCache = new TokenCacheImpl();
    }
}
