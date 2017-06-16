package com.autoCustomer.demo;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.autoCustomer.util.ImageUtil;
import com.autoCustomer.util.LocalUtil2;
import com.autoCustomer.util.MessageUtil;
import com.autoCustomer.util.SendUtils;
import com.autoCustomer.util.TagUtil;
import com.autoCustomer.util.TimeUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CustomerTest {
	private static final String URL = "http://api.convertwork.cn/";

	public static void main(String[] args) throws ParseException {
		//for (int i = 0; i < 30; i++) {
			String accessToken = getAccessToken("cl02dd15a2228ee92", "ce2f7581f4203b257ed5687c2e2106c3978a93be");
			//String accessToken = getAccessToken("cl009a15c876eb2d1", "719a2b55d7deb618c00620fcb9542da76788205f");
			// String customerId =  sendCustomer(accessToken);
			// System.out.println(customerId);
			// addCustomerTag(customerId,accessToken);
			// createCustomerEvent(customerId,accessToken);
			String listid = createList(accessToken);
			String code = updateList(listid,accessToken);
			System.out.println("code is "+code);
			
		//}
	

	}

	/**
	 * 获得accesstolen
	 * @param appid
	 * @param secret
	 * @return
	 */
	public static String getAccessToken(String appid,String sercet) {
		String url = URL + "security/accesstoken";
		String retunrstr = SendUtils.sendGet(url,"grant_type=client_credentials&appid="+appid+"&secret="+sercet+"");
		// 发送get请求,通过appid和sercet获取accesstoken.
		// retunrstr ="{\"error_code\":0,\"access_token\":\"123\"}";
		JsonObject returnData = new JsonParser().parse(retunrstr).getAsJsonObject();
		String codes = returnData.get("error_code").toString();
		String access_token = "";
		if ("0".equals(codes)) {
			access_token = returnData.get("access_token").toString().replaceAll("\"", "");
		}
		return access_token;
	}

	/**
	 * 创建符合格式要求的客户json字符串
	 * @param openid
	 * @param unionid
	 */
	public static JSONObject getcustomer(){
		JSONObject j = new JSONObject();

		JSONObject j1 = new JSONObject();
		JSONObject cust = new JSONObject();
		String moblie = MessageUtil.getMobile();
		int sex = MessageUtil.getGender();
		String provinceAndcity = LocalUtil2.getProvinceAndCity(); //json字符串
		JSONObject obj = JSONObject.fromObject(provinceAndcity);
		String province =obj.getString("province");
		String city =obj.getString("city");
		String county =obj.getString("countys");

		//cust.put("email", moblie+"@qq.com");
		cust.put("email", MessageUtil.getEmail(6, 9));
		cust.put("dateCreated", TimeUtil.getStringTime());
		cust.put("img", ImageUtil.getHttpLink());
		cust.put("name", MessageUtil.getChineseName(sex));
		cust.put("country", "中国");
		if("北京".equals(province)||"天津".equals(province)||"重庆".equals(province) ||"上海".equals(province)||"澳门".equals(province)||"香港".equals(province)){
			cust.put("province", province);
			cust.put("city", province);
			cust.put("county", city);
			county = city;
			city = province;
		}
		if("".equals(county)){
			county = city;
		}
		cust.put("province", province);
		cust.put("city", city);
		cust.put("county", county);
		cust.put("mobile", moblie);
		cust.put("gender", sex);
		JSONArray arr = new JSONArray();
		for(int i = 0; i < 1; i++){
			j1.put("identityType", "wechat");
			j1.put("identityValue", MessageUtil.getOpenId());
			j1.put("identityName", "微信");
			arr.add(j1);
		}

		j.put("customerIdentities", arr);
		j.put("customer", cust);
		return j;
	}

	/**
	 * 将客户信息推送到dmhub中,同时返回客户的id
	 * @param access_token
	 * @param openid
	 * @param unionid
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String sendCustomer(String access_token){
		String customer = getcustomer().toString();
		System.out.println(customer);
		String url = URL+"v1/customerandidentities?access_token="+access_token;
		String retunrstr = SendUtils.post(url, customer);
		JsonObject returnData = new JsonParser().parse(retunrstr).getAsJsonObject();
		JsonElement id = returnData.get("id");
		return id.toString();
	}

	/**
	 * 创建标签,返回创建的标签id
	 * @return 
	 */
	public static String createTag(String access_token){
		String url = URL + "v1/tags?access_token="+access_token;
		JSONObject obj = new JSONObject();
		obj.put("dimension", "coder");
		obj.put("name", "程序员");
		String returnstr = SendUtils.post(url, obj.toString());
		JSONObject returnObj = JSONObject.fromObject(returnstr);
		String tagId = returnObj.getString("id");
		return tagId;
	}

	/**
	 * 给客户打标签,是通过客户id来关联客户与标签的
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String addCustomerTag(String customerId, String access_token){
		String url = URL + "v1/tagservice/addCustomerTag?access_token=" + access_token;
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		obj.put("customerId", customerId);
		
		Map<String, Object> map = TagUtil.getTags();
		Map<String, Object> tagmap = (Map<String, Object>) map.get("map1");
		Map<String, Object> tagmap2 = (Map<String, Object>) map.get("map2");
	     for(Entry<String, Object> entry:tagmap.entrySet()){  
	    	 JSONObject obj1 = new JSONObject();
	            String key = entry.getKey();  
	            String value = (String)entry.getValue();  
	            obj1.put("dimension", value);
	            obj1.put("name", key);
				arr.add(obj1);
	        }  
	     for(Entry<String, Object> entry:tagmap2.entrySet()){  
	    	 JSONObject obj1 = new JSONObject();
	    	 String key = entry.getKey();  
	    	 String lastword = key.substring(key.length()-1);
        	 Pattern pattern = Pattern.compile("[0-9]*");
        	   boolean isMath =  pattern.matcher(lastword).matches();  
        	   if(isMath){
        		   key = key.substring(0,key.length()-1);
        	   }
	    	 String value = (String)entry.getValue();  
	    	 obj1.put("dimension", key);
	    	 obj1.put("name", value);
	    	 arr.add(obj1);
	     }  
		obj.put("tags", arr);
		
		String returncodes = SendUtils.post(url, obj.toString());
		return returncodes;
		}


	/**
	 * 给客户创建事件
	 * @param URL
	 * @param json
	 * @return
	 */
	public static String createCustomerEvent(String customerId,String access_token){
		String url = URL + "v1/customerevents?access_token=" + access_token;
		JSONObject obj = new JSONObject();
		obj.put("customerId", customerId);
		obj.put("date", "");
		obj.put("event", "submit_form");
		obj.put("targetId", "111");
		obj.put("targetName", "阿斯达");
		String returnCode = SendUtils.post(url, obj.toString());
		return returnCode;
	}

	/**
	 * 创建客户组群
	 * @param URL
	 * @param json
	 * @return
	 */
	public static String createList(String access_token){
		String url = URL + "v1/lists?access_token=" + access_token;
		JSONObject obj = new JSONObject();
		obj.put("email", "1");
		obj.put("name", "自动导入组群测试03");
		obj.put("city", "3");
		obj.put("mobile", "4");
		obj.put("source", "5");
		String returnCode = SendUtils.post(url, obj.toString());
		JSONObject returnObj = JSONObject.fromObject(returnCode);
		String listid = returnObj.getString("id");
		return listid;
	}

	/**
	 * 将客户放入某个组群中
	 * @param URL
	 * @param json
	 * @return
	 */
	public static String updateList(String id, String access_token){
		String url = URL + "v1/lists/" + id + "?access_token=" + access_token;
		JSONObject obj = new JSONObject();
		obj.put("name", "自动导入组群测试");
		String returnCode = SendUtils.post(url, obj.toString());
		return returnCode;
	}

}
