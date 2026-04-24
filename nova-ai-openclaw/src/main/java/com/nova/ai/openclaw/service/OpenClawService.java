package com.nova.ai.openclaw.service;

import com.nova.ai.openclaw.codec.OpenClawCodec;
import com.nova.ai.openclaw.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OpenCLAW Service: Open protocol for cross-agent communication.
 * Handles message encoding, decoding, routing, and delivery.
 */
@Service
public class OpenClawService {

    private static final Logger log = LoggerFactory.getLogger(OpenClawService.class);
    private final OpenClawCodec codec = new OpenClawCodec();
    private final Map<String, MessageHandler> handlers = new ConcurrentHashMap<>();

    public void registerHandler(String action, MessageHandler handler) {
        handlers.put(action, handler);
    }

    public String sendMessage(String senderId, String receiverId, String action, Map<String, Object> params, String content) {
        String messageId = UUID.randomUUID().toString();
        OpenClawMessage message = new OpenClawMessage(
            messageId, Instant.now(), MessageType.REQUEST,
            senderId, receiverId,
            new OpenClawPayload(action, params, content)
        );
        String encoded = codec.encode(message);
        log.info("Sending OpenCLAW message: {} from {} to {} action={}", messageId, senderId, receiverId, action);
        return encoded;
    }

    public OpenClawMessage receiveMessage(String encoded) {
        OpenClawMessage message = codec.decode(encoded);
        log.info("Received OpenCLAW message: {} type={}", message.messageId(), message.type());
        return message;
    }

    public String processReceivedMessage(String encoded) {
        OpenClawMessage message = receiveMessage(encoded);
        MessageHandler handler = handlers.get(message.payload().action());
        if (handler == null) {
            return codec.encode(new OpenClawMessage(
                message.messageId(), Instant.now(), MessageType.ERROR,
                "system", message.senderAgentId(),
                new OpenClawPayload("error", Map.of("reason", "Unknown action: " + message.payload().action()), "")
            ));
        }
        String response = handler.handle(message);
        log.info("Processed OpenCLAW message: {}", message.messageId());
        return response;
    }

    public interface MessageHandler {
        String handle(OpenClawMessage message);
    }
}
