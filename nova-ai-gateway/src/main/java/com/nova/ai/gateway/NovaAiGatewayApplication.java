package com.nova.ai.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nova.ai")
public class NovaAiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(NovaAiGatewayApplication.class, args);
    }
}
