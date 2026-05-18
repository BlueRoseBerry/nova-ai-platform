package com.nova.ai.modelgateway.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * LlmModelPageRequest类用于分页查询大语言模型(Large Language Model)的请求参数封装
 * 该类使用了Lombok的@Data注解，自动生成getter、setter、equals、hashCode和toString方法
 */
@Data
public class LlmModelPageRequest {

    /**
     * 页码，必须大于等于1
     * 使用@NotNull注解确保该字段不能为null
     * 使用@Min注解确保页码最小值为1
     */
    @NotNull
    @Min(1)
    private Integer pageNum;

    /**
     * 每页大小，必须大于等于1且小于等于200
     * 使用@NotNull注解确保该字段不能为null
     * 使用@Min注解确保每页大小最小值为1
     * 使用@Max注解确保每页大小最大值为200
     */
    @NotNull
    @Min(1)
    @Max(200)
    private Integer pageSize;

    /**
     * 是否启用的状态筛选条件
     * 该字段为可选参数，用于筛选特定状态的模型
     */
    private Boolean enabled;
}
