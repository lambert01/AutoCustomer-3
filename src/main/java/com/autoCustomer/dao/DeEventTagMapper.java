package com.autoCustomer.dao;

import java.util.List;

import com.autoCustomer.entity.DeEventTag;

public interface DeEventTagMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeEventTag record);

    int insertSelective(DeEventTag record);

    DeEventTag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeEventTag record);

    int updateByPrimaryKey(DeEventTag record);
    
    List<DeEventTag> selectAllEventTag(String accounttype);
}