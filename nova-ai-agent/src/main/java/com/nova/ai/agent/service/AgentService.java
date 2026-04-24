package com.nova.ai.agent.service;

import com.nova.ai.agent.model.*;
import com.nova.ai.agent.orchestrator.AgentOrchestrator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * Agent Service with JDK 25 Structured Concurrency.
 * Orchestrates agent execution with parallel subtasks.
 */
@Service
public class AgentService {

    private final Map<String, Agent> agentRegistry = new ConcurrentHashMap<>();
    private final AgentOrchestrator orchestrator = new AgentOrchestrator();

    public void register(Agent agent) {
        agentRegistry.put(agent.id(), agent);
    }

    public AgentResponse execute(AgentRequest request) throws InterruptedException, ExecutionException {
        Agent agent = agentRegistry.get(request.agentId());
        if (agent == null) {
            return new ErrorResponse("resp-err", 404, "Agent not found: " + request.agentId());
        }
        return orchestrator.orchestrate(request);
    }

    public Agent getAgent(String agentId) {
        return agentRegistry.get(agentId);
    }

    public List<Agent> listAgents() {
        return List.copyOf(agentRegistry.values());
    }
}
