package com.autoCustomer.dao;

import java.util.List;
import java.util.Map;

import com.autoCustomer.entity.TblPropertiesInfo;

public interface TblPropertiesInfoMapper {
    int deleteByPrimaryKey(Integer infoid);

    int insert(TblPropertiesInfo record);

    int insertSelective(TblPropertiesInfo record);

    TblPropertiesInfo selectByPrimaryKey(Integer infoid);

    int updateByPrimaryKeySelective(TblPropertiesInfo record);

    int updateByPrimaryKey(TblPropertiesInfo record);

	void updateValue(Map<String, String> pp);

	public List<String> selectPropertyInfoByKind(String kind);
}