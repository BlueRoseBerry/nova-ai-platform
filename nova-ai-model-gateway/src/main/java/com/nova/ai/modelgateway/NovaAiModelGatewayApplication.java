package com.nova.ai.modelgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nova.ai")
public class NovaAiModelGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(NovaAiModelGatewayApplication.class, args);
    }
}
