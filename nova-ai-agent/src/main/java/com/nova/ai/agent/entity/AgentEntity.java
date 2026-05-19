package com.nova.ai.agent.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nova.ai.agent.typehandler.PostgresqlJsonbJacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@TableName(value = "agent", autoResultMap = true)
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
    @TableLogic(value = "false", delval = "true")
    private Boolean deleted;

    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private String createUser;

    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @TableField(value = "created_date", fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    @TableField(value = "updated_date", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateDate;
}
