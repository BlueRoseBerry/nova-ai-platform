package com.nova.ai.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nova.ai.common.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 用户实体，对应表 sys_user。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = "password")
@TableName("sys_user")
public class User extends BaseEntity {

    private static final long serialVersionUID = 102420483068L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String name;

    private String email;

    private String account;

    @JsonIgnore
    private String password;

    private String phone;

    private String avatar;

    @TableField("user_group")
    private String group;

    private String role;

    private String tenant;
}
