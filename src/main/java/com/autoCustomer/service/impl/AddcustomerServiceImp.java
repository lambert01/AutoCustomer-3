package com.autoCustomer.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.autoCustomer.dao.DeImageMapper;
import com.autoCustomer.dao.TblPropertiesInfoMapper;
import com.autoCustomer.entity.DeImage;
import com.autoCustomer.service.AddcustomerService;
import com.autoCustomer.service.DePercentageService;
import com.autoCustomer.util.LocalUtil2;
import com.autoCustomer.util.MessageUtil;
import com.autoCustomer.util.SendUtils;
import com.autoCustomer.util.TagUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service("addcustomerService")
@Transactional
public class AddcustomerServiceImp implements AddcustomerService {
	
	@Resource
	private TblPropertiesInfoMapper tblPropertiesInfoDao;
	
	@Resource
	private DePercentageService percentageService; //获得符合要求的utc时间
	
	@Resource
	private DeImageMapper imagedao;
	
	private static final String ADDRESS_SET ="address_set"; //地址配置
	private static final String DOMIAN_NAME ="domian_name"; //域名,可配置测试域名或生产域名
	private static final String APPID ="appid";  
	private static final String SERCET ="sercet";  
    
	@Override
	public String addcustomer(String customer) {
		String domain = getPropertyInfo(DOMIAN_NAME);
		String appid = getPropertyInfo(APPID);
		String sercet = getPropertyInfo(SERCET);
		//appid.String accessToken = getAccessToken("cl02dd15a2228ee92", "ce2f7581f4203b257ed5687c2e2106c3978a93be");
		String accessToken = getAccessToken(appid,sercet);
		String url = domain+"/v1/customerandidentities?access_token="+accessToken;
		String retunrstr = SendUtils.post(url, customer);
		JSONObject returnobj = JSONObject.fromObject(retunrstr);
		System.out.println("创建客户返回的json是"+returnobj);
		String id = returnobj.getString("id");
		String dateJoin = returnobj.getString("dateJoin");
		String event = createCustomerEvent(id, accessToken,dateJoin);
		System.out.println("event is "+event);
		return id;

	}
	
	private String getAccessToken(String appid,String sercet) {
		String domain = getPropertyInfo(DOMIAN_NAME);
		String url =domain+"/security/accesstoken";
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
	

	
	/**
	 * 创建符合格式要求的客户json字符串
	 * @param openid
	 * @param unionid
	 */
	@Override
	public  JSONObject getcustomer(){
		JSONObject j = new JSONObject();

		JSONObject j1 = new JSONObject();
		JSONObject cust = new JSONObject();
		String moblie = MessageUtil.getMobile();
		int sex = MessageUtil.getGender();
		String addressSet = getPropertyInfo(ADDRESS_SET);
		String provinceAndcity = LocalUtil2.getProvinceAndCity(addressSet); //json字符串
		JSONObject obj = JSONObject.fromObject(provinceAndcity);
		String province =obj.getString("province");
		String city =obj.getString("city");
		String county =obj.getString("countys");

		cust.put("email", MessageUtil.getEmail(6, 9));
		cust.put("dateJoin", percentageService.getRanCreateTime());
		cust.put("img", getImage());
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
	 * 创建标签,标签也有所属的组群,返回创建的标签id
	 * @return 
	 */
	public  String createTag(String access_token){
		String domain = getPropertyInfo(DOMIAN_NAME);
		String url = domain + "/v1/tags?access_token="+access_token;
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
	public  String addCustomerTag(String customerId, String access_token){
		String domain = getPropertyInfo(DOMIAN_NAME);
		String url = domain + "/v1/tagservice/addCustomerTag?access_token=" + access_token;
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
     * 给客户绑定事件
     * @param customerId
     * @param access_token
     * @param dateTime
     * @return
     */
	public  String createCustomerEvent(String customerId,String access_token,String dateTime){
		String domain = getPropertyInfo(DOMIAN_NAME);
		String url = domain + "/v1/customerevents?access_token=" + access_token;
		JSONObject obj = new JSONObject();
		obj.put("customerId", customerId);
		obj.put("date",dateTime);
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
	public  String createList(String access_token,String name){
		String domain = getPropertyInfo(DOMIAN_NAME);
		String url = domain + "/v1/lists?access_token=" + access_token;
		JSONObject obj = new JSONObject();
		obj.put("name", name);
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
	public  String addcustomertoList(String customerId,String listId, String access_token){
		String domain = getPropertyInfo(DOMIAN_NAME);
		String url = domain + "/v1/listMembers" + "?access_token=" + access_token;
		JSONObject data = new JSONObject();
		JSONObject obj2 = new JSONObject();
		data.put("customerId", customerId);
		data.put("listId", listId);
		obj2.put("data", data);
		String returnCode = SendUtils.post(url, obj2.toString());
		return returnCode;
	}
	
	/**
	 * 随机返回一个图片的路径
	 * @return
	 */
	private String getImage(){
		String imageurl ="";
		List<DeImage> arrs = imagedao.selectAllImage();
		int size = arrs.size();
		if(size >0){
			int index = (int) (Math.random() * size);
			DeImage image = arrs.get(index);
			imageurl =  image.getImageurl();
		}
		return imageurl;
	
	}
	
	/**
	 * 获取配置数据
	 * @param kind
	 * @return
	 */
	public String getPropertyInfo(String kind){
		List<String> list = tblPropertiesInfoDao.selectPropertyInfoByKind(kind);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return "";
	}

}
