package com.autoCustomer.dao;

import com.autoCustomer.entity.DeStageOrder;

public interface DeStageOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeStageOrder record);

    int insertSelective(DeStageOrder record);

    DeStageOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeStageOrder record);

    int updateByPrimaryKey(DeStageOrder record);
}