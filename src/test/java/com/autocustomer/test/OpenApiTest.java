package com.autocustomer.test;

import java.util.Random;

import com.autoCustomer.util.MessageUtil;
import com.autoCustomer.util.SendUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * Created by house on 17-5-15.
 */
public class OpenApiTest {
	public static void main(String[] args) {/*JSONObject order = new JSONObject();
	 JSONArray lines = new JSONArray();
	 for (int i = 0; i < 1; i++) {
		 JSONObject product = new JSONObject();
		 product.put("productName", "");
		 product.put("productId", "");
		 product.put("skuId", "");
		 product.put("category", "");
		 product.put("qty", "");
		 product.put("priceUnit", "");
		 product.put("priceSubTotal", "");
		 lines.add(product);
		
	}
	 order.put("customerId", "");
	 order.put("orderNo", "");
	 order.put("amountTotal", "");
	 order.put("amountPaid", "");
	 order.put("amountDiscount", "");
	 order.put("counponCode", "");
	 order.put("groupId", "");
	 order.put("paymentTerm", "");
	 order.put("paymentNo", "");
	 order.put("type", "");
	 order.put("dateOrder", "");
	 order.put("store", "");
	 order.put("salesChannel", "");
	 order.put("shippingMethod", "");
	 order.put("contactName", "");
	 order.put("contactTel", "");
	 order.put("shippingProvince", "");
	 order.put("shippingCity", "");
	 order.put("shippingCounty", "");
	 order.put("shippingStreet", "");
	 order.put("shippingAddress", "");
	 order.put("lines", lines);
	 System.out.println(order.toString());
	 
	 */
		//addcustomer();
		StringBuilder str=new StringBuilder();//定义变长字符串
		Random random=new Random();
		//随机生成数字，并添加到字符串
		for(int i=0;i<18;i++){
		    str.append(random.nextInt(10));
		}
		//将字符串转换为数字并输出
		String num=str.toString();
		System.out.println(num);
		}
	
  
	public static String addcustomer() {
	 
	 String accessToken = getAccessToken("cl02dd15a2228ee92", "ce2f7581f4203b257ed5687c2e2106c3978a93be");
	 System.out.println("---------------------");
		String url = "http://api.convertwork.cn"+"/v1/customerandidentities?access_token="+accessToken;
	//	String customer = getcustomer().toString();
		System.out.println("accessToken is "+accessToken);
		//String retunrstr = SendUtils.post(url, customer);
		//JSONObject returnobj = JSONObject.fromObject(retunrstr);
		//System.out.println("创建客户返回的json是"+returnobj);
		//String customeid = returnobj.getString("id");
	//	addCustomerTag(customeid,accessToken);
	//	String dateJoin = returnobj.getString("dateJoin");
	//	String event = createCustomerEvent(customeid, accessToken,dateJoin);
		//System.out.println("event is "+event);
		//String listid = createList(accessToken, "静态组群1");
		//createTag(accessToken);
		//addcustomertoList( customeid, listid, accessToken);
		//addCustomerTag(customeid, accessToken);
		return "";

	}
	
	/**
	 * 获得accessToken
	 * @param appid
	 * @param sercet
	 * @return
	 */
	private static String getAccessToken(String appid,String sercet) {
		String domain ="http://api.convertwork.cn";
		String url =domain+"/security/accesstoken";
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
	

	/**
	 * 创建符合格式要求的客户json字符串
	 * @param openid
	 * @param unionid
	 */
	public  JSONObject getcustomer(){
		JSONObject j = new JSONObject();

		JSONObject j1 = new JSONObject();
		JSONObject cust = new JSONObject();
		String moblie = MessageUtil.getMobile();
		int sex = MessageUtil.getGender();

		cust.put("email", MessageUtil.getEmail(6, 9));
		cust.put("name", MessageUtil.getChineseName(sex));
		cust.put("country", "中国");
	 
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
	 * 创建标签,标签也有所属的组群,返回创建的标签id
	 * @return 
	 */
	public  String createTag(String access_token){
		String domain ="http://api.convertwork.cn";
		String url = domain + "/v1/tags?access_token="+access_token;
		JSONObject obj = new JSONObject();
		obj.put("dimension", "basic");
		//obj.put("name", "土豪");
		obj.put("name", "精英");
		String returnstr = SendUtils.post(url, obj.toString());
		JSONObject returnObj = JSONObject.fromObject(returnstr);
		String tagId = returnObj.getString("id");
		return tagId;
	}

	/**
	 * 将客户与标签绑定
	 * @return
	 */
 	@SuppressWarnings("unchecked")
	public  String addCustomerTag(String customerId, String access_token){
		String domain = "http://api.convertwork.cn";
		String url = domain + "/v1/tagservice/addCustomerTag?access_token=" + access_token;
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		obj.put("customerId", customerId);
		return url;
	 
		
	 
		}


    /**
     * 给客户绑定事件
     * @param customerId
     * @param access_token
     * @param dateTime
     * @return
     */
	public  String createCustomerEvent(String customerId,String access_token,String dateTime){
		String domain = "http://api.convertwork.cn";
		String url = domain + "/v1/customerevents?access_token=" + access_token;
		JSONObject obj = new JSONObject();
		obj.put("customerId", customerId);
		obj.put("date",dateTime);
		obj.put("event", "submit_form");
		obj.put("targetId", "111");
		obj.put("targetName", "微信事件");
		String returnCode = SendUtils.post(url, obj.toString());
		return returnCode;
	}

	/**
	 * 创建客户组群
	 * @param URL
	 * @param json
	 * @return
	 */
	public  String createList(String access_token,String name){
		String domain ="http://api.convertwork.cn";
		String url = domain + "/v1/lists?access_token=" + access_token;
		JSONObject obj = new JSONObject();
		obj.put("name", name);
		String returnCode = SendUtils.post(url, obj.toString());
		JSONObject returnObj = JSONObject.fromObject(returnCode);
		String listid = returnObj.getString("id");//群组id
		return listid;
	}

	/**
	 * 将客户放入某个组群中
	 * @param URL
	 * @param json
	 * @return
	 */
	public  String addcustomertoList(String customerId,String listId, String access_token){
		String domain = "http://api.convertwork.cn";
		String url = domain + "/v1/listMembers" + "?access_token=" + access_token;
		JSONObject data = new JSONObject();
		JSONObject obj2 = new JSONObject();
		data.put("customerId", customerId);
		data.put("listId", listId);
		obj2.put("data", data);
		String returnCode = SendUtils.post(url, obj2.toString());
		return returnCode;
	}
	
  

}
