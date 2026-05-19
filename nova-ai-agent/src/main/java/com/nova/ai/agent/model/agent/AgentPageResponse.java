package com.nova.ai.agent.model.agent;

import com.nova.ai.agent.model.Agent;
import lombok.Data;

import java.util.List;

@Data
public class AgentPageResponse {

    private Long total;

    private Long pages;

    private Long current;

    private Long pageSize;

    private List<Agent> records;
}
