package com.autocustomer.test;

import com.autoCustomer.util.MessageUtil;

public class NameAndSexText {
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			int sex = MessageUtil.getGender();
			String name =  MessageUtil.getChineseName(sex);
			String men = sex==2?"女":"男";
			System.out.println(name+":" +men);
			
			
		}
		
	}

}
