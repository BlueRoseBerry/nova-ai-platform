package com.nova.ai.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nova.ai.common.exception.ErrorCode;
import com.nova.ai.common.exception.NovaAiException;
import com.nova.ai.service.auth.AuthTokenStore;
import com.nova.ai.service.dto.user.UserCreateRequest;
import com.nova.ai.service.dto.user.UserLoginRequest;
import com.nova.ai.service.dto.user.UserLoginResponse;
import com.nova.ai.service.dto.user.UserPageRequest;
import com.nova.ai.service.dto.user.UserPageResponse;
import com.nova.ai.service.dto.user.UserUpdateRequest;
import com.nova.ai.service.entity.User;
import com.nova.ai.service.mapper.UserMapper;
import com.nova.ai.service.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final AuthTokenStore authTokenStore;

    public UserServiceImpl(PasswordEncoder passwordEncoder, AuthTokenStore authTokenStore) {
        this.passwordEncoder = passwordEncoder;
        this.authTokenStore = authTokenStore;
    }

    @Override
    public User createUser(UserCreateRequest userCreateRequest) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccount, userCreateRequest.getAccount());
        if (baseMapper.selectCount(queryWrapper) > 0) {
            throw new NovaAiException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User newUser = new User();
        BeanUtils.copyProperties(userCreateRequest, newUser);
        newUser.setPassword(encodePassword(userCreateRequest.getPassword()));
        newUser.setDeleted(false);
        LocalDateTime currentDate = LocalDateTime.now();
        newUser.setCreateDate(currentDate.toString());
        baseMapper.insert(newUser);
        return newUser;
    }

    @Override
    public User getUserById(Long id) {
        User user = baseMapper.selectById(id);
        if (user == null) {
            throw new NovaAiException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    @Override
    public List<User> getUserByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getName, username);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public User updateUser(UserUpdateRequest userUpdateRequest) {
        User user = baseMapper.selectById(userUpdateRequest.getId());
        if (user == null) {
            throw new NovaAiException(ErrorCode.USER_NOT_FOUND);
        }
        applyUpdate(userUpdateRequest, user);
        user.setUpdateDate(Instant.now().toString());
        baseMapper.updateById(user);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        User user = baseMapper.selectById(id);
        if (user == null) {
            throw new NovaAiException(ErrorCode.USER_NOT_FOUND);
        }
        baseMapper.deleteById(id);
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccount, request.getAccount());
        User user = baseMapper.selectOne(queryWrapper);
        if (user == null
            || user.getPassword() == null
            || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new NovaAiException(ErrorCode.INVALID_CREDENTIALS);
        }

        String token = authTokenStore.createSession(user.getId());

        UserLoginResponse response = new UserLoginResponse();
        response.setToken(token);
        response.setExpiresIn(authTokenStore.tokenTtl().toSeconds());
        response.setUserId(user.getId());
        response.setAccount(user.getAccount());
        response.setName(user.getName());
        response.setRole(user.getRole());
        response.setTenant(user.getTenant());
        return response;
    }

    @Override
    public void logout(String token) {
        authTokenStore.revoke(token);
    }

    @Override
    public UserPageResponse getUserPage(UserPageRequest userPageRequest) {
        int current = userPageRequest.getCurrent() != null && userPageRequest.getCurrent() > 0
            ? userPageRequest.getCurrent() : 1;
        int pageSize = userPageRequest.getPageSize() != null && userPageRequest.getPageSize() > 0
            ? userPageRequest.getPageSize() : 10;

        Page<User> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(User::getId);

        IPage<User> result = baseMapper.selectPage(page, wrapper);

        UserPageResponse response = new UserPageResponse();
        response.setTotal(result.getTotal());
        response.setPages(result.getPages());
        response.setCurrent(result.getCurrent());
        response.setPageSize(result.getSize());
        response.setRecords(result.getRecords());
        return response;
    }

    private void applyUpdate(UserUpdateRequest request, User user) {
        if (StringUtils.hasText(request.getName())) {
            user.setName(request.getName());
        }
        if (StringUtils.hasText(request.getEmail())) {
            user.setEmail(request.getEmail());
        }
        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(encodePassword(request.getPassword()));
        }
        if (StringUtils.hasText(request.getPhone())) {
            user.setPhone(request.getPhone());
        }
        if (StringUtils.hasText(request.getAvatar())) {
            user.setAvatar(request.getAvatar());
        }
        if (StringUtils.hasText(request.getGroup())) {
            user.setGroup(request.getGroup());
        }
        if (StringUtils.hasText(request.getRole())) {
            user.setRole(request.getRole());
        }
        if (StringUtils.hasText(request.getTenant())) {
            user.setTenant(request.getTenant());
        }
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
