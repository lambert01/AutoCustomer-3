package com.autoCustomer.dao;

import java.util.List;
import java.util.Map;


public interface DePercentageMapper {
	
	List<Map<String,Object>> selectByType(Map<String, Object> map);
	
}
