package com.nova.ai.agent.model;

import com.nova.ai.common.model.BaseEntity;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Agent extends BaseEntity {

    private String id;
    private String name;
    private String role;
    private String systemPrompt;
    private List<String> skillIds;
    private Map<String, Object> config;
    private String modelId;
    private double temperature;
    private int maxTokens;
}
