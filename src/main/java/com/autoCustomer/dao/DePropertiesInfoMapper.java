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
	
	//查询kind对应的id
	public List<Integer> selectIdByValue(String value);
    
	
	//返回所有的字段
	DePropertiesInfo selectallPropertyInfoByKind(String kind);
	
    ///查询符合要求的kind和status对应的信息
	List<DePropertiesInfo> getPropertyInfoBymap(Map<String, Object> querymap);
}