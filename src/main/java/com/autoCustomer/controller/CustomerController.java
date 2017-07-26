package com.autoCustomer.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.autoCustomer.service.AddcustomerService;
import com.autoCustomer.service.DePercentageService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/demo")
public class CustomerController {

	
	@Autowired
	private DePercentageService percentageService;
	
	@Autowired
	private AddcustomerService addcustomerService2;
	
	@RequestMapping(value="/createTime",method= RequestMethod.GET,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String excete(HttpServletResponse response) {
		JSONObject json = new JSONObject();
		//response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Content-Type", "application/xml; charset=UTF-8");  
		String createTime = percentageService.getRanCreateTime();
		System.out.println("createTime:"+createTime);
		for (int i = 0; i < 10; i++) {
			Map<String, Object> currentStages = percentageService.getCurrentStage();
			String meaage = currentStages.get("message").toString();
			System.out.println("客户状态是 "+meaage);
		}
		
		//System.out.println("currentStage:"+currentStage);
		String activeDate = percentageService.getActiveData();
		System.out.println("activeDate:"+activeDate);
		json.put("createTime", createTime);
	//	json.put("currentStage",currentStage);
		json.put("activeDate", activeDate);
		return json.toString();
	}
	
 
	
    /**
     * 自动创建客户
     * @param size
     * @return
     */
	@RequestMapping(value="/createCustomer/{username}/{size}",produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String createCustomer(@PathVariable("username")String username,@PathVariable("size")int size){
		JSONArray arr = new JSONArray();
		if(size <= 0){
			size = 1;
		}
		for (int i = 0; i < size; i++) { 
			
			String mes = addcustomerService2.addcustomer(username);
			arr.add(mes);
		}
		String retnemmes = arr.toString();
		
		return retnemmes;
	}
}
