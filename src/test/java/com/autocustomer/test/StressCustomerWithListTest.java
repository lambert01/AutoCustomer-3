package com.autocustomer.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import com.autoCustomer.util.SendUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * Created by house on 17-5-15.
 */
public class StressCustomerWithListTest {
	
	private static String domain ="https://api.convertlab.com/";
	private static String appid ="cl002115d78a615d2";
	private static String sercet ="509369090e4fd3fea899c6d15bc61740a41f81ed";
    public static void main(String[] args) throws ParseException{
    	long strt = System.currentTimeMillis();

        FileInputStream fis = null;
        FileInputStream fis2 = null;
        InputStreamReader isr = null;
        InputStreamReader isr2 = null;
        BufferedReader br = null;  //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
        BufferedReader br2 = null;  //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
        try {


         String str = "";
         String str2 = "";
         fis = new FileInputStream("D:\\customer.csv");// FileInputStream
         fis2 = new FileInputStream("D:\\event.csv");// FileInputStream
         // 从文件系统中的某个文件中获取字节
          isr = new InputStreamReader(fis,"gbk");// InputStreamReader 是字节流通向字符流的桥梁,
          isr2 = new InputStreamReader(fis2,"gbk");// InputStreamReader 是字节流通向字符流的桥梁,
          br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
          br2 = new BufferedReader(isr2);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
          int a = 0;
          Map<String, Object> customermap = new HashMap<String, Object>();
          Map<String,Object> customeridmap = new HashMap<String, Object>();
          String accessToken = getAccessToken();
         while ((str = br.readLine()) != null) {
         	 a = ++a;
         	 if(a==1){
         		 continue;
         	 }
         	String[] customers = str.split(",");
         	String identityValue = "";
         	String displayName = "";
         	String name = "";
         	String gender = "";
         	String birthday = "";
         	String dateJoin ="";
			try {
				 identityValue = customers[0].trim();
	         	 displayName = customers[1].trim();
	         	 name = customers[2].trim();
	         	 gender = customers[4].trim();
	         	 birthday = customers[9].trim();
				dateJoin = customers[36].replace("\"","").replace("/", "-").trim();
			} catch (Exception e) {
				continue;
			}
			
         	if(!checkTime(dateJoin)){
         		dateJoin = dateJoin+":00";
         	}
         	dateJoin = getwanttime(dateJoin);
         	customermap.put("identityValue", identityValue);
         	customermap.put("displayName", displayName);
         	customermap.put("name", name);
         	customermap.put("gender", gender);
         	customermap.put("birthday", birthday);
         	customermap.put("dateJoin", dateJoin);
         	JSONObject customer = getcustomer(customermap);
         	
         	String url = domain+"v1/customerandidentities?access_token=" + accessToken;
         	String retunrstr = SendUtils.post(url, customer.toString());
         	JSONObject returnobj = JSONObject.fromObject(retunrstr);
         	System.out.println("创建客户返回的json是"+retunrstr);
         	String customeid = returnobj.getString("id");
         	
         	customeridmap.put(identityValue,customeid);
 
         }
         
         
         
         int j = 0;
         Map<String, Object> checkrepeateventmap = new HashMap<String, Object>();
         Map<String, Object> enevtmap = new HashMap<String, Object>();
         
    	 Map<String, Object> eventlistmap = new HashMap<String, Object>();
		 eventlistmap.put("c_scanBeiJing4S", "北京中普4S到店扫码");
		 eventlistmap.put("c_getCoupon", "北京中普4S_领取优惠券");
		 eventlistmap.put("c_scanQingDaoCar", "青岛车展扫码");
		 eventlistmap.put("c_bookShiJia", "青岛车展扫码_预约试驾");
		 eventlistmap.put("c_daodianKeXiao", "青岛车展扫码_到店核销");
		 
		 Map<String, Object> listmap = getAllListId(accessToken);
         
         while ((str2 = br2.readLine()) != null) {
        	 j = ++j;
         	 if(j==1){
         		 continue;
         	 }
        	String[] events = str2.split(",");
        	String event ="";
        	String identityValue = "";
			try {
				event = events[2];
				identityValue = events[1]; // 通过这个关联客户与事件
			} catch (Exception e1){
				continue;
			}
        	
        	boolean b = checkRepaet(event, identityValue, checkrepeateventmap); //查看是否重复
        	if(b){
        		continue;
        	}
 
        	
        	Set<String> keys = customeridmap.keySet();
        	if(!keys.contains(identityValue)){
        		//System.out.println("没有客户对应的事件");
        		continue;
        	}
        	for (String mapidentityValue : keys) {
        		if(mapidentityValue.equals(identityValue)){
        			String customerid= customeridmap.get(mapidentityValue).toString();
        			enevtmap.put("customerId", customerid);
        		}
			}
        	
        	
        	enevtmap.put("event", event);
        	String date = events[5].replace("\"","").replace("/", "-").trim();
         	if(!checkTime(date)){
         		date = date+":00";
         	}
         	date = getwanttime(date);
        	enevtmap.put("date", date);
        	
        	
        	try {
        		String source = events[6];
            	enevtmap.put("source", source);
            	String campaignName = events[11];
				enevtmap.put("campaignName", campaignName);
			} catch (Exception e) {
				continue;
			}
        	String returnstr = createCustomerEvent(enevtmap, accessToken,eventlistmap,listmap);
        	System.out.println("创建事件返回的json是"+returnstr);
        	 
         }
         
         //事件执行完了
        
        } catch (FileNotFoundException e) {
         System.out.println("找不到指定文件");
        } catch (IOException e) {
         System.out.println("读取文件失败");
        } finally {
         try {
           br.close();
           br2.close();
           isr.close();
           isr2.close();
           fis.close();
           fis2.close();
          // 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
         } catch (IOException e) {
          e.printStackTrace();
         }
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时"+((end-strt)/1000)+"秒");
       
    }
    
	public static JSONObject getcustomer(Map<String, Object> map){
		JSONObject data = new JSONObject();

		JSONObject custidentitie = new JSONObject();
		JSONObject cust = new JSONObject();

		cust.put("dateJoin", map.get("dateJoin"));
		cust.put("name",map.get("name"));
		cust.put("gender",map.get("gender"));
		JSONArray identitiesarr = new JSONArray();
		for(int i = 0;i < 1;i++){
			custidentitie.put("identityType","ID");
			custidentitie.put("identityValue",map.get("identityValue"));
			identitiesarr.add(custidentitie);
		}

		data.put("customerIdentities",identitiesarr);
		data.put("customer",cust);
		return data;
	}
	
	private static String getAccessToken(){
		String url =  domain+"security/accesstoken";
		String retunrstr = SendUtils.sendGet(url,"grant_type=client_credentials&appid="+appid+"&secret="+sercet+"");
		JsonObject returnData = new JsonParser().parse(retunrstr).getAsJsonObject();
		String codes = returnData.get("error_code").toString();
		String access_token = "";
		if("0".equals(codes)){
			access_token = returnData.get("access_token").toString().replaceAll("\"","");
		}
		return access_token;
	}
	
	//获得所有的群组
	public static Map<String, Object> getAllListId(String token){
		Map<String, Object> listmap = new HashMap<String, Object>();
		String url = domain + "v1/lists";
		String json = SendUtils.sendGet(url,"access_token="+token);
		JSONObject listsjson = JSONObject.fromObject(json);
		JSONArray arr = listsjson.getJSONArray("rows");
		for (Object object : arr) {
			JSONObject list = JSONObject.fromObject(object);
			Integer id = (Integer)list.get("id");
			String name =(String)list.get("name");
			listmap.put(name, id);
			
		}
		return listmap;
		
	}
	
	//把客户添加到群组中
	public static String createCustomerList(String customerId,Integer listId,String token){
		String url = domain + "/v1/listMembers" + "?access_token=" + token;
		JSONObject data = new JSONObject();
		JSONObject list = new JSONObject();
		data.put("customerId", customerId);
		data.put("listId", listId);
		list.put("data", data);
		String returnCode = SendUtils.post(url, list.toString());
		return returnCode;
	
		
	}
	
	
	
	public static String createCustomerEvent(Map<String, Object> map,String access_token,Map<String, Object> eventlistmap,Map<String, Object> listmap){

		String url =  domain+"v1/customerevents?access_token=" + access_token;
			JSONObject obj = new JSONObject();
			String customerId = (String)map.get("customerId");
			String event = (String)map.get("event");
			String campaignName = (String)map.get("campaignName");
			obj.put("customerId", map.get("customerId"));
			obj.put("date", map.get("date"));
			obj.put("event", map.get("event"));
			obj.put("source", map.get("source"));
			obj.put("campaignName", map.get("campaignName"));
			String returnstr = SendUtils.post(url, obj.toString());
			
			//开始加入群组
			
			String listname = (String)eventlistmap.get(event);
			Integer listid =null;
			if(!(listname == null) && !(listname.equals(""))){
				 listid = (Integer)listmap.get(listname);
			}
			if(event.equals("c_liuzi")&& campaignName.equals("荣威4S到店试驾活动")){
				listid = (Integer)listmap.get("北京中普4S_留资");
			}
			
			if(event.equals("c_liuzi")&& campaignName.equals("青岛汽车展活动")){
				listid = (Integer)listmap.get("青岛车展扫码_留资");
			}
			if(!(listid == null)){
				String listresult = createCustomerList(customerId, listid,access_token);
				System.out.println("创建群组返回的是"+listresult);
			}
			
			return returnstr;
	}
	
    public static boolean checkTime(String date){
    	boolean b = true;
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	try {
			df.format(date);
		} catch (Exception e) {
			b = false;
		}
    	return b;
    }
    
    public static String getwanttime(String time){
		 SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		 SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 df1.setTimeZone(TimeZone.getTimeZone("UTC"));
		 Date date = null;
		 try {
			  date = df2.parse(time);
		} catch (ParseException e) {
			date = new Date();
		}
		 time = df1.format(date);
		 return time;
    }
    
    public static boolean checkRepaet(String event,String identityValue,Map<String, Object> checkrepeateventmap){
 
    	String identityValueAndevent = identityValue+","+event;
    	Set<String> repeatkeys = checkrepeateventmap.keySet();
    	if(repeatkeys.contains(identityValueAndevent)){ 
    		return true;
    	}else{
    		checkrepeateventmap.put(identityValueAndevent, event);
    		return false;
    	}
     
    	
    }
 
    
}
