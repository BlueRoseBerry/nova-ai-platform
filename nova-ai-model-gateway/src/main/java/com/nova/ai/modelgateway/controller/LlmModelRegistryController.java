package com.nova.ai.modelgateway.controller;

import com.nova.ai.common.response.BaseResponse;
import com.nova.ai.modelgateway.dto.LlmModelPageRequest;
import com.nova.ai.modelgateway.dto.LlmModelPageResponse;
import com.nova.ai.modelgateway.dto.LlmModelResponse;
import com.nova.ai.modelgateway.dto.ModelCreatePayload;
import com.nova.ai.modelgateway.dto.ModelUpdatePayload;
import com.nova.ai.modelgateway.service.LlmModelService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/model/registry")
public class LlmModelRegistryController {

    private final LlmModelService llmModelService;

    public LlmModelRegistryController(LlmModelService llmModelService) {
        this.llmModelService = llmModelService;
    }

    @PostMapping("/create")
    public BaseResponse<LlmModelResponse> create(@Valid @RequestBody ModelCreatePayload body) {
        return BaseResponse.success(llmModelService.create(body));
    }

    @GetMapping("/get/{id}")
    public BaseResponse<LlmModelResponse> get(@PathVariable("id") String id) {
        return BaseResponse.success(llmModelService.getById(id));
    }

    @PostMapping("/update")
    public BaseResponse<LlmModelResponse> update(@Valid @RequestBody ModelUpdatePayload body) {
        return BaseResponse.success(llmModelService.update(body));
    }

    @PostMapping("/delete/{id}")
    public BaseResponse<Void> delete(@PathVariable("id") String id) {
        llmModelService.delete(id);
        return BaseResponse.success(null);
    }

    /** 使用查询参数：`pageNum` `pageSize` `enabled`。 */
    @GetMapping("/page")
    public BaseResponse<LlmModelPageResponse> page(@Valid @ModelAttribute LlmModelPageRequest request) {
        return BaseResponse.success(llmModelService.page(request));
    }
}
