package com.autocustomer.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.autoCustomer.util.SendUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class deleteCustomerTest {
	static String domian = "https://api.convertlab.com/";
	static String getaccessToken(){

		String url =domian+"security/accesstoken";
		String appid ="cl02d515d8356ee74";
		String sercet ="6b1d3f8573d3cb325b58cad72856b278715e2278";
		String retunrstr = SendUtils.sendGet(url,"grant_type=client_credentials&appid="+appid+"&secret="+sercet+"");
		// 发送get请求,通过appid和sercet获取accesstoken.
		// retunrstr ="{\"error_code\":0,\"access_token\":\"123\"}";
		JsonObject returnData = new JsonParser().parse(retunrstr).getAsJsonObject();
		System.out.println("调用accessToken   "+returnData);
		String codes = returnData.get("error_code").toString();
		String access_token = "";
		if ("0".equals(codes)){
			access_token = returnData.get("access_token").toString().replaceAll("\"", "");
		}
		return access_token;
	
		
	}
	
	static void deletecustomer() throws IOException{
		String accesstoken = getaccessToken();
		String queryurl =domian+"v1/customers";
		String json = SendUtils.sendGet(queryurl,"access_token="+accesstoken+"&rows=7000");
		JSONObject customerjson = JSONObject.fromObject(json);
		JSONArray arr = customerjson.getJSONArray("rows");
		System.out.println(arr.size());
		for (Object object : arr) {
			JSONObject cust = JSONObject.fromObject(object);
			String id = cust.get("id").toString();
			System.out.println(id);
			String deleteurl =domian+"v1/customers/"+id;
			  try {
				  doDelete(deleteurl,"access_token="+accesstoken);
			} catch (Exception e) {
				e.printStackTrace();
			}
//System.out.println("删除返回的结果"+str);
		}
	}
	
    public static void doDelete(String urlStr,String acctoken) throws Exception{  
        
            urlStr +="?"+acctoken;  
        System.out.println(urlStr);  
        URL url = new URL(urlStr);  
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
        conn.setDoOutput(true);  
        conn.setRequestMethod("DELETE");  
        //屏蔽掉的代码是错误的，java.net.ProtocolException: HTTP method DELETE doesn't support output  
/*      OutputStream os = conn.getOutputStream();      
        os.write(paramStr.toString().getBytes("utf-8"));      
        os.close();  */   
          
        if(conn.getResponseCode() ==200){  
            System.out.println("成功");  
        }else{  
            System.out.println(conn.getResponseCode());  
        }  
    } 
    
  
	public static void main(String[] args) {
		try {
			deletecustomer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
