package com.nova.ai.service.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateRequest {

    @NotNull(message = "用户 ID 不能为空")
    private Long id;

    private String name;

    private String email;

    private String password;

    private String phone;

    private String avatar;

    private String group;

    private String role;

    private String tenant;
}
