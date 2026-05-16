package com.nova.ai.modelgateway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nova.ai.modelgateway.entity.LlmModelEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LlmModelMapper extends BaseMapper<LlmModelEntity> {
}
