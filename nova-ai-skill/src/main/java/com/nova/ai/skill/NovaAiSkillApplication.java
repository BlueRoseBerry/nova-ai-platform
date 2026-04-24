package com.nova.ai.skill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nova.ai")
public class NovaAiSkillApplication {
    public static void main(String[] args) {
        SpringApplication.run(NovaAiSkillApplication.class, args);
    }
}
