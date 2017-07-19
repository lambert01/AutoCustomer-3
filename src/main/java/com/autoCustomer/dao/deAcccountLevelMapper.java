package com.autoCustomer.dao;

import com.autoCustomer.entity.deAcccountLevel;

public interface deAcccountLevelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(deAcccountLevel record);

    int insertSelective(deAcccountLevel record);

    deAcccountLevel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(deAcccountLevel record);

    int updateByPrimaryKey(deAcccountLevel record);
    
   //返回该订单的金额对应的客户级别
    String selectLevelByAccount(Double account);
    
    //如果没搜到对应的级别,看看是不是订单金额超过了金领的最大额
    String selectMaxLevel(Double account);
}