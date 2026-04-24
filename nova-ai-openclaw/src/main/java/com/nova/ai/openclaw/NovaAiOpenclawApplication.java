package com.nova.ai.openclaw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nova.ai")
public class NovaAiOpenclawApplication {
    public static void main(String[] args) {
        SpringApplication.run(NovaAiOpenclawApplication.class, args);
    }
}
