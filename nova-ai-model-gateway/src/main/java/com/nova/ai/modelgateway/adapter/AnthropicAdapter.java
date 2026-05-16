package com.nova.ai.modelgateway.adapter;

import com.nova.ai.modelgateway.model.ChatRequest;
import com.nova.ai.modelgateway.model.ChatResponse;
import org.springframework.stereotype.Component;

@Component
public class AnthropicAdapter implements ModelAdapter {
    @Override
    public String getProvider() { return "anthropic"; }
    @Override
    public ChatResponse chat(ChatRequest request) {
        return new ChatResponse(
            "This is a simulated Anthropic response for model: " + request.getModel(),
            request.getPrompt().length() / 4, 150, request.getModel());
    }
}
