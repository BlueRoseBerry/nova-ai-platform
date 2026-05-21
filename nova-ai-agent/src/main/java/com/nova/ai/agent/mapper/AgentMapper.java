package com.nova.ai.agent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nova.ai.agent.model.Agent;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AgentMapper extends BaseMapper<Agent> {
}