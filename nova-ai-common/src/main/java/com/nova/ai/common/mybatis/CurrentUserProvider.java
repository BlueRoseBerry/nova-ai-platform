package com.nova.ai.common.mybatis;

/**
 * 提供当前操作用户标识，供 MyBatis-Plus 自动填充 createUser / updateUser。
 * <p>各业务模块可实现此接口并从登录上下文解析用户；未提供实现时默认使用 {@code system}。</p>
 */
@FunctionalInterface
public interface CurrentUserProvider {

    String currentUser();
}
