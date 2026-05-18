package com.nova.ai.service.dto.user;

import com.nova.ai.service.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class UserPageResponse {

    private Long total;

    private Long pages;

    private Long current;

    private Long pageSize;

    private List<User> records;
}
