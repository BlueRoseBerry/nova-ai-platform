package com.nova.ai.common.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.nova.ai.common.mybatis.CurrentUserProvider;
import java.time.LocalDateTime;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

/**
 * {@link com.nova.ai.common.model.BaseEntity} 公共字段自动填充。
 */
@Component
public class NovaMetaObjectHandler implements MetaObjectHandler {

    private static final String DEFAULT_USER = "system";

    private final ObjectProvider<CurrentUserProvider> currentUserProvider;

    public NovaMetaObjectHandler(ObjectProvider<CurrentUserProvider> currentUserProvider) {
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        String operator = resolveCurrentUser();
        strictInsertFill(metaObject, "createDate", LocalDateTime.class, now);
        strictInsertFill(metaObject, "updateDate", LocalDateTime.class, now);
        strictInsertFill(metaObject, "createUser", String.class, operator);
        strictInsertFill(metaObject, "updateUser", String.class, operator);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        strictUpdateFill(metaObject, "updateDate", LocalDateTime.class, LocalDateTime.now());
        strictUpdateFill(metaObject, "updateUser", String.class, resolveCurrentUser());
    }

    private String resolveCurrentUser() {
        return currentUserProvider.getIfAvailable(() -> () -> DEFAULT_USER).currentUser();
    }
}
