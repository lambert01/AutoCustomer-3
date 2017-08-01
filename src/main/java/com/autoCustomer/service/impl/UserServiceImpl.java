package com.autoCustomer.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.autoCustomer.dao.DeUserMapper;
import com.autoCustomer.entity.DeUser;
import com.autoCustomer.service.UserService;
@Service("uservice")
public class UserServiceImpl implements UserService{
	
	@Resource
	private DeUserMapper userdao;

	@Override
	public boolean checkUser(DeUser user) {
		DeUser returnuser = userdao.selectUserWhetherHave(user);
		if(returnuser != null){
		return true;	
		}
		return false;
	}

	@Override
	public void updateUser(DeUser user) {
		 userdao.updateByPrimaryKey(user);
		 
	}

}
