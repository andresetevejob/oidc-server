package com.andre.service.iamservice.controller;

import com.andre.service.iamservice.dto.HealthCheckResponse;
import com.andre.service.iamservice.services.HealthCheckService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/services/health")
public class HealthCheckController {
    private final HealthCheckService healthCheckService;

    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }
    @GetMapping(path = "/status", produces = "application/json")
    public HealthCheckResponse getStatus(){
        return healthCheckService.getStatus();
    }
}
