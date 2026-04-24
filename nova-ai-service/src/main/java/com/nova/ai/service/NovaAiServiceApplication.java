package com.nova.ai.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.nova.ai")
@EnableAsync
public class NovaAiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovaAiServiceApplication.class, args);
    }
}
