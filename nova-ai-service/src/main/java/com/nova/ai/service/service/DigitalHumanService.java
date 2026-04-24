package com.nova.ai.service.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nova.ai.common.exception.ErrorCode;
import com.nova.ai.common.exception.NovaAiException;
import com.nova.ai.service.dto.*;
import com.nova.ai.service.entity.DigitalHuman;
import com.nova.ai.service.repository.DigitalHumanMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class DigitalHumanService {

    private final DigitalHumanMapper digitalHumanMapper;

    public DigitalHumanService(DigitalHumanMapper digitalHumanMapper) {
        this.digitalHumanMapper = digitalHumanMapper;
    }

    public DigitalHumanResponse create(String userId, DigitalHumanCreateRequest request) {
        DigitalHuman entity = new DigitalHuman();
        entity.setUserId(userId);
        entity.setName(request.name());
        entity.setAvatarUrl(request.avatarUrl());
        entity.setVoiceModel(request.voiceModel());
        entity.setPersonality(request.personality());
        entity.setSkills(request.skills());
        entity.setAgentId(request.agentId());
        entity.setWorkflowId(request.workflowId());
        entity.setPublishStatus("draft");
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        entity.setDeleted(false);

        digitalHumanMapper.insert(entity);
        return toResponse(entity);
    }

    public DigitalHumanResponse getById(Long id) {
        DigitalHuman entity = digitalHumanMapper.selectById(id);
        if (entity == null) {
            throw new NovaAiException(ErrorCode.DIGITAL_HUMAN_NOT_FOUND);
        }
        return toResponse(entity);
    }

    public DigitalHumanResponse update(String userId, DigitalHumanUpdateRequest request) {
        DigitalHuman entity = digitalHumanMapper.selectById(request.id());
        if (entity == null) {
            throw new NovaAiException(ErrorCode.DIGITAL_HUMAN_NOT_FOUND);
        }
        if (userId != null && !entity.getUserId().equals(userId)) {
            throw new NovaAiException(ErrorCode.FORBIDDEN);
        }
        if (request.name() != null) entity.setName(request.name());
        if (request.avatarUrl() != null) entity.setAvatarUrl(request.avatarUrl());
        if (request.voiceModel() != null) entity.setVoiceModel(request.voiceModel());
        if (request.personality() != null) entity.setPersonality(request.personality());
        if (request.skills() != null) entity.setSkills(request.skills());
        if (request.agentId() != null) entity.setAgentId(request.agentId());
        if (request.workflowId() != null) entity.setWorkflowId(request.workflowId());
        entity.setUpdatedAt(Instant.now());

        digitalHumanMapper.updateById(entity);
        return toResponse(entity);
    }

    @Transactional
    public void delete(String userId, Long id) {
        DigitalHuman entity = digitalHumanMapper.selectById(id);
        if (entity == null) {
            throw new NovaAiException(ErrorCode.DIGITAL_HUMAN_NOT_FOUND);
        }
        if (userId != null && !entity.getUserId().equals(userId)) {
            throw new NovaAiException(ErrorCode.FORBIDDEN);
        }
        digitalHumanMapper.deleteById(id);
    }

    public DigitalHumanPageResponse page(String userId, DigitalHumanPageRequest request) {
        Page<DigitalHuman> page = new Page<>(request.pageNum(), request.pageSize());
        LambdaQueryWrapper<DigitalHuman> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(DigitalHuman::getUserId, userId);
        }
        wrapper.orderByDesc(DigitalHuman::getCreatedAt);
        Page<DigitalHuman> result = digitalHumanMapper.selectPage(page, wrapper);

        List<DigitalHumanResponse> records = result.getRecords().stream()
            .map(this::toResponse)
            .toList();

        return new DigitalHumanPageResponse(
            result.getTotal(),
            result.getPages(),
            result.getCurrent(),
            result.getSize(),
            records
        );
    }

    public DigitalHumanResponse publish(String userId, Long id) {
        DigitalHuman entity = digitalHumanMapper.selectById(id);
        if (entity == null) {
            throw new NovaAiException(ErrorCode.DIGITAL_HUMAN_NOT_FOUND);
        }
        if (userId != null && !entity.getUserId().equals(userId)) {
            throw new NovaAiException(ErrorCode.FORBIDDEN);
        }
        entity.setPublishStatus("published");
        entity.setUpdatedAt(Instant.now());
        digitalHumanMapper.updateById(entity);
        return toResponse(entity);
    }

    private DigitalHumanResponse toResponse(DigitalHuman entity) {
        return new DigitalHumanResponse(
            entity.getId(),
            entity.getUserId(),
            entity.getName(),
            entity.getAvatarUrl(),
            entity.getVoiceModel(),
            entity.getPersonality(),
            entity.getSkills(),
            entity.getAgentId(),
            entity.getWorkflowId(),
            entity.getPublishStatus(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
