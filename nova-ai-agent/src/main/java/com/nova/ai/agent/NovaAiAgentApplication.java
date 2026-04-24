package com.nova.ai.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nova.ai")
public class NovaAiAgentApplication {
    public static void main(String[] args) {
        SpringApplication.run(NovaAiAgentApplication.class, args);
    }
}
