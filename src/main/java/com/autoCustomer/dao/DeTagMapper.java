package com.autoCustomer.dao;

import java.util.List;

import com.autoCustomer.entity.DeTag;

public interface DeTagMapper {
    int deleteByPrimaryKey(Integer tagid);

    int insert(DeTag record);

    int insertSelective(DeTag record);

    DeTag selectByPrimaryKey(Integer tagid);
    
    List<DeTag> selectAllTag();

    int updateByPrimaryKeySelective(DeTag record);

    int updateByPrimaryKey(DeTag record);
}