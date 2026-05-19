package com.nova.ai.agent.service;

import com.nova.ai.agent.model.Agent;
import com.nova.ai.agent.model.AgentRequest;
import com.nova.ai.agent.model.AgentResponse;
import com.nova.ai.agent.model.agent.AgentPageRequest;
import com.nova.ai.agent.model.agent.AgentPageResponse;

import java.util.List;

public interface AgentService {

    void register(Agent agent);

    AgentResponse execute(AgentRequest request);

    Agent getAgent(String agentId);

    AgentPageResponse listAgents(AgentPageRequest agentPageRequest);

    /** 更新已存在的 Agent，不存在则返回 {@code null} */
    Agent updateAgent(Agent agent);

    /** 按 id 删除，删除成功返回 true */
    boolean deleteAgent(String agentId);
}
