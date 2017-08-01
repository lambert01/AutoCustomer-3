package com.autoCustomer.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.autoCustomer.entity.DePropertiesInfo;
import com.autoCustomer.service.AddcustomerService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/demo")
public class CustomerController {
	
	@Autowired
	private AddcustomerService addcustomerService;
 
    /**
     * 自动创建客户
     * @param size
     * @return
     */
	@RequestMapping(value="/createCustomer/{username}/{size}",produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String createCustomer(@PathVariable("username")String username,@PathVariable("size")int size){
		JSONObject returnjson = new JSONObject();
		
		if(size <= 0){
			returnjson.put("error", "至少创建一个用户");
			return returnjson.toString();
		}
		
		String  token = addcustomerService.getAccessToken(username);
		
		if(token == null || "".equals(token)){
			returnjson.put("error", "没有对应的token");
			return returnjson.toString();
		}
		
		DePropertiesInfo info = addcustomerService.selectallPropertyInfoByKind(username);
		String accounttype = info.getStatus();
		Integer userid = info.getInfoid();
		if(accounttype == null || "".equals(accounttype)){
			returnjson.put("error", "帐号没有配置对应的类型");
			return returnjson.toString();
		}
		
		//addcustomerService.gettaglist(token,accounttype);
		 
		Map<String, Object> paramap = new HashMap<String, Object>();
		paramap.put("username", username); //用户名
		paramap.put("accounttype", accounttype);//帐号类型
		paramap.put("accessToken", token);
		paramap.put("size", size); //创建几个用户
		paramap.put("userid", userid); //帐号的id,数据库中没有保存帐号,用唯一的appid的id替代帐号的唯一性
		String mes = addcustomerService.addcustomer(paramap);
 
		return mes;
	}
}
