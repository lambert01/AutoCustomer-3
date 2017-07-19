package com.autoCustomer.dao;

import com.autoCustomer.entity.deCityLevel;

public interface deCityLevelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(deCityLevel record);

    int insertSelective(deCityLevel record);

    deCityLevel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(deCityLevel record);

    int updateByPrimaryKey(deCityLevel record);
    
    String selectLevelBycity(String city);
}