package com.autoCustomer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.autoCustomer.entity.DeUser;
import com.autoCustomer.service.UserService;

@Controller("/user")
public class UserController {
	 @Autowired
	 private UserService userservice;
	
	@RequestMapping("/chechuser")
	public String checkUser(DeUser user){
		boolean b = userservice.checkUser(user);
		return null;
		
	}

}
