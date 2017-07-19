package com.autoCustomer.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.autoCustomer.dao.DeImageMapper;
import com.autoCustomer.dao.DeProductsMapper;
import com.autoCustomer.dao.DeStageEventMapper;
import com.autoCustomer.dao.DeStageOrderMapper;
import com.autoCustomer.dao.DeTagMapper;
import com.autoCustomer.dao.TblPropertiesInfoMapper;
import com.autoCustomer.entity.DeImage;
import com.autoCustomer.entity.DeProducts;
import com.autoCustomer.entity.DeStageEvent;
import com.autoCustomer.entity.DeTag;
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
	private DePercentageService percentageService; // 获得符合要求的utc时间

	@Resource
	private DeImageMapper imagedao;

	@Resource
	private DeTagMapper tagdao;

	@Resource
	private DeStageEventMapper eventdao; // 事件dao

	@Resource
	private DeStageOrderMapper stageorderdao;// 查看客户状态是否该有订单

	@Resource
	private DeProductsMapper productdao;

	private static final String ADDRESS_SET = "address_set"; // 地址配置
	private static final String DOMIAN_NAME = "domian_name"; // 域名,可配置测试域名或生产域名
	private static final String APPID = "appid";
	private static final String SERCET = "sercet";

	@Override
	public String addcustomer() {
		String domain = getPropertyInfo(DOMIAN_NAME);
		String appid = getPropertyInfo(APPID);
		String sercet = getPropertyInfo(SERCET);
		// appid.String accessToken = getAccessToken("cl02dd15a2228ee92",
		// "ce2f7581f4203b257ed5687c2e2106c3978a93be");
		String accessToken = getAccessToken(appid, sercet);
		String url = domain + "/v1/customerandidentities?access_token=" + accessToken;
		JSONObject customer = getcustomer();
		Map<String, Object> stagemap = percentageService.getCurrentStage();
		String stage = stagemap.get("message").toString();
		//Integer stageid = (Integer)stagemap.get("id"); //客户状态id,通过状态id找到符合的事件
		Integer stageid = 30;
		List<DeStageEvent> stageevents = eventdao.selectEventsByStage(stageid); // 所有符合客户状态的事件
		customer.put("stage", stage);
		String retunrstr = SendUtils.post(url, customer.toString());
		JSONObject returnobj = JSONObject.fromObject(retunrstr);
		String name = returnobj.get("name").toString();
		System.out.println("创建客户返回的是" + name);
		String customeid = returnobj.getString("id");
		String dateJoin = returnobj.getString("dateJoin");
		String ordertime = createCustomerEvent(customeid, accessToken, dateJoin, stageevents);
		ordertime = paserUtcTime(ordertime);
		Integer hasOrder = stageorderdao.selectByStageId(stageid);// 是否需要配置订单
		if (hasOrder == 1) {
			List<DeProducts> products = getproducts();
			String returndeal = addcustomerDeals(customeid, accessToken, products, ordertime);
			System.out.println("创建订单返回的 " + returndeal);
		}

		return "";

	}

	/**
	 * 获得accessToken
	 * @param appid
	 * @param sercet
	 */
	private String getAccessToken(String appid, String sercet) {
		String domain = getPropertyInfo(DOMIAN_NAME);
		String url = domain + "/security/accesstoken";
		String retunrstr = SendUtils.sendGet(url,"grant_type=client_credentials&appid=" + appid + "&secret=" + sercet + "");
		// 发送get请求,通过appid和sercet获取accesstoken.
		// retunrstr ="{\"error_code\":0,\"access_token\":\"123\"}";
		JsonObject returnData = new JsonParser().parse(retunrstr).getAsJsonObject();
		System.out.println("调用accessToken   " + returnData);
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
	public JSONObject getcustomer() {
		JSONObject data = new JSONObject();

		JSONObject custidentitie = new JSONObject();
		JSONObject cust = new JSONObject();
		String moblie = MessageUtil.getMobile();
		int sex = MessageUtil.getGender();
		String addressSet = getPropertyInfo(ADDRESS_SET);
		String provinceAndcity = LocalUtil2.getProvinceAndCity(addressSet); // json字符串
		JSONObject obj = JSONObject.fromObject(provinceAndcity);
		String province = obj.getString("province");
		String city = obj.getString("city");
		String county = obj.getString("countys");

		cust.put("email", MessageUtil.getEmail(6, 9));
		cust.put("dateJoin", percentageService.getRanCreateTime());
		cust.put("img", getImage());
		cust.put("name", MessageUtil.getChineseName(sex));
		cust.put("country", "中国");
		if ("北京".equals(province) || "天津".equals(province) || "重庆".equals(province) || "上海".equals(province)
				|| "澳门".equals(province) || "香港".equals(province)) {
			cust.put("province", province);
			cust.put("city", province);
			cust.put("county", city);
			county = city;
			city = province;
		}
		if ("".equals(county)) {
			county = city;
		}
		cust.put("province", province);
		cust.put("city", city);
		cust.put("county", county);
		cust.put("mobile", moblie);
		cust.put("gender", sex);
		cust.put("source", percentageService.getActiveData());
		JSONArray identitiesarr = new JSONArray();
		for (int i = 0; i < 1; i++) {
			custidentitie.put("identityType", "wechat");
			custidentitie.put("identityValue", MessageUtil.getOpenId());
			custidentitie.put("identityName", "微信");
			identitiesarr.add(custidentitie);
		}

		data.put("customerIdentities", identitiesarr);
		data.put("customer", cust);
		return data;
	}


	/**
	 * 给客户绑定事件 返回绑定的事件最后的一个事件 有可能绑定订单,订单事件要在最后的
	 * 返回最后一个事件创建的事件,如果有订单,订单的创建时间要在这个时间之后
	 * @param customerId
	 * @param access_token
	 * @param dateTime
	 */
	public String createCustomerEvent(String customerId,String access_token,String dateTime,List<DeStageEvent> events){
		List<DeStageEvent> unrelatedevents = eventdao.selectUnRelatedStageEvent();
		String domain = getPropertyInfo(DOMIAN_NAME);
		String url = domain + "/v1/customerevents?access_token=" + access_token;
		String differentTime = dateTime;
		int eventsize = unrelatedevents.size();
		int index = (int) (Math.random() * eventsize);
		DeStageEvent event = unrelatedevents.get(index);
		Integer related = event.getIsrelated();
		if (related != null && related != 0) {
			differentTime = paserUtcTime(differentTime);
			DeStageEvent relatedevent = eventdao.selectByPrimaryKey(related);
			JSONObject obj = new JSONObject();
			obj.put("customerId", customerId);
			obj.put("date", differentTime);
			obj.put("event", relatedevent.getEvent());
			obj.put("targetId", relatedevent.getTargetid());
			obj.put("targetName", relatedevent.getTargetname());
			  SendUtils.post(url, obj.toString());
			System.out.println("将客户绑定事件的json  " + obj.toString());
		}

		differentTime = paserUtcTime(differentTime);
		JSONObject eventobj = new JSONObject();
		eventobj.put("customerId", customerId);
		eventobj.put("date", differentTime);
		eventobj.put("event", event.getEvent());
		eventobj.put("targetId", event.getTargetid());
		eventobj.put("targetName", event.getTargetname());
		 SendUtils.post(url, eventobj.toString());
		System.out.println("将客户绑定事件的json  " + eventobj.toString());

		for (DeStageEvent deStageEvent : events) {
			differentTime = paserUtcTime(differentTime); // 事件的发生时间不同
			JSONObject obj = new JSONObject();
			obj.put("customerId", customerId);
			obj.put("date", differentTime);
			obj.put("event", deStageEvent.getEvent());
			obj.put("targetId", deStageEvent.getTargetid());
			obj.put("targetName", deStageEvent.getTargetname());
			 SendUtils.post(url, obj.toString());
			System.out.println("将客户绑定事件的json  " + obj.toString());
		}
		return differentTime;
	}


	/**
	 * 给符合条件的客户创建订单
	 * @param customerId
	 * @param listId
	 * @param access_token
	 * @return
	 */
	public String addcustomerDeals(String customerId, String access_token, List<DeProducts> products,String ordertime) {
		String domain = getPropertyInfo(DOMIAN_NAME);
		String url = domain + "/v1/deals" + "?access_token=" + access_token;
		JSONObject order = new JSONObject();
		JSONArray lines = new JSONArray();
		Double totalPrice = 0d;
		Double amountDiscounts = 0d;
		for (DeProducts deProducts : products) {
			JSONObject productobj = new JSONObject();
			Integer qty = deProducts.getProductquantity();
			Double price = deProducts.getProductprice();
			Double amountDiscount = deProducts.getAmountdiscount();
			productobj.put("productName", deProducts.getProductname());
			productobj.put("productId", deProducts.getProductid());
			productobj.put("qty", qty);
			productobj.put("priceUnit", price);
			amountDiscounts += qty * amountDiscount;
			totalPrice += qty * price;
			lines.add(productobj);
		}

		order.put("customerId", customerId);
		order.put("orderNo", MessageUtil.getOrderNumber());
		order.put("amountTotal", totalPrice);
		order.put("amountDiscount", amountDiscounts);
		order.put("amountPaid", totalPrice - amountDiscounts);
		order.put("dateOrder", ordertime);
		order.put("lines", lines);
		System.out.println("订单格式 " + order.toString());

		String returnCode = SendUtils.post(url, order.toString());
		return returnCode;
	}
	
   /**
    * 返回随机数量的不同的商品信息,每个商品的出售数量也随机
    * @return
    */
    public  List<DeProducts> getproducts() {
    	List<DeProducts> products = productdao.selectProduct();
    	int selected = (int) ((Math.random()*5)+1);//返回随机数量的商品
        List<DeProducts> reList = new ArrayList<DeProducts>();
 
        Random random = new Random();
        // 先抽取，备选数量的个数
        if (products.size() >= selected) {
            for (int i = 0; i < selected; i++) {
                // 随机数的范围为0-list.size()-1;
                int target = random.nextInt(products.size());
                reList.add(products.get(target));
                products.remove(target);
            }
        } else {
            selected = products.size();
            for (int i = 0; i < selected; i++) {
                // 随机数的范围为0-list.size()-1;
                int target = random.nextInt(products.size());
                reList.add(products.get(target));
                products.remove(target);
            }
        }
        for (DeProducts deProducts : reList) { 
        	//在循环准备返回的product,里面的商品数量也返回随机的小于存量的
        	Integer quantity = deProducts.getProductquantity();//商品数量
        	int quantitysize = (int)(Math.random()*quantity);
        	if(quantitysize ==0){
        		quantitysize = 1;
        	}
        	deProducts.setProductquantity(quantitysize);
			
		}
        return reList;
    }
    
	/**
	 * 将客户与标签绑定
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String addCustomerTag(String customerId, String access_token,String city,String totalpay){
		String domain = getPropertyInfo(DOMIAN_NAME);
		String url = domain + "/v1/tagservice/addCustomerTag?access_token=" + access_token;
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		obj.put("customerId", customerId);
		List<DeTag> tags = tagdao.selectAllTag();
		Map<String, String> tagdealmap = new HashMap<String, String>();
		for (DeTag deTag : tags) {
			String dimension = deTag.getDimension();
			String name = deTag.getTagname();
			tagdealmap.put(name, dimension);
		}

		TagUtil tag = new TagUtil();
		Map<String, Object> map = tag.getTags(tagdealmap);
		Map<String, Object> tagmap = (Map<String, Object>) map.get("map1");
		Map<String, Object> tagmap2 = (Map<String, Object>) map.get("map2");
		for (Entry<String, Object> entry : tagmap.entrySet()) {
			JSONObject obj1 = new JSONObject();
			String key = entry.getKey();
			String value = (String) entry.getValue();
			obj1.put("dimension", value);
			obj1.put("name", key);
			arr.add(obj1);
		}
		for (Entry<String, Object> entry : tagmap2.entrySet()) {
			JSONObject obj1 = new JSONObject();
			String key = entry.getKey();
			String lastword = key.substring(key.length() - 1);
			Pattern pattern = Pattern.compile("[0-9]*");
			boolean isMath = pattern.matcher(lastword).matches();
			if (isMath) {
				key = key.substring(0, key.length() - 1);
			}
			String value = (String) entry.getValue();
			obj1.put("dimension", key);
			obj1.put("name", value);
			arr.add(obj1);
		}
		obj.put("tags", arr);

		String returncodes = SendUtils.post(url, obj.toString());
		return returncodes;
	}

	/**
	 * 创建客户组群
	 * @param URL
	 * @param json
	 */
	public String createList(String access_token, String name) {
		String domain = getPropertyInfo(DOMIAN_NAME);
		String url = domain + "/v1/lists?access_token=" + access_token;
		JSONObject obj = new JSONObject();
		obj.put("name", name);
		String returnCode = SendUtils.post(url, obj.toString());
		JSONObject returnObj = JSONObject.fromObject(returnCode);
		String listid = returnObj.getString("id");// 群组id
		return listid;
	}

	/**
	 * 将客户放入某个组群中
	 * @param URL
	 * @param json
	 */
	public String addcustomertoList(String customerId, String listId, String access_token) {
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
	private String getImage() {
		String imageurl = "";
		List<DeImage> arrs = imagedao.selectAllImage();
		int size = arrs.size();
		if (size > 0) {
			int index = (int) (Math.random() * size);
			DeImage image = arrs.get(index);
			imageurl = image.getImageurl();
		}
		return imageurl;
	}

	/**
	 * 获取配置数据
	 * @param kind
	 */
	public String getPropertyInfo(String kind) {
		List<String> list = tblPropertiesInfoDao.selectPropertyInfoByKind(kind);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return "";
	}

	/**
	 * 返回不同的时间
	 * @param time
	 */
	public String paserUtcTime(String time) {
		Date date = null;
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		df1.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			date = df1.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.HOUR_OF_DAY, 3);
		String returndate = df1.format(ca.getTime());
		return returndate;
	}

}
