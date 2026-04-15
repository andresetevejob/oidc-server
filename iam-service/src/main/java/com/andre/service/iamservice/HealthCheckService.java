package com.andre.service.iamservice;


import com.andre.service.iamservice.dto.HealthCheckResponse;

public interface HealthCheckService {
    HealthCheckResponse getStatus();
}
