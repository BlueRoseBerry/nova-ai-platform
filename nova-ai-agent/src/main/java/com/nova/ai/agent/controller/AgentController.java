package com.nova.ai.agent.controller;

import com.nova.ai.agent.model.Agent;
import com.nova.ai.agent.model.AgentChatRequest;
import com.nova.ai.agent.model.AgentChatResponse;
import com.nova.ai.agent.model.AgentRequest;
import com.nova.ai.agent.model.AgentResponse;
import com.nova.ai.agent.model.agent.AgentPageResponse;
import com.nova.ai.agent.model.agent.AgentPageRequest;
import com.nova.ai.agent.model.agent.AgentRegisterRequest;

import com.nova.ai.agent.service.AgentService;
import com.nova.ai.common.response.BaseResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/agents")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/register")
    public BaseResponse<Agent> register(@Valid @RequestBody AgentRegisterRequest request) {
        Agent agent = agentService.register(request);
        return BaseResponse.success(agent);
    }

    @PostMapping("/update")
    public BaseResponse<Agent> updateAgent(@RequestBody Agent agent) {
        Agent updated = agentService.updateAgent(agent);
        if (updated == null) {
            return BaseResponse.fail(404, "Agent not found: " + agent.getId());
        }
        return BaseResponse.success(updated);
    }

    @PostMapping("/execute")
    public BaseResponse<AgentResponse> execute(@RequestBody AgentRequest request) {
        return BaseResponse.success(agentService.execute(request));
    }

    @PostMapping("/chat")
    public BaseResponse<AgentChatResponse> chat(@Valid @RequestBody AgentChatRequest request) {
        return BaseResponse.success(agentService.chat(request));
    }

    @PostMapping(value = "/chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public reactor.core.publisher.Flux<String> chatStream(@Valid @RequestBody AgentChatRequest request) {
        return agentService.chatStream(request);
    }

    @GetMapping("/{agentId}")
    public BaseResponse<Agent> getAgent(@PathVariable String agentId) {
        return BaseResponse.success(agentService.getAgent(agentId));
    }

    @PostMapping("lists")
    public BaseResponse<AgentPageResponse> listAgents(@RequestBody AgentPageRequest agentPageRequest) {
        return BaseResponse.success(agentService.listAgents(agentPageRequest));
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteAgent(@RequestBody Agent agent) {
        boolean removed = agentService.deleteAgent(agent.getId());
        if (!removed) {
            return BaseResponse.fail(404, "Agent not found: " + agent.getId());
        }
        return BaseResponse.success(true);
    }
}