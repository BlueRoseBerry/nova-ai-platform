package com.nova.ai.modelgateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "nova.model")
public class NovaModelProperties {

    /**
     * 按 provider（如 openai）提供默认密钥与 Base URL；当注册表条目未填写时回落。
     */
    private Map<String, ProviderBinding> providers = new HashMap<>();

    public Map<String, ProviderBinding> getProviders() {
        return providers;
    }

    public void setProviders(Map<String, ProviderBinding> providers) {
        this.providers = providers == null ? new HashMap<>() : new HashMap<>(providers);
    }

    /** YAML: api-key、base-url 通过 Spring 松散绑定填入。 */
    public static class ProviderBinding {

        /**
         * 对应 YAML {@code api-key}。
         */
        private String apiKey;

        /**
         * 对应 YAML {@code base-url}。
         */
        private String baseUrl;

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }
}
