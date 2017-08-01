package com.autoCustomer.service;

import java.util.Map;

import com.autoCustomer.entity.DePropertiesInfo;

public interface AddcustomerService {

	String getAccessToken(String username);

	String getPropertyInfo(String kind);

	String gettaglist(String access_token,String accounttype);

	String addcustomer(Map<String, Object> paramap);


	DePropertiesInfo selectallPropertyInfoByKind(String username);

	DePropertiesInfo getPropertyInfoBymap(Map<String, Object> querymap);


}
