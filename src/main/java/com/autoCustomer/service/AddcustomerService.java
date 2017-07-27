package com.autoCustomer.service;

public interface AddcustomerService {
	public String addcustomer(String username,String token);

	String getAccessToken(String username);

	String getPropertyInfo(String kind);

	String gettaglist(String access_token);

}
