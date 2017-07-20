package com.autoCustomer.dao;

import com.autoCustomer.entity.DeCityLevel;

public interface DeCityLevelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeCityLevel record);

    int insertSelective(DeCityLevel record);

    DeCityLevel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeCityLevel record);

    int updateByPrimaryKey(DeCityLevel record);
    
    String selectLevelBycity(String city);
}