package com.nova.ai.service.controller;

import com.nova.ai.common.response.BaseResponse;
import com.nova.ai.service.dto.user.UserCreateRequest;
import com.nova.ai.service.dto.user.UserLoginRequest;
import com.nova.ai.service.dto.user.UserLoginResponse;
import com.nova.ai.service.dto.user.UserPageRequest;
import com.nova.ai.service.dto.user.UserPageResponse;
import com.nova.ai.service.dto.user.UserUpdateRequest;
import com.nova.ai.service.entity.User;
import com.nova.ai.service.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public BaseResponse<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest request) {
        log.info("User login, account: {}", request.getAccount());
        return BaseResponse.success(userService.login(request));
    }

    @PostMapping("/logout")
    public BaseResponse<Void> logout(
        @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        userService.logout(extractBearerToken(authorization));
        return BaseResponse.success(null);
    }

    @PostMapping("/create")
    public BaseResponse<User> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        log.info("Create user, account: {}", userCreateRequest.getAccount());
        return BaseResponse.success(userService.createUser(userCreateRequest));
    }

    @GetMapping("/get/{id}")
    public BaseResponse<User> getUserById(@PathVariable Long id) {
        return BaseResponse.success(userService.getUserById(id));
    }

    @PostMapping("/update")
    public BaseResponse<User> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        log.info("Update user, id: {}", userUpdateRequest.getId());
        return BaseResponse.success(userService.updateUser(userUpdateRequest));
    }

    @PostMapping("/delete/{id}")
    public BaseResponse<Void> deleteUser(@PathVariable Long id) {
        log.info("Delete user, id: {}", id);
        userService.deleteUser(id);
        return BaseResponse.success(null);
    }

    @GetMapping("/page")
    public BaseResponse<UserPageResponse> getUserPage(@Valid UserPageRequest userPageRequest) {
        return BaseResponse.success(userService.getUserPage(userPageRequest));
    }

    private String extractBearerToken(String authorization) {
        if (authorization == null || authorization.isBlank()) {
            return null;
        }
        if (authorization.startsWith("Bearer ")) {
            return authorization.substring(7).trim();
        }
        return authorization.trim();
    }
}
