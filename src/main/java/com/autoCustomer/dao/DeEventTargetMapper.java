package com.autoCustomer.dao;

import java.util.Map;

import com.autoCustomer.entity.DeEventTarget;

public interface DeEventTargetMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeEventTarget record);

    int insertSelective(DeEventTarget record);

    DeEventTarget selectByPrimaryKey(Integer id);
    
    //通过eventid和secretid找到对应的事件的targetid和targetname
    DeEventTarget selectByEventIdAndserretId(Map<String, Object> map);

    int updateByPrimaryKeySelective(DeEventTarget record);

    int updateByPrimaryKey(DeEventTarget record);
}