package com.autoCustomer.service;

import java.util.Map;

public interface DePercentageService {

	//获取线下活动数据
	String getActiveData();
	
	//获取随机创建时间
	String getRanCreateTime();
	
	
	String frontPercentage();
	
	//获取当前阶段数据
	 Map<String,Object> getCurrentStage();
}
