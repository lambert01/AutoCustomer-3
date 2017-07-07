package com.autoCustomer.dao;

import java.util.List;

import com.autoCustomer.entity.DeImage;

public interface DeImageMapper {
    int deleteByPrimaryKey(Integer imageid);

    int insert(DeImage record);

    int insertSelective(DeImage record);

    DeImage selectByPrimaryKey(Integer imageid);
    
    List<DeImage> selectAllImage();

    int updateByPrimaryKeySelective(DeImage record);

    int updateByPrimaryKey(DeImage record);
}