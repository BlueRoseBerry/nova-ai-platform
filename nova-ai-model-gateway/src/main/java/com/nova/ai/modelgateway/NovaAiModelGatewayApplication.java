package com.nova.ai.modelgateway;

import com.nova.ai.modelgateway.config.NovaModelProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {"com.nova.ai.modelgateway", "com.nova.ai.common"})
@MapperScan("com.nova.ai.modelgateway.mapper")
@EnableConfigurationProperties(NovaModelProperties.class)
public class NovaAiModelGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(NovaAiModelGatewayApplication.class, args);
    }
}
