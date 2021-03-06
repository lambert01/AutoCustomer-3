package com.autocustomer.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.autoCustomer.util.SendUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class deleteTimeCustomerTest {
	static String domian = "https://api.convertlab.com/";
	static String getaccessToken(){

		String url =domian+"security/accesstoken";
		String appid ="cl002115d78a615d2";
		String sercet ="509369090e4fd3fea899c6d15bc61740a41f81ed";
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
	
	static void deletecustomer(String starttime,String endtime) throws IOException{
		String accesstoken = getaccessToken();
		String queryurl =domian+"v1/customers";
		String param = "access_token="+accesstoken+"&dateCreatedFrom="+starttime+"&dateCreatedTo="+endtime+"&rows="+106;
		String json = SendUtils.sendGet(queryurl,param);
		JSONObject customerjson = JSONObject.fromObject(json);
		JSONArray arr = customerjson.getJSONArray("rows");
		System.out.println(arr.size());
		return;
/*		for (Object object : arr) {
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
		}*/
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
    
    
  public static String transTime(String time){
	  SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	  SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  df1.setTimeZone(TimeZone.getTimeZone("UTC"));
	  Date date = null;
	  try {
		date = df2.parse(time);
		time = df1.format(date);
	} catch (ParseException e) {
	}
	  
	  return time;
	  
  }
	public static void main(String[] args) {
		try {
			//时间格式 2016-11-11 11:11:11
			String starttime = "2017-08-01 00:00:00";
			String endtime = "2017-08-09 16:50:00";
			
			starttime = transTime(starttime);
			endtime = transTime(endtime);
			deletecustomer(starttime,endtime);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
