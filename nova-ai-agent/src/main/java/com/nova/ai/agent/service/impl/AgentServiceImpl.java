package com.nova.ai.agent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nova.ai.agent.mapper.AgentMapper;
import com.nova.ai.agent.model.*;
import com.nova.ai.agent.model.agent.AgentPageRequest;
import com.nova.ai.agent.model.agent.AgentPageResponse;
import com.nova.ai.agent.model.agent.AgentRegisterRequest;
import com.nova.ai.agent.orchestrator.AgentOrchestrator;
import com.nova.ai.agent.service.AgentService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.ExecutionException;

/**
 * Agent Service with JDK 25 Structured Concurrency.
 * Orchestrates agent execution with parallel subtasks.
 */
@Service
public class AgentServiceImpl implements AgentService {

    private final AgentMapper agentMapper;
    private final AgentOrchestrator orchestrator;

    public AgentServiceImpl(AgentMapper agentMapper, AgentOrchestrator orchestrator) {
        this.agentMapper = agentMapper;
        this.orchestrator = orchestrator;
    }

    @Override
    public Agent register(AgentRegisterRequest request) {
        Agent agent = new Agent();
        agent.setName(request.getName());
        agent.setRole(request.getRole());
        agent.setSystemPrompt(request.getSystemPrompt());
        agent.setSkillIds(request.getSkillIds());
        agent.setConfig(request.getConfig());
        agent.setModelId(request.getModelId());
        agent.setTemperature(request.getTemperature());
        agent.setMaxTokens(request.getMaxTokens());
        agentMapper.insert(agent);
        return agent;
    }

    @Override
    public AgentResponse execute(AgentRequest request) {
        Agent agent = agentMapper.selectById(request.getAgentId());
        if (agent == null) {
            return new ErrorResponse("resp-err", 404, "Agent not found: " + request.getAgentId());
        }
        try {
            return orchestrator.orchestrate(request);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ErrorResponse("resp-err", 500, "Agent execution interrupted");
        } catch (ExecutionException e) {
            return new ErrorResponse("resp-err", 500, "Agent execution failed: " + e.getCause().getMessage());
        }
    }

    @Override
    public AgentChatResponse chat(AgentChatRequest request) {
        Agent agent = agentMapper.selectById(request.getAgentId());
        if (agent == null) {
            throw new IllegalArgumentException("Agent not found: " + request.getAgentId());
        }
        return orchestrator.chat(agent, request);
    }

    @Override
    public reactor.core.publisher.Flux<String> chatStream(AgentChatRequest request) {
        Agent agent = agentMapper.selectById(request.getAgentId());
        if (agent == null) {
            return reactor.core.publisher.Flux.error(
                new IllegalArgumentException("Agent not found: " + request.getAgentId()));
        }
        return orchestrator.chatStream(agent, request);
    }

    @Override
    public Agent getAgent(String agentId) {
        return agentMapper.selectById(agentId);
    }

    @Override
    public AgentPageResponse listAgents(AgentPageRequest agentPageRequest) {
        int current = agentPageRequest.getCurrent() != null && agentPageRequest.getCurrent() > 0
            ? agentPageRequest.getCurrent() : 1;
        int pageSize = agentPageRequest.getPageSize() != null && agentPageRequest.getPageSize() > 0
            ? agentPageRequest.getPageSize() : 10;

        Page<Agent> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<Agent> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(agentPageRequest.getName())) {
            queryWrapper.like(Agent::getName, agentPageRequest.getName());
        }
        queryWrapper.orderByDesc(Agent::getCreateDate);

        IPage<Agent> result = agentMapper.selectPage(page, queryWrapper);

        AgentPageResponse response = new AgentPageResponse();
        response.setTotal(result.getTotal());
        response.setPages(result.getPages());
        response.setCurrent(result.getCurrent());
        response.setPageSize(result.getSize());
        response.setRecords(result.getRecords());
        return response;
    }

    @Override
    public Agent updateAgent(Agent agent) {
        Agent existing = agentMapper.selectById(agent.getId());
        if (existing == null) {
            return null;
        }
        agentMapper.updateById(agent);
        return agentMapper.selectById(agent.getId());
    }

    @Override
    public boolean deleteAgent(String agentId) {
        return agentMapper.deleteById(agentId) > 0;
    }
}