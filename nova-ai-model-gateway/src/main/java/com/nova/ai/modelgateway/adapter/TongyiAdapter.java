package com.nova.ai.modelgateway.adapter;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class TongyiAdapter implements ModelAdapter {
    @Override
    public String getProvider() { return "tongyi"; }
    @Override
    public ChatResponse chat(ChatRequest request) {
        return new ChatResponse(
            "This is a simulated Tongyi response for model: " + request.model(),
            request.prompt().length() / 4, 150, request.model());
    }
}
