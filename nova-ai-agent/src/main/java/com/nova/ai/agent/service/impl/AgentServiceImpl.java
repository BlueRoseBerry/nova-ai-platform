package com.nova.ai.agent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nova.ai.agent.entity.AgentEntity;
import com.nova.ai.agent.mapper.AgentMapper;
import com.nova.ai.agent.model.*;
import com.nova.ai.agent.model.agent.AgentPageRequest;
import com.nova.ai.agent.model.agent.AgentPageResponse;
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
    public void register(Agent agent) {
        AgentEntity existing = agentMapper.selectById(agent.getId());
        AgentEntity entity = toEntity(agent);
        if (existing == null) {
            agentMapper.insert(entity);
            return;
        }
        agentMapper.updateById(entity);
    }

    @Override
    public AgentResponse execute(AgentRequest request) {
        AgentEntity entity = agentMapper.selectById(request.getAgentId());
        Agent agent = entity == null ? null : toModel(entity);
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
    public Agent getAgent(String agentId) {
        AgentEntity entity = agentMapper.selectById(agentId);
        return entity == null ? null : toModel(entity);
    }

    @Override
    public AgentPageResponse listAgents(AgentPageRequest agentPageRequest) {
        int current = agentPageRequest.getCurrent() != null && agentPageRequest.getCurrent() > 0
            ? agentPageRequest.getCurrent() : 1;
        int pageSize = agentPageRequest.getPageSize() != null && agentPageRequest.getPageSize() > 0
            ? agentPageRequest.getPageSize() : 10;

        Page<AgentEntity> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<AgentEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(agentPageRequest.getName())) {
            queryWrapper.like(AgentEntity::getName, agentPageRequest.getName());
        }
        queryWrapper.orderByDesc(AgentEntity::getCreateDate);

        IPage<AgentEntity> result = agentMapper.selectPage(page, queryWrapper);

        AgentPageResponse response = new AgentPageResponse();
        response.setTotal(result.getTotal());
        response.setPages(result.getPages());
        response.setCurrent(result.getCurrent());
        response.setPageSize(result.getSize());
        response.setRecords(result.getRecords().stream().map(this::toModel).toList());
        return response;
    }

    @Override
    public Agent updateAgent(Agent agent) {
        AgentEntity existing = agentMapper.selectById(agent.getId());
        if (existing == null) {
            return null;
        }
        agentMapper.updateById(toEntity(agent));
        return toModel(agentMapper.selectById(agent.getId()));
    }

    @Override
    public boolean deleteAgent(String agentId) {
        return agentMapper.deleteById(agentId) > 0;
    }

    private AgentEntity toEntity(Agent agent) {
        AgentEntity entity = new AgentEntity();
        entity.setId(agent.getId());
        entity.setName(agent.getName());
        entity.setRole(agent.getRole());
        entity.setSystemPrompt(agent.getSystemPrompt());
        entity.setSkillIds(agent.getSkillIds());
        entity.setConfig(agent.getConfig());
        entity.setModelId(agent.getModelId());
        entity.setTemperature(agent.getTemperature());
        entity.setMaxTokens(agent.getMaxTokens());
        return entity;
    }

    private Agent toModel(AgentEntity entity) {
        Agent agent = new Agent(
            entity.getId(),
            entity.getName(),
            entity.getRole(),
            entity.getSystemPrompt(),
            entity.getSkillIds(),
            entity.getConfig(),
            entity.getModelId(),
            entity.getTemperature() == null ? 0.0 : entity.getTemperature(),
            entity.getMaxTokens() == null ? 0 : entity.getMaxTokens()
        );
        agent.setCreateUser(entity.getCreateUser());
        agent.setUpdateUser(entity.getUpdateUser());
        agent.setCreateDate(entity.getCreateDate());
        agent.setUpdateDate(entity.getUpdateDate());
        agent.setDeleted(entity.getDeleted());
        return agent;
    }
}
