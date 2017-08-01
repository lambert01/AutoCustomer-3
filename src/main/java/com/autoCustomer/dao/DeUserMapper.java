package com.autoCustomer.dao;

import com.autoCustomer.entity.DeUser;

public interface DeUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeUser record);

    int insertSelective(DeUser record);

    DeUser selectByPrimaryKey(Integer id);
    
    DeUser selectUserWhetherHave(DeUser user);

    int updateByPrimaryKeySelective(DeUser record);

    int updateByPrimaryKey(DeUser record);
}