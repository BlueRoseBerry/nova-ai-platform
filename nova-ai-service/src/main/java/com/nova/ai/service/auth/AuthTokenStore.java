package com.nova.ai.service.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
public class AuthTokenStore {

    private static final String TOKEN_KEY_PREFIX = "nova:auth:token:";
    private static final String USER_KEY_PREFIX = "nova:auth:user:";

    private final StringRedisTemplate redisTemplate;
    private final Duration tokenTtl;

    public AuthTokenStore(
        StringRedisTemplate redisTemplate,
        @Value("${nova.auth.token-ttl-hours:24}") long tokenTtlHours
    ) {
        this.redisTemplate = redisTemplate;
        this.tokenTtl = Duration.ofHours(tokenTtlHours);
    }

    public Duration tokenTtl() {
        return tokenTtl;
    }

    public String createSession(Long userId) {
        String userKey = USER_KEY_PREFIX + userId;
        String oldToken = redisTemplate.opsForValue().get(userKey);
        if (oldToken != null) {
            redisTemplate.delete(TOKEN_KEY_PREFIX + oldToken);
        }

        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(TOKEN_KEY_PREFIX + token, String.valueOf(userId), tokenTtl);
        redisTemplate.opsForValue().set(userKey, token, tokenTtl);
        return token;
    }

    public Long resolveUserId(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        String userId = redisTemplate.opsForValue().get(TOKEN_KEY_PREFIX + token);
        if (userId == null) {
            return null;
        }
        try {
            return Long.parseLong(userId);
        } catch (NumberFormatException e) {
            redisTemplate.delete(TOKEN_KEY_PREFIX + token);
            return null;
        }
    }

    public void revoke(String token) {
        if (token == null || token.isBlank()) {
            return;
        }
        String tokenKey = TOKEN_KEY_PREFIX + token;
        String userId = redisTemplate.opsForValue().get(tokenKey);
        redisTemplate.delete(tokenKey);
        if (userId != null) {
            redisTemplate.delete(USER_KEY_PREFIX + userId);
        }
    }
}
