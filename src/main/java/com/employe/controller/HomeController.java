package com.employe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> rootEndpoint() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("service", "Employee Management REST API");
        response.put("version", "1.0.0");

        Map<String, String> endpoints = new LinkedHashMap<>();
        endpoints.put("GET /api/employees", "Get all employees");
        endpoints.put("GET /api/employees/{id}", "Get an employee by ID");
        endpoints.put("POST /api/employees", "Create a new employee");
        endpoints.put("PUT /api/employees/{id}", "Update an employee by ID");
        endpoints.put("DELETE /api/employees/{id}", "Delete an employee by ID");
        endpoints.put("GET /api/employees/department/{department}", "Filter employees by department");
        endpoints.put("GET /health", "Health status");

        response.put("endpoints", endpoints);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = new LinkedHashMap<>();
        health.put("status", "UP");
        health.put("message", "Service is healthy and operational");
        return ResponseEntity.ok(health);
    }
}
