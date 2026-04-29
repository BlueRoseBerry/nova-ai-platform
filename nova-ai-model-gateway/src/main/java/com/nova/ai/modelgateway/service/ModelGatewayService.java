package com.nova.ai.modelgateway.service;

import com.nova.ai.modelgateway.adapter.*;
import com.nova.ai.modelgateway.model.ChatRequest;
import com.nova.ai.modelgateway.model.ChatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ModelGatewayService {

    private static final Logger log = LoggerFactory.getLogger(ModelGatewayService.class);
    private final Map<String, ModelAdapter> adapters;

    public ModelGatewayService(List<ModelAdapter> adapterList) {
        this.adapters = adapterList.stream()
            .collect(Collectors.toMap(ModelAdapter::getProvider, Function.identity()));
    }

    public ChatResponse chat(String provider, ChatRequest request) {
        ModelAdapter adapter = adapters.getOrDefault(provider, adapters.get("openai"));
        log.info("Routing to provider: {}, model: {}", provider, request.model());
        return adapter.chat(request);
    }

    public ChatResponse chatWithFallback(List<String> fallbackProviders, ChatRequest request) {
        Exception lastException = null;
        for (String provider : fallbackProviders) {
            try {
                ModelAdapter adapter = adapters.get(provider);
                if (adapter != null) {
                    log.info("Trying provider: {}", provider);
                    return adapter.chat(request);
                }
            } catch (Exception e) {
                log.warn("Provider {} failed, trying fallback: {}", provider, e.getMessage());
                lastException = e;
            }
        }
        throw new RuntimeException("All providers failed: " + (lastException != null ? lastException.getMessage() : "none"));
    }

    public Map<String, ModelAdapter> getAvailableProviders() {
        return adapters;
    }
}
