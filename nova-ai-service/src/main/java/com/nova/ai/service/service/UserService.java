package com.nova.ai.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nova.ai.service.dto.user.UserCreateRequest;
import com.nova.ai.service.dto.user.UserLoginRequest;
import com.nova.ai.service.dto.user.UserLoginResponse;
import com.nova.ai.service.dto.user.UserPageRequest;
import com.nova.ai.service.dto.user.UserPageResponse;
import com.nova.ai.service.dto.user.UserUpdateRequest;
import com.nova.ai.service.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    public User createUser(UserCreateRequest userCreateRequest);

    public User getUserById(Long id);

    public List<User> getUserByUsername(String username);

    public User updateUser(UserUpdateRequest userUpdateRequest);

    public void deleteUser(Long id);

    public UserPageResponse getUserPage(UserPageRequest userPageRequest);

    UserLoginResponse login(UserLoginRequest request);

    void logout(String token);
}
