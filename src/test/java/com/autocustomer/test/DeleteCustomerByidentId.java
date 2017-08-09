package com.autocustomer.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.autoCustomer.util.SendUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * Created by house on 17-5-15.
 */
public class DeleteCustomerByidentId {
    public static void main(String[] args) throws ParseException{
    	long strt = System.currentTimeMillis();

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;  //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
        try {


         String str = "";
         fis = new FileInputStream("D:\\customer.csv");// FileInputStream
         // 从文件系统中的某个文件中获取字节
          isr = new InputStreamReader(fis,"gbk");// InputStreamReader 是字节流通向字符流的桥梁,
          br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
          int a = 0;
          List<String> arrs = new ArrayList<String>();
          
          String accessToken = getAccessToken();
         while ((str = br.readLine()) != null) {
         	 a = ++a;
         	 if(a==1){
         		 continue;
         	 }
         	String[] customers = str.split(",");
         	String identityValue ="";
			try {
				identityValue = customers[0].trim();
				
			} catch (Exception e) {
				continue;
			}
			
			arrs.add(identityValue);
         	 
         //	customeridmap.put(identityValue,customeid);
 
         }
         getcustomerids(arrs, accessToken);
       
        
        } catch (FileNotFoundException e) {
         System.out.println("找不到指定文件");
        } catch (IOException e) {
         System.out.println("读取文件失败");
        } finally {
         try {
           br.close();
           isr.close();
           fis.close();
          // 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
         } catch (IOException e) {
          e.printStackTrace();
         }
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时"+((end-strt)/1000)+"秒");
       
    }
    
	
	private static String getAccessToken(){
		String appid ="cl002115d78a615d2";
		String sercet ="509369090e4fd3fea899c6d15bc61740a41f81ed";
		String url =  "https://api.convertlab.com/security/accesstoken";
		String retunrstr = SendUtils.sendGet(url,"grant_type=client_credentials&appid="+appid+"&secret="+sercet+"");
		// 发送get请求,通过appid和sercet获取accesstoken.
		// retunrstr ="{\"error_code\":0,\"access_token\":\"123\"}";
		JsonObject returnData = new JsonParser().parse(retunrstr).getAsJsonObject();
		String codes = returnData.get("error_code").toString();
		String access_token = "";
		if("0".equals(codes)){
			access_token = returnData.get("access_token").toString().replaceAll("\"","");
		}
		return access_token;
	}
	
	public static void getcustomerids(List<String> lists,String accesstoken){
        String domian ="https://api.convertlab.com/";
		String queryurl =domian+"v1/customeridentities";


		for (String identityValue : lists) {
			String json = SendUtils.sendGet(queryurl,"access_token="+accesstoken+"&identityValue="+identityValue);
			JSONArray jsonarr = JSONArray.fromObject(json);
		 	for (Object object : jsonarr) {
				JSONObject customerjson = JSONObject.fromObject(object);
				System.out.println("return "+customerjson);
				String customeid = customerjson.get("customerId").toString();
				String deleteurl =domian+"v1/customers/"+customeid;
				  try {
					  doDelete(deleteurl,"access_token="+accesstoken);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
		}

	}
	
    public static void doDelete(String urlStr,String acctoken) throws Exception{  
        
        urlStr +="?"+acctoken;  
    System.out.println(urlStr);  
    URL url = new URL(urlStr);  
    HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
    conn.setDoOutput(true);  
    conn.setRequestMethod("DELETE");    
      
    if(conn.getResponseCode() ==200){  
        System.out.println("成功");  
    }else{  
        System.out.println(conn.getResponseCode());  
    }  
} 
  
}
