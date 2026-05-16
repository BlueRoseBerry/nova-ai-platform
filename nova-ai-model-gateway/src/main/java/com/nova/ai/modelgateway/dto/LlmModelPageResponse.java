package com.nova.ai.modelgateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LlmModelPageResponse {

    private long total;
    private long pages;
    private long current;
    private long size;
    private List<LlmModelResponse> records;
}
