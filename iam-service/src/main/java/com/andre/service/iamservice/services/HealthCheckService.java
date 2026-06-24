package com.andre.service.iamservice.services;


import com.andre.service.iamservice.dto.HealthCheckResponse;

public interface HealthCheckService {
    HealthCheckResponse getStatus();
}
