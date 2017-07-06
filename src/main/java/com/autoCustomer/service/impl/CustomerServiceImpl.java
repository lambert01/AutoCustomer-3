package com.autoCustomer.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.autoCustomer.dao.APIMapper;
import com.autoCustomer.entity.Api;
import com.autoCustomer.service.CustomerService;

@Service("customerServiceImpl")
public class CustomerServiceImpl implements CustomerService{
	
	@Resource
	private APIMapper apiMapper;
	
	public List<Api> selectApi() {
		return apiMapper.selectApi();
	}
}
