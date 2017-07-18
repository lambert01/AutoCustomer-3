package com.autoCustomer.dao;

import java.util.List;

import com.autoCustomer.entity.DeProducts;

public interface DeProductsMapper {
    int deleteByPrimaryKey(Integer productid);

    int insert(DeProducts record);

    int insertSelective(DeProducts record);

    DeProducts selectByPrimaryKey(Integer productid);

    int updateByPrimaryKeySelective(DeProducts record);

    int updateByPrimaryKey(DeProducts record);
    
   List<DeProducts> selectProduct();
}