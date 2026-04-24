package com.nova.ai.rag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nova.ai")
public class NovaAiRagApplication {
    public static void main(String[] args) {
        SpringApplication.run(NovaAiRagApplication.class, args);
    }
}
