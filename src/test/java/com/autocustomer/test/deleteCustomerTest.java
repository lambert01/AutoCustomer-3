package com.autocustomer.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.autoCustomer.util.SendUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class deleteCustomerTest {
	static String getaccessToken(){

		String domain ="http://api.convertwork.cn";
		String url =domain+"/security/accesstoken";
		String appid ="cl009c15d31412af3";
		String sercet ="bd348212d1e03e92916c14e6910dcef4a9e55343";
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
		String queryurl ="http://api.convertwork.cn/v1/customers";
		String json = SendUtils.sendGet(queryurl,"access_token="+accesstoken+"&rows=759");
		JSONObject customerjson = JSONObject.fromObject(json);
		JSONArray arr = customerjson.getJSONArray("rows");
		System.out.println(arr.size());
		for (Object object : arr) {
			JSONObject cust = JSONObject.fromObject(object);
			String id = cust.get("id").toString();
			System.out.println(id);
			String deleteurl ="http://api.convertwork.cn/v1/customers/"+id;
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
    
    private static String deleteurl(List<String> list,String url1) throws IOException {  
        
        URL url = new URL(url1);  
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
        connection.setRequestMethod("DELETE");  
        connection.setDoInput(true);  
        connection.setDoOutput(true);  
        connection.setRequestProperty("name", "robben");  
        connection.setRequestProperty("content-type", "text/html");  
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "8859_1");  
        // 将要传递的集合转换成JSON格式  
        JSONArray jsonArray = JSONArray.fromObject(list);  
        // 组织要传递的参数  
        out.write("" + jsonArray);  
        out.flush();  
        out.close();  
        // 获取返回的数据  
        BufferedReader in = new BufferedReader(new InputStreamReader(  
                connection.getInputStream()));  
        String line = null;  
        StringBuffer content = new StringBuffer();  
        while ((line = in.readLine()) != null) {  
            // line 为返回值，这就可以判断是否成功  
            content.append(line);  
        }  
        in.close();  
        return content.toString();  
    }  
  
	public static void main(String[] args) {
		try {
			deletecustomer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
