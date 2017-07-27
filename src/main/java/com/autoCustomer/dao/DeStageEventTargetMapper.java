package com.autoCustomer.dao;

import java.util.List;

import com.autoCustomer.entity.DeStageEventTarget;

public interface DeStageEventTargetMapper {
    int deleteByPrimaryKey(Integer eventid);

    int insert(DeStageEventTarget record);

    int insertSelective(DeStageEventTarget record);

    DeStageEventTarget selectByPrimaryKey(Integer eventid);

    int updateByPrimaryKeySelective(DeStageEventTarget record);

    int updateByPrimaryKey(DeStageEventTarget record);
    
    //查询所有符合客户当前以及之前状态对应的事件
    List<DeStageEventTarget> selectEventsByStage(Integer stageid);
    
    //查询与状态无关的事件
    List<DeStageEventTarget> selectUnRelatedStageEvent();
     
    //查询相同阶段的多选事件有几个
	int selectCountSize(Integer ismultiselect);
	
	//查找指定阶段的事件
	List<DeStageEventTarget> selectEventstageEvent(Integer stageid);
}