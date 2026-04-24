package com.nova.ai.workflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nova.ai")
public class NovaAiWorkflowApplication {
    public static void main(String[] args) {
        SpringApplication.run(NovaAiWorkflowApplication.class, args);
    }
}
