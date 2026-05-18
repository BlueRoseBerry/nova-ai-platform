package com.nova.ai.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 实体公共字段基类，实现 {@link Serializable} 供子类继承。
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean deleted;

    private String createUser;

    private String updateUser;

    private String createDate;

    private String updateDate;
}
