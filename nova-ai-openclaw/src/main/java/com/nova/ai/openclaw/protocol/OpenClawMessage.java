package com.nova.ai.openclaw.protocol;

import java.time.Instant;
import java.util.Map;

/**
 * OpenCLAW Protocol: Open protocol for cross-agent communication.
 * Defines message format, envelope, and payload structure.
 */
public record OpenClawMessage(
    String messageId,
    Instant timestamp,
    MessageType type,
    String senderAgentId,
    String receiverAgentId,
    OpenClawPayload payload
) {}

