package com.nova.ai.agent.service;

import com.nova.ai.agent.model.Agent;
import com.nova.ai.agent.model.AgentChatRequest;
import com.nova.ai.agent.model.AgentChatResponse;
import com.nova.ai.agent.model.AgentRequest;
import com.nova.ai.agent.model.AgentResponse;
import com.nova.ai.agent.model.agent.AgentPageRequest;
import com.nova.ai.agent.model.agent.AgentPageResponse;
import com.nova.ai.agent.model.agent.AgentRegisterRequest;

import java.util.List;

public interface AgentService {

    Agent register(AgentRegisterRequest request);

    AgentResponse execute(AgentRequest request);

    AgentChatResponse chat(AgentChatRequest request);

    reactor.core.publisher.Flux<String> chatStream(AgentChatRequest request);

    Agent getAgent(String agentId);

    AgentPageResponse listAgents(AgentPageRequest agentPageRequest);

    Agent updateAgent(Agent agent);

    boolean deleteAgent(String agentId);
}