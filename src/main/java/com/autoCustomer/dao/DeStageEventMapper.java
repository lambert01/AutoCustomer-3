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
    
    //查询所有符合客户当前以及之前状态对应的事件
    List<DeStageEvent> selectEventsByStage(Integer stageid);
    
    //查询与状态无关的事件
    List<DeStageEvent> selectUnRelatedStageEvent();
     
    //查询相同阶段的多选事件有几个
	int selectCountSize(Integer ismultiselect);
}