package com.autoCustomer.dao;

import java.util.List;
import java.util.Map;

import com.autoCustomer.entity.DeTagList;

public interface DeTagListMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeTagList record);

    int insertSelective(DeTagList record);

    DeTagList selectByPrimaryKey(Integer id);
    
    List<DeTagList> selectAllTagList(String accounttype);
    
    DeTagList selectTagListCheckDidHad(Map<String, Object> map);

    int updateByPrimaryKeySelective(DeTagList record);

    int updateByPrimaryKey(DeTagList record);
}