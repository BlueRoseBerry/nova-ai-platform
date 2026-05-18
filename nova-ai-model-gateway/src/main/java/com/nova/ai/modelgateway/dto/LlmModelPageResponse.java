package com.nova.ai.modelgateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * LLM模型分页响应类
 * 用于封装分页查询结果，包含分页信息和模型列表数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LlmModelPageResponse {

    private long total;    // 总记录数
    private long pages;    // 总页数
    private long current;  // 当前页码
    private long size;     // 每页大小
    private List<LlmModelResponse> records;  // 模型列表数据
}
