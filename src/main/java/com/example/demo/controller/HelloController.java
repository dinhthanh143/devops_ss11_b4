package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/demo")
public class HelloController {

    @Value("${server.port:8081}")
    private String port;

    @Value("${spring.application.name:demo-service}")
    private String appName;

    @GetMapping("/hello")
    public Map<String, Object> sayHello() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", appName);
        response.put("port", port);
        response.put("message", "Xin chào từ Microservice đơn giản kết nối Eureka!");
        response.put("status", "UP");
        return response;
    }
}
