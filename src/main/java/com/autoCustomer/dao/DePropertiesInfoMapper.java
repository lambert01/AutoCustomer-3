package com.autoCustomer.dao;

import java.util.List;
import java.util.Map;

import com.autoCustomer.entity.DePropertiesInfo;

public interface DePropertiesInfoMapper {
    int deleteByPrimaryKey(Integer infoid);

    int insert(DePropertiesInfo record);

    int insertSelective(DePropertiesInfo record);

    DePropertiesInfo selectByPrimaryKey(Integer infoid);

    int updateByPrimaryKeySelective(DePropertiesInfo record);

    int updateByPrimaryKey(DePropertiesInfo record);

	void updateValue(Map<String, String> pp);

	public List<String> selectPropertyInfoByKind(String kind);
}