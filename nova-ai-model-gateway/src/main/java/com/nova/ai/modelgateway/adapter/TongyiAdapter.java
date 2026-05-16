package com.nova.ai.modelgateway.adapter;

import com.nova.ai.modelgateway.model.ChatRequest;
import com.nova.ai.modelgateway.model.ChatResponse;
import org.springframework.stereotype.Component;

@Component
public class TongyiAdapter implements ModelAdapter {
    @Override
    public String getProvider() { return "tongyi"; }
    @Override
    public ChatResponse chat(ChatRequest request) {
        return new ChatResponse(
            "This is a simulated Tongyi response for model: " + request.getModel(),
            request.getPrompt().length() / 4, 150, request.getModel());
    }
}
