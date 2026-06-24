package com.andre.service.iamservice.services;
import com.andre.core.services.dto.JWToken;

public interface ResourceServerService {
    boolean verify(JWToken jwToken);
}
