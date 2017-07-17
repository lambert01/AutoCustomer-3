package com.autoCustomer.dao;

import java.util.List;

import com.autoCustomer.entity.DeStageEvent;

public interface DeStageEventMapper {
    int deleteByPrimaryKey(Integer eventid);

    int insert(DeStageEvent record);

    int insertSelective(DeStageEvent record);

    DeStageEvent selectByPrimaryKey(Integer eventid);

    int updateByPrimaryKeySelective(DeStageEvent record);

    int updateByPrimaryKey(DeStageEvent record);
    
    //查找所有符合客户当前以及之前状态对应的事件
    List<DeStageEvent> selectEventsByStage(Integer stageid);
    
    int selectUnnectagesize(Integer stageid);
}