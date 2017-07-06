package com.autoCustomer.service.impl;

import org.springframework.stereotype.Service;

import com.autoCustomer.service.AddcustomerService;
import com.autoCustomer.util.SendUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONObject;
@Service("addcustomerService")
public class AddcustomerServiceImp implements AddcustomerService {

	public String addcustomer(String customer) {
		String accessToken = getAccessToken("cl02dd15a2228ee92", "ce2f7581f4203b257ed5687c2e2106c3978a93be");
		String url = "http://api.convertwork.cn/v1/customerandidentities?access_token="+accessToken;
		String retunrstr = SendUtils.post(url, customer);
		JSONObject returnobj = JSONObject.fromObject(retunrstr);
		String id = returnobj.get("id").toString();
		return id;

	}
	
	private String getAccessToken(String appid,String sercet) {
		String url ="http://api.convertwork.cn/security/accesstoken";
		String retunrstr = SendUtils.sendGet(url,"grant_type=client_credentials&appid="+appid+"&secret="+sercet+"");
		// 发送get请求,通过appid和sercet获取accesstoken.
		// retunrstr ="{\"error_code\":0,\"access_token\":\"123\"}";
		JsonObject returnData = new JsonParser().parse(retunrstr).getAsJsonObject();
		System.out.println("调用accessToken   "+returnData);
		String codes = returnData.get("error_code").toString();
		String access_token = "";
		if ("0".equals(codes)) {
			access_token = returnData.get("access_token").toString().replaceAll("\"", "");
		}
		return access_token;
	}

}
