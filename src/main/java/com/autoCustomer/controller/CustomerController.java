package com.autoCustomer.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.autoCustomer.service.AddcustomerService;
import com.autoCustomer.service.CustomerService;
import com.autoCustomer.service.DePercentageService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/demo")
public class CustomerController {
	
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private DePercentageService percentageService;
	
	@Autowired
	private AddcustomerService addcustomerService;
	
	@RequestMapping(value="/createTime",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String excete(HttpServletResponse response) throws UnsupportedEncodingException{
		JSONObject json = new JSONObject();
		//response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Content-Type", "application/xml; charset=UTF-8");  
		String createTime = percentageService.getRanCreateTime();
		System.out.println("createTime:"+createTime);
		String currentStage = percentageService.getCurrentStage();
		System.out.println("currentStage:"+currentStage);
		String activeDate = percentageService.getActiveData();
		System.out.println("activeDate:"+activeDate);
		json.put("createTime", createTime);
		json.put("currentStage",currentStage);
		json.put("activeDate", activeDate);
		return json.toString();
	}
	
	
	/**
	 * 联调创建客户信息
	 * @param data
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/pushData",method= RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String createCustomer(HttpServletRequest request,@RequestBody String data) throws UnsupportedEncodingException{
		request.setCharacterEncoding("utf-8");
		
		System.out.println("参数是 "+data);
		if(data == null || "".equals(data)){
			JSONObject json = new JSONObject();
			json.put("code","error");
			json.put("errMsg", "入参为空");
			return json.toString();
		}
		String message = addcustomerService.addcustomer(data);
		return message;
	}
	
	/**
	 * 自动创建客户,客户的创建时间调用方法返回符合要求的时间
	 */
	@RequestMapping("/createCustomer")
	public void createCustomer(){
		JSONObject obj = addcustomerService.getcustomer();
		String dateJoin = percentageService.getRanCreateTime();
		JSONObject customer = (JSONObject)obj.get("customer");
	 	customer.put("dateCreated", dateJoin);
		customer.put("dateJoin", dateJoin); 
		System.out.println("obj.toString() "+obj.toString());
		addcustomerService.addcustomer(obj.toString());
		
		
	}
}
