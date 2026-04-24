package com.nova.ai.service.controller;

import com.nova.ai.common.response.BaseResponse;
import com.nova.ai.service.dto.*;
import com.nova.ai.service.service.DigitalHumanService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/digital-human")
public class DigitalHumanController {

    private static final Logger log = LoggerFactory.getLogger(DigitalHumanController.class);
    private final DigitalHumanService digitalHumanService;

    public DigitalHumanController(DigitalHumanService digitalHumanService) {
        this.digitalHumanService = digitalHumanService;
    }

    @PostMapping("/create")
    public BaseResponse<DigitalHumanResponse> create(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody DigitalHumanCreateRequest request) {
        log.info("Create digital human, user: {}, name: {}", userId, request.name());
        return BaseResponse.success(digitalHumanService.create(userId, request));
    }

    @GetMapping("/get/{id}")
    public BaseResponse<DigitalHumanResponse> getById(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @PathVariable Long id) {
        return BaseResponse.success(digitalHumanService.getById(id));
    }

    @PostMapping("/update")
    public BaseResponse<DigitalHumanResponse> update(
            @RequestHeader("X-User-Id") String userId,
            @Valid @RequestBody DigitalHumanUpdateRequest request) {
        log.info("Update digital human, user: {}, id: {}", userId, request.id());
        return BaseResponse.success(digitalHumanService.update(userId, request));
    }

    @PostMapping("/delete/{id}")
    public BaseResponse<Void> delete(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable Long id) {
        log.info("Delete digital human, user: {}, id: {}", userId, id);
        digitalHumanService.delete(userId, id);
        return BaseResponse.success(null);
    }

    @GetMapping("/page")
    public BaseResponse<DigitalHumanPageResponse> page(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @Valid DigitalHumanPageRequest request) {
        return BaseResponse.success(digitalHumanService.page(userId, request));
    }

    @PostMapping("/publish/{id}")
    public BaseResponse<DigitalHumanResponse> publish(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable Long id) {
        log.info("Publish digital human, user: {}, id: {}", userId, id);
        return BaseResponse.success(digitalHumanService.publish(userId, id));
    }
}
