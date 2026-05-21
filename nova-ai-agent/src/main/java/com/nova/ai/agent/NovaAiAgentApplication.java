package com.nova.ai.agent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.nova.ai.agent", "com.nova.ai.modelgateway", "com.nova.ai.common"})
@MapperScan({"com.nova.ai.agent.mapper", "com.nova.ai.modelgateway.mapper"})
public class NovaAiAgentApplication {
    public static void main(String[] args) {
        SpringApplication.run(NovaAiAgentApplication.class, args);
    }
}
