package com.nova.ai.service.dto.user;

import lombok.Data;

@Data
public class UserLoginResponse {

    private String token;

    /** 令牌有效时长（秒） */
    private long expiresIn;

    private Long userId;

    private String account;

    private String name;

    private String role;

    private String tenant;
}
