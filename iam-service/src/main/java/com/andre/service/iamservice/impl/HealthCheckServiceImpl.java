package com.andre.service.iamservice.impl;


import com.andre.service.iamservice.HealthCheckService;
import com.andre.service.iamservice.dto.HealthCheckResponse;

public class HealthCheckServiceImpl implements HealthCheckService {

    @Override
    public HealthCheckResponse getStatus() {
        return new HealthCheckResponse("OK", "1.0.0", System.currentTimeMillis());
    }
}
