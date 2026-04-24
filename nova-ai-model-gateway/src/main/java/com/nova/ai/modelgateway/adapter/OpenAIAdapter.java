package com.nova.ai.modelgateway.adapter;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class OpenAIAdapter implements ModelAdapter {
    @Override
    public String getProvider() { return "openai"; }

    @Override
    public ChatResponse chat(ChatRequest request) {
        return new ChatResponse(
            "This is a simulated OpenAI response for model: " + request.model(),
            request.prompt().length() / 4,
            150,
            request.model()
        );
    }
}
