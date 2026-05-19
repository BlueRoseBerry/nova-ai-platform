package com.nova.ai.agent.model.agent;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class AgentPageRequest {

    @Min(value = 1, message = "当前页码从 1 开始")
    private Integer current;

    @Min(value = 1, message = "每页条数至少为 1")
    @Max(value = 100, message = "每页条数不能超过 100")
    private Integer pageSize;

    private String name;
    private String phone;
    private String email;
}
