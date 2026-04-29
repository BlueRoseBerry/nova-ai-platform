package com.nova.ai.openclaw.protocol;

import java.util.Map;

public record OpenClawPayload(
    String action,
    Map<String, Object> parameters,
    String content
) {}
