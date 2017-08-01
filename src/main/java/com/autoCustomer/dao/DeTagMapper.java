package com.autoCustomer.dao;

import java.util.List;
import java.util.Map;

import com.autoCustomer.entity.DeTag;

public interface DeTagMapper {
    int deleteByPrimaryKey(Integer tagid);

    int insert(DeTag record);

    int insertSelective(DeTag record);

    DeTag selectByPrimaryKey(Integer tagid);
    
    //搜索所有的标签
    List<DeTag> selectAllTag(String accounttype);
    
    //搜索所有的有相同分组关系的标签
    List<DeTag> selectAllTagHavingSameDemension(Map<String, Object> map);

    int updateByPrimaryKeySelective(DeTag record);
    
    //查询有互斥关系的标签的数量
    int selectCountSize(Map<String, Object> map);

    int updateByPrimaryKey(DeTag record);
    
}