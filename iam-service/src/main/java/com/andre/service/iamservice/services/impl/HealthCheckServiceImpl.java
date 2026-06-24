package com.andre.service.iamservice.services.impl;


import com.andre.service.iamservice.services.HealthCheckService;
import com.andre.service.iamservice.dto.HealthCheckResponse;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckServiceImpl implements HealthCheckService {

    @Override
    public HealthCheckResponse getStatus() {
        return new HealthCheckResponse("OK", "1.0.0", System.currentTimeMillis());
    }
}
