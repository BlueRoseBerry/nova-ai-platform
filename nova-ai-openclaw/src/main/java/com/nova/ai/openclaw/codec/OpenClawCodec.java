package com.nova.ai.openclaw.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nova.ai.openclaw.protocol.OpenClawMessage;

/**
 * OpenCLAW message encoder/decoder.
 */
public class OpenClawCodec {

    private final ObjectMapper mapper = new ObjectMapper();

    public String encode(OpenClawMessage message) {
        try {
            return mapper.writeValueAsString(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode OpenCLAW message", e);
        }
    }

    public OpenClawMessage decode(String json) {
        try {
            return mapper.readValue(json, OpenClawMessage.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode OpenCLAW message", e);
        }
    }
}
