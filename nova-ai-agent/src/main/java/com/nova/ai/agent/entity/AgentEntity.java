package com.nova.ai.agent.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nova.ai.agent.typehandler.PostgresqlJsonbJacksonTypeHandler;
import lombok.Data;

import java.util.List;
import java.util.Map;

@TableName(value = "agent", autoResultMap = true)
@Data
public class AgentEntity {

    @TableId
    private String id;
    private String name;
    private String role;
    private String systemPrompt;
    @TableField(typeHandler = PostgresqlJsonbJacksonTypeHandler.class)
    private List<String> skillIds;
    @TableField(typeHandler = PostgresqlJsonbJacksonTypeHandler.class)
    private Map<String, Object> config;
    private String modelId;
    private Double temperature;
    private Integer maxTokens;

}
