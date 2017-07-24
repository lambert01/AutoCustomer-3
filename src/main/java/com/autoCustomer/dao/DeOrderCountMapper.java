package com.autoCustomer.dao;

import com.autoCustomer.entity.DeOrderCount;

public interface DeOrderCountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeOrderCount record);

    int insertSelective(DeOrderCount record);

    DeOrderCount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeOrderCount record);

    int updateByPrimaryKey(DeOrderCount record);
    
    int selectAllOrderCount();
}