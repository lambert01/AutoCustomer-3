package com.autoCustomer.service;

import com.autoCustomer.entity.DeUser;

public interface UserService {
	boolean checkUser(DeUser user);
	
	void updateUser(DeUser user);

}
