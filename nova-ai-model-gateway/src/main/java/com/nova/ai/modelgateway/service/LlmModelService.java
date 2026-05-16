package com.nova.ai.modelgateway.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nova.ai.common.exception.ErrorCode;
import com.nova.ai.common.exception.NovaAiException;
import com.nova.ai.modelgateway.dto.LlmModelPageRequest;
import com.nova.ai.modelgateway.dto.LlmModelPageResponse;
import com.nova.ai.modelgateway.dto.LlmModelResponse;
import com.nova.ai.modelgateway.dto.ModelCreatePayload;
import com.nova.ai.modelgateway.dto.ModelUpdatePayload;
import com.nova.ai.modelgateway.entity.LlmModelEntity;
import com.nova.ai.modelgateway.mapper.LlmModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LlmModelService {

    private static final String DEFAULT_INVOKE_FORMAT = "openai_chat_completions";

    private final LlmModelMapper llmModelMapper;

    public LlmModelService(LlmModelMapper llmModelMapper) {
        this.llmModelMapper = llmModelMapper;
    }

    @Transactional
    public LlmModelResponse create(ModelCreatePayload payload) {
        if (llmModelMapper.selectById(payload.getId()) != null) {
            throw new NovaAiException(ErrorCode.INVALID_REQUEST.code(), "模型注册 id 已存在: " + payload.getId());
        }
        Instant now = Instant.now();
        LlmModelEntity e = new LlmModelEntity();
        e.setId(payload.getId());
        e.setName(payload.getName());
        e.setProvider(payload.getProvider());
        e.setInvokeFormat(payload.getInvokeFormat());
        e.setRemoteModel(payload.getRemoteModel());
        e.setBaseUrl(blankToNull(payload.getBaseUrl()));
        e.setApiKey(blankToNull(payload.getApiKey()));
        e.setDefaultTemperature(payload.getDefaultTemperature());
        e.setDefaultMaxTokens(payload.getDefaultMaxTokens());
        e.setEnabled(payload.getEnabled());
        e.setDescription(payload.getDescription());
        e.setExtraConfig(copyMap(payload.getExtraConfig()));
        if (e.getInvokeFormat() == null || e.getInvokeFormat().isBlank()) {
            e.setInvokeFormat(DEFAULT_INVOKE_FORMAT);
        }
        e.setDeleted(Boolean.FALSE);
        e.setCreatedAt(now);
        e.setUpdatedAt(now);
        llmModelMapper.insert(e);
        return toResponse(e);
    }

    public LlmModelResponse getById(String id) {
        LlmModelEntity entity = llmModelMapper.selectById(id);
        if (entity == null) {
            throw new NovaAiException(ErrorCode.MODEL_NOT_FOUND);
        }
        return toResponse(entity);
    }

    @Transactional
    public LlmModelResponse update(ModelUpdatePayload payload) {
        LlmModelEntity entity = llmModelMapper.selectById(payload.getId());
        if (entity == null) {
            throw new NovaAiException(ErrorCode.MODEL_NOT_FOUND);
        }
        if (payload.getName() != null) {
            entity.setName(payload.getName());
        }
        if (payload.getProvider() != null) {
            entity.setProvider(payload.getProvider());
        }
        if (payload.getInvokeFormat() != null && !payload.getInvokeFormat().isBlank()) {
            entity.setInvokeFormat(payload.getInvokeFormat());
        }
        if (payload.getRemoteModel() != null) {
            entity.setRemoteModel(payload.getRemoteModel());
        }
        if (payload.getBaseUrl() != null) {
            entity.setBaseUrl(blankToNull(payload.getBaseUrl()));
        }
        if (payload.getApiKeySecretUpdate() != null) {
            entity.setApiKey(blankToNull(payload.getApiKeySecretUpdate()));
        }
        if (payload.getDefaultTemperature() != null) {
            entity.setDefaultTemperature(payload.getDefaultTemperature());
        }
        if (payload.getDefaultMaxTokens() != null) {
            entity.setDefaultMaxTokens(payload.getDefaultMaxTokens());
        }
        if (payload.getEnabled() != null) {
            entity.setEnabled(payload.getEnabled());
        }
        if (payload.getDescription() != null) {
            entity.setDescription(payload.getDescription());
        }
        if (payload.getExtraConfig() != null) {
            entity.setExtraConfig(copyMap(payload.getExtraConfig()));
        }
        entity.setUpdatedAt(Instant.now());
        llmModelMapper.updateById(entity);
        return toResponse(entity);
    }

    @Transactional
    public void delete(String id) {
        int rows = llmModelMapper.deleteById(id);
        if (rows == 0) {
            throw new NovaAiException(ErrorCode.MODEL_NOT_FOUND);
        }
    }

    public LlmModelPageResponse page(LlmModelPageRequest request) {
        LambdaQueryWrapper<LlmModelEntity> filter = filterWrapper(request.getEnabled());
        long total = llmModelMapper.selectCount(filter);

        LambdaQueryWrapper<LlmModelEntity> query = filterWrapper(request.getEnabled());
        query.orderByDesc(LlmModelEntity::getCreatedAt);
        long offset = (long) (request.getPageNum() - 1) * request.getPageSize();
        query.last(true, String.format("LIMIT %d OFFSET %d", request.getPageSize(), offset));
        List<LlmModelEntity> list = llmModelMapper.selectList(query);

        long pages = total == 0 ? 0 : (total + request.getPageSize() - 1) / request.getPageSize();
        List<LlmModelResponse> records = list.stream().map(this::toResponse).toList();
        return new LlmModelPageResponse(
            total,
            pages,
            request.getPageNum(),
            request.getPageSize(),
            records
        );
    }

    private static LambdaQueryWrapper<LlmModelEntity> filterWrapper(Boolean enabled) {
        LambdaQueryWrapper<LlmModelEntity> w = new LambdaQueryWrapper<>();
        if (enabled != null) {
            w.eq(LlmModelEntity::getEnabled, enabled);
        }
        return w;
    }

    public LlmModelEntity requireEnabledForInvoke(String id) {
        LlmModelEntity entity = llmModelMapper.selectById(id);
        if (entity == null) {
            throw new NovaAiException(ErrorCode.MODEL_NOT_FOUND);
        }
        if (!Boolean.TRUE.equals(entity.getEnabled())) {
            throw new NovaAiException(ErrorCode.MODEL_UNAVAILABLE.code(), "模型未启用: " + id);
        }
        return entity;
    }

    private LlmModelResponse toResponse(LlmModelEntity entity) {
        boolean hasSecret = StringUtils.hasText(entity.getApiKey());
        Instant created = entity.getCreatedAt();
        Instant updated = entity.getUpdatedAt();
        LlmModelResponse r = new LlmModelResponse();
        r.setId(entity.getId());
        r.setName(entity.getName());
        r.setProvider(entity.getProvider());
        r.setInvokeFormat(entity.getInvokeFormat());
        r.setRemoteModel(entity.getRemoteModel());
        r.setBaseUrl(entity.getBaseUrl());
        r.setHasApiSecret(hasSecret);
        r.setDefaultTemperature(entity.getDefaultTemperature());
        r.setDefaultMaxTokens(entity.getDefaultMaxTokens());
        r.setEnabled(entity.getEnabled());
        r.setDescription(entity.getDescription());
        r.setExtraConfig(entity.getExtraConfig() == null ? new HashMap<>() : new HashMap<>(entity.getExtraConfig()));
        r.setCreatedAtEpochMillis(created == null ? 0L : created.toEpochMilli());
        r.setUpdatedAtEpochMillis(updated == null ? 0L : updated.toEpochMilli());
        return r;
    }

    private static String blankToNull(String s) {
        return StringUtils.hasText(s) ? s : null;
    }

    private static Map<String, Object> copyMap(Map<String, Object> raw) {
        if (raw == null || raw.isEmpty()) {
            return new HashMap<>();
        }
        return new HashMap<>(raw);
    }
}
