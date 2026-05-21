package com.nova.ai.agent.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nova.ai.agent.typehandler.PostgresqlJsonbJacksonTypeHandler;
import com.nova.ai.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "agent", autoResultMap = true)
public class Agent extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
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