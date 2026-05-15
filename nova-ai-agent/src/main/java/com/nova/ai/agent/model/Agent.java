package com.nova.ai.agent.model;

import java.util.List;
import java.util.Map;

public class Agent {

    private String id;
    private String name;
    private String role;
    private String systemPrompt;
    private List<String> skillIds;
    private Map<String, Object> config;
    private String modelId;
    private double temperature;
    private int maxTokens;

    public Agent() {
    }

    public Agent(
        String id,
        String name,
        String role,
        String systemPrompt,
        List<String> skillIds,
        Map<String, Object> config,
        String modelId,
        double temperature,
        int maxTokens
    ) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.systemPrompt = systemPrompt;
        this.skillIds = skillIds;
        this.config = config;
        this.modelId = modelId;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }

    public List<String> getSkillIds() {
        return skillIds;
    }

    public void setSkillIds(List<String> skillIds) {
        this.skillIds = skillIds;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }
}
