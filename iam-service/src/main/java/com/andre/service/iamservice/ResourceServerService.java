package com.andre.service.iamservice;

import openid.services.dto.JWToken;

public interface ResourceServerService {
    boolean verify(JWToken jwToken);
}
