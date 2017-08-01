package com.autoCustomer.dao;

import java.util.Map;

import com.autoCustomer.entity.DeAcccountLevel;

public interface DeAcccountLevelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeAcccountLevel record);

    int insertSelective(DeAcccountLevel record);

    DeAcccountLevel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeAcccountLevel record);

    int updateByPrimaryKey(DeAcccountLevel record);
    
   //返回该订单的金额对应的客户级别
    String selectLevelByAccount(Map<String, Object> map);
    
    //如果没搜到对应的级别,看看是不是订单金额超过了金领的最大额
    String selectMaxLevel(Map<String, Object> map);
}