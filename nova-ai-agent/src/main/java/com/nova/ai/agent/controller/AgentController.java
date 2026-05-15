package com.nova.ai.agent.controller;

import com.nova.ai.agent.model.Agent;
import com.nova.ai.agent.model.AgentRequest;
import com.nova.ai.agent.model.AgentResponse;
import com.nova.ai.agent.service.AgentService;
import com.nova.ai.common.response.BaseResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agents")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/register")
    public BaseResponse<Void> register(@RequestBody Agent agent) {
        agentService.register(agent);
        return BaseResponse.success(null);
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

    @GetMapping("/{agentId}")
    public BaseResponse<Agent> getAgent(@PathVariable String agentId) {
        return BaseResponse.success(agentService.getAgent(agentId));
    }

    @GetMapping("lists")
    public BaseResponse<List<Agent>> listAgents() {
        return BaseResponse.success(agentService.listAgents());
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
