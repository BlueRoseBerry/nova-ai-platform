package com.nova.ai.service.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCreateRequest {

    private String name;

    private String email;

    @NotBlank(message = "账号不能为空")
    private String account;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String phone;

    private String avatar;

    private String group;

    private String role;

    private String tenant;
}
