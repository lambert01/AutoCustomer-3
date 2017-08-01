package com.autoCustomer.dao;

import java.util.Map;

import com.autoCustomer.entity.DeStageEventTargetRelation;

public interface DeStageEventTargetRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeStageEventTargetRelation record);

    int insertSelective(DeStageEventTargetRelation record);

    DeStageEventTargetRelation selectByPrimaryKey(Integer id);
    
    
    DeStageEventTargetRelation selectByEventidandUserid(Map<String, Object> map);

    int updateByPrimaryKeySelective(DeStageEventTargetRelation record);

    int updateByPrimaryKey(DeStageEventTargetRelation record);
}