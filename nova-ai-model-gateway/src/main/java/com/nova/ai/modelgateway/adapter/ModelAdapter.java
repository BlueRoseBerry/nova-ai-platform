package com.nova.ai.modelgateway.adapter;

import com.nova.ai.modelgateway.model.ChatRequest;
import com.nova.ai.modelgateway.model.ChatResponse;

public interface ModelAdapter {
    String getProvider();
    ChatResponse chat(ChatRequest request);
}



