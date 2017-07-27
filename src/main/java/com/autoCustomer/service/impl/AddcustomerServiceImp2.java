package com.autoCustomer.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.autoCustomer.dao.DeAcccountLevelMapper;
import com.autoCustomer.dao.DeCityLevelMapper;
import com.autoCustomer.dao.DeEventTagMapper;
import com.autoCustomer.dao.DeImageMapper;
import com.autoCustomer.dao.DeProductsMapper;
import com.autoCustomer.dao.DePropertiesInfoMapper;
import com.autoCustomer.dao.DeStageEventTargetMapper;
import com.autoCustomer.dao.DeStageOrderMapper;
import com.autoCustomer.dao.DeTagListMapper;
import com.autoCustomer.dao.DeTagMapper;
import com.autoCustomer.entity.DeEventTag;
import com.autoCustomer.entity.DeImage;
import com.autoCustomer.entity.DeProducts;
import com.autoCustomer.entity.DeStageEventTarget;
import com.autoCustomer.entity.DeTag;
import com.autoCustomer.entity.DeTagList;
import com.autoCustomer.service.AddcustomerService;
import com.autoCustomer.service.DePercentageService;
import com.autoCustomer.util.LocalUtil2;
import com.autoCustomer.util.MessageUtil;
import com.autoCustomer.util.SendUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("addcustomerService2")
@Transactional
public class AddcustomerServiceImp2 implements AddcustomerService {

	@Resource
	private DePropertiesInfoMapper dePropertiesInfoDao; //配置dao

	@Resource
	private DePercentageService percentageService; 

	@Resource
	private DeImageMapper imagedao;  //图片dao

	@Resource
	private DeTagMapper tagdao; //标签dao

	@Resource
	private DeStageEventTargetMapper eventdao; // 事件dao

	@Resource
	private DeStageOrderMapper stageorderdao;// 查看客户状态是否该有订单dao

	@Resource
	private DeProductsMapper productdao; //产品dao

	@Resource
	private DeAcccountLevelMapper accountleveldao; // 订单金额对应的级别dao

	@Resource
	private DeCityLevelMapper cityleveldao; // 城市对应的级别dao
	
	@Resource
	private DeEventTagMapper eventtagdao;//内容标签dao
	
	@Resource
	private DeTagListMapper taglistdao;//标签分组dao


	private static final String ADDRESS_SET = "address_set"; // 地址配置
	private static final String DOMIAN_NAME = "domian_name"; // 域名,可配置测试域名或生产域名
	private static final String APPID = "appid_";
	private static final String SERCET = "sercet_";
	private static final String ORDERCOUNT = "orderCount"; //订单数量区间
	private static final String PRODUCTCOUNT = "productCount"; //产品数量区间

	@Override
	public String addcustomer(String username,String accessToken){
		JSONObject returnjson = new JSONObject();
		String domain = getPropertyInfo(DOMIAN_NAME);

		String url = domain + "/v1/customerandidentities?access_token=" + accessToken;
		JSONObject customer = getcustomer();
		Map<String,Object> stagemap = percentageService.getCurrentStage();
		String stage = "";
		try{
			stage = stagemap.get("message").toString();
		}catch (Exception e){
			stage = "未知";
		}
		Integer stageid = (Integer) stagemap.get("id"); // 客户状态id,通过状态id找到符合对应状态的事件

		List<DeStageEventTarget> stageevents = eventdao.selectEventsByStage(stageid); //所有的符合状态的事件都被搜索出来了,有的事件是多选一,过滤一下
		List<DeStageEventTarget> stageeventselecteds = new ArrayList<DeStageEventTarget>(); //这个是传送给创建事件方法的事件集合
		List<DeStageEventTarget> onestageeventselecteds = new ArrayList<DeStageEventTarget>(); //这个是在多个相同阶段的事件中随机选取一个事件的集合
		Integer differtrelation = 0;
		for(DeStageEventTarget DeStageEventTarget : stageevents){
			Integer ismultiselect = DeStageEventTarget.getIsmultiselect(); //是否多选一的
			if(ismultiselect == null){
				stageeventselecteds.add(DeStageEventTarget);
			}else{
				if(differtrelation == 0 || differtrelation == ismultiselect){
					onestageeventselecteds.add(DeStageEventTarget);
					int selectcount = onestageeventselecteds.size();
					int eventselectcount = eventdao.selectCountSize(ismultiselect); //查询该多选事件的数量
					if(selectcount == eventselectcount){
						int index = (int)(Math.random() * eventselectcount);
						DeStageEventTarget stageevent = onestageeventselecteds.get(index); //随机选取一个事件
						stageeventselecteds.add(stageevent);
						onestageeventselecteds.clear();
						differtrelation = 0;
					}
				}
			}
		}
		
		customer.put("stage", stage);
		String retunrstr = SendUtils.post(url, customer.toString());
		JSONObject returnobj = JSONObject.fromObject(retunrstr);
		String name = returnobj.get("name").toString();
		System.out.println("创建客户返回的是" + returnobj);

		String customeid = returnobj.getString("id");
		String dateJoin = returnobj.getString("dateJoin");
		returnjson.put("姓名", name);
		returnjson.put("状态", stage);
		returnjson.put("创建时间", dateJoin);
		returnjson.put("来源", returnobj.getString("source"));
		String city = returnobj.getString("city");
		String ordertime = createCustomerEvent(customeid,accessToken,dateJoin,stageeventselecteds,returnjson);
		ordertime = paserUtcTime(ordertime);
		Integer hasOrder = stageorderdao.selectByStageId(stageid);// 是否需要配置订单,状态为1需要配置订单
		if(hasOrder == null){
			hasOrder = 0;
		}
 
		// 开始配置标签
		String accountLevel = "";
		String cityLevel = "";
		if(hasOrder == 1){
			//发送订单并返回客户级别
			accountLevel = senddeals(customeid,accessToken,ordertime,accountLevel,1,returnjson);
		}
		
		if(hasOrder > 1){
			String ordercountstr = getPropertyInfo(ORDERCOUNT);
			if(ordercountstr == null || "".equals(ordercountstr)){
				ordercountstr = "20";
			}
			int ordecount = 1;
			try {
				ordecount = Integer.parseInt(ordercountstr);
			} catch (NumberFormatException e) {
				ordecount = 1;
			}
			int realordecount = (int)(Math.random()*ordecount);
			if(realordecount == 0){
				realordecount = 1;
			}
			//是复购客户,再次购买
			accountLevel = senddeals(customeid,accessToken,ordertime,accountLevel,realordecount,returnjson);
			
		}
		
		cityLevel = cityleveldao.selectLevelBycity(city);
		if(cityLevel == null || "".equals(cityLevel)){
			cityLevel = "三四线城市";
		}
		String tagreturn = addCustomerTag(customeid,accessToken,cityLevel,accountLevel);
		System.out.println("创建标签返回的结果是" + tagreturn);
		JSONArray tagarr = JSONArray.fromObject(tagreturn);
		List<String> listtag = new ArrayList<String>();
		for (Object tag : tagarr){
			JSONObject jsontag = JSONObject.fromObject(tag);
			String tagname = jsontag.getString("name");
			listtag.add(tagname);
		}
		
		returnjson.put("标签", listtag);
		return returnjson.toString();
	}
	
	/**
	 * 发送订单,并计算是白领还是金领,蓝领
	 * 返回的是白领级别,String类型的需要返回重新赋值
	 * @param customeid
	 * @param accessToken
	 * @param ordertime
	 * @param accountLevel
	 * @param returnjson
	 */
	public String senddeals(String customeid,String accessToken,String ordertime,String accountLevel,int realordecount,JSONObject returnjson){
		Double allamountPaid = 0d;
		String addordertime = ordertime;

		for (int i = 0; i < realordecount; i++){
			addordertime = paserUtcTime(addordertime);
			List<DeProducts> products = getproducts();
			String returndeal = addcustomerDeals(customeid,accessToken,products,addordertime);
			System.out.println("创建订单返回的 " + returndeal);
			
			returnjson.put("订单", JSONObject.fromObject(returndeal));
			JSONObject returndealobj = JSONObject.fromObject(returndeal);
			
			Double amountPaid = Double.parseDouble(returndealobj.get("amountPaid").toString());
			allamountPaid += amountPaid; //有多个订单,计算多个订单的总价
		}
		
		accountLevel = accountleveldao.selectLevelByAccount(allamountPaid);
		if(accountLevel == null || "".equals(accountLevel)){
			accountLevel = accountleveldao.selectMaxLevel(allamountPaid);
		}
		return accountLevel;
		
	}

	/**
	 * 获得accessToken
	 * @param appid
	 * @param sercet
	 */
	@Override
	public String getAccessToken(String username){
		String appid = getPropertyInfo(APPID+username);
		 String sercet = getPropertyInfo(SERCET+username);
		String domain = getPropertyInfo(DOMIAN_NAME);
		if(appid == null || "".equals(appid)){
			return "";
		}
		if(sercet == null || "".equals(sercet)){
			return "";
		}
		
		String url = domain + "/security/accesstoken";
		String retunrstr = SendUtils.sendGet(url,"grant_type=client_credentials&appid="+appid+"&secret="+sercet+"");
		// 发送get请求,通过appid和sercet获取accesstoken.
		// retunrstr ="{\"error_code\":0,\"access_token\":\"123\"}";
		JsonObject returnData = new JsonParser().parse(retunrstr).getAsJsonObject();
		System.out.println("调用accessToken" + returnData);
		String codes = returnData.get("error_code").toString();
		String access_token = "";
		if("0".equals(codes)){
			access_token = returnData.get("access_token").toString().replaceAll("\"","");
		}
		return access_token;
	}

	/**
	 * 创建符合格式要求的客户json字符串
	 * @param openid
	 * @param unionid
	 */
	private JSONObject getcustomer(){
		JSONObject data = new JSONObject();

		JSONObject custidentitie = new JSONObject(); //客户身份json
		JSONObject cust = new JSONObject(); //客户json
		String moblie = MessageUtil.getMobile(); //手机号
		int sex = MessageUtil.getGender(); //性别
		String addressSet = getPropertyInfo(ADDRESS_SET);
		String provinceAndcity = LocalUtil2.getProvinceAndCity(addressSet); // json字符串
		JSONObject provinceAndcityjson = JSONObject.fromObject(provinceAndcity);
		String province = provinceAndcityjson.getString("province");//省份
		String city = provinceAndcityjson.getString("city");//城市
		String county = provinceAndcityjson.getString("countys");//区县
		
        String datejointime = percentageService.getRanCreateTime();
        while(datejointime == null){
        	datejointime = percentageService.frontPercentage();
        }
 
		cust.put("email", MessageUtil.getEmail(6,9));
		cust.put("dateJoin", datejointime); //客户的创建时间
		cust.put("img", getImage());
		cust.put("name", MessageUtil.getChineseName(sex));
		cust.put("country", "中国");
		if("北京".equals(province) || "天津".equals(province) || "重庆".equals(province)
				|| "上海".equals(province)|| "澳门".equals(province) || "香港".equals(province)){
			//如果城市是以上几个城市,省份,城市相同
			cust.put("province",province);
			cust.put("city",province);
			cust.put("county",city);
			county = city;
			city = province;
		}
		if("".equals(county)){
			county = city;
		}
		cust.put("province",province);
		cust.put("city",city);
		cust.put("county",county);
		cust.put("mobile",moblie);
		cust.put("gender",sex);
		cust.put("source",percentageService.getActiveData());//客户来源
		JSONArray identitiesarr = new JSONArray();
		for(int i = 0;i < 1;i++){
			custidentitie.put("identityType","wechat");
			custidentitie.put("identityValue",MessageUtil.getOpenId());
			custidentitie.put("identityName","微信");
			identitiesarr.add(custidentitie);
		}

		data.put("customerIdentities",identitiesarr);
		data.put("customer",cust);
		return data;
	}

   /**
    * 给客户绑定事件 返回绑定的事件最后的一个事件 有可能绑定订单,订单事件要在最后的
	* 返回最后一个事件创建的时间,如果有订单,订单的创建时间要在这个时间之后
    * @param customerId
    * @param secretId
    * @param access_token
    * @param dateTime
    * @param events
    * @param returnjson
    */
	private String createCustomerEvent(String customerId,String access_token,String dateTime,List<DeStageEventTarget> events,JSONObject returnjson){
	
		List<DeStageEventTarget> unrelatedevents = eventdao.selectUnRelatedStageEvent(); //这是与状态无关的事件,随机推送
		String domain = getPropertyInfo(DOMIAN_NAME);
		String url = domain + "/v1/customerevents?access_token=" + access_token;
		
		List<DeEventTag> eventtags = eventtagdao.selectAllEventTag(); //查询内容标签;
		
		JSONArray enventarr = new JSONArray();
		String differentTime = dateTime;
		int eventsize = unrelatedevents.size();
		int index = (int) (Math.random() * eventsize);
		DeStageEventTarget event = unrelatedevents.get(index);
		Integer relatedId = event.getIsrelated();
		if(relatedId != null && relatedId != 0){
			//当前事件与其它事件有关联,关联事件要先发生,该事件才能后发生
			differentTime = paserUtcTime(differentTime);
			DeStageEventTarget relatedevent = eventdao.selectByPrimaryKey(relatedId);
			//这个related就是关联事件的eventid
	 
			 
		 
			//事件的类型是固定的,但是targetid和targetname不一样
			JSONObject obj = new JSONObject();
			obj.put("customerId", customerId);
			obj.put("date", differentTime);
			obj.put("event", relatedevent.getEvent());
			obj.put("targetId", relatedevent.getTargetid());
			obj.put("targetName", relatedevent.getTargetname());
			String returnstr = SendUtils.post(url, obj.toString());
			enventarr.add(JSONObject.fromObject(returnstr));
			System.out.println("将客户绑定事件的json" + obj.toString());
		}
		
		//如果没有关联的事件,直接推送事件
	 
 

		differentTime = paserUtcTime(differentTime);
		JSONObject eventobj = new JSONObject();
		eventobj.put("customerId", customerId);
		eventobj.put("date", differentTime);
		eventobj.put("event", event.getEvent());
		eventobj.put("targetId", event.getTargetid());
		eventobj.put("targetName", event.getTargetname());
		String returnstr = SendUtils.post(url, eventobj.toString());
		enventarr.add(JSONObject.fromObject(returnstr));
		System.out.println("将客户绑定事件的json" + eventobj.toString());
		
		for (DeStageEventTarget deStageEventTarget : events){
			//循环与状态有关的事件,开始推送
			 
			String eventwechat = deStageEventTarget.getEvent();
			if("subscribe".equals(eventwechat)){
              Map<String, Object> idmap = getwechatid(access_token);
              String id = idmap.get("id").toString();
              String name = idmap.get("name").toString();
              differentTime = paserUtcTime(differentTime); // 事件的发生时间不同
  			JSONObject obj = new JSONObject();
  			obj.put("customerId", customerId);
  			obj.put("date", differentTime);
  			obj.put("event", deStageEventTarget.getEvent());
  			obj.put("targetId",id );
  			obj.put("targetName", name);
  			
  			int eventtagssize = eventtags.size();
  			int eventindex = (int)(Math.random()*eventtagssize);
  				DeEventTag eventtag = eventtags.get(eventindex);
  				obj.put("tag",eventtag.getTag());
  		
  			String returnstr1 = SendUtils.post(url, obj.toString());
  			enventarr.add(JSONObject.fromObject(returnstr1));
  			System.out.println("将客户绑定事件的json" + obj.toString());
  			continue;
				
			}

			differentTime = paserUtcTime(differentTime); // 事件的发生时间不同
			JSONObject obj = new JSONObject();
			obj.put("customerId", customerId);
			obj.put("date", differentTime);
			obj.put("event", deStageEventTarget.getEvent());
			obj.put("targetId", deStageEventTarget.getTargetid());
			obj.put("targetName", deStageEventTarget.getTargetname());
			
			int eventtagssize = eventtags.size();
			int eventindex = (int)(Math.random()*eventtagssize);
				DeEventTag eventtag = eventtags.get(eventindex);
				obj.put("tag",eventtag.getTag());
		
			String returnstr1 = SendUtils.post(url, obj.toString());
			enventarr.add(JSONObject.fromObject(returnstr1));
			System.out.println("将客户绑定事件的json" + obj.toString());
		}
		returnjson.put("事件", enventarr);
		return differentTime;
	}

	/**
	 * 给符合条件的客户创建订单,
	 * 返回的json中有订单的总金额,用来添加标签时通过订单金额判断客户是金领,白领,还是蓝领
	 * @param customerId
	 * @param listId
	 * @param access_token
	 */
	private String addcustomerDeals(String customerId,String access_token,List<DeProducts> products,String ordertime){
		String domain = getPropertyInfo(DOMIAN_NAME);
		String url = domain + "/v1/deals" + "?access_token=" + access_token;
		JSONObject order = new JSONObject();
		JSONArray lines = new JSONArray();
		Double totalPrice = 0d;
		Double amountDiscounts = 0d;
		for(DeProducts deProducts : products){
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

		String returnCode = SendUtils.post(url,order.toString());
		return returnCode;
	}

	/**
	 * 返回随机数量的不同的商品信息,每个商品的出售数量也随机
	 */
	private List<DeProducts> getproducts(){
		List<DeProducts> products = productdao.selectProduct();
		//int selected = (int) ((Math.random() * 5) + 1);// 返回随机数量的商品
		int selected = 1;
		String productcount = getPropertyInfo(PRODUCTCOUNT);
		if(productcount != null && !"".equals(productcount)){
			try {
				int count = Integer.parseInt(productcount);
				selected = (int)(Math.random() * count);
				if(selected == 0){
					selected =1;
				}
			} catch (NumberFormatException e) {
				selected = 1;
			}
			
		}

		List<DeProducts> reList = new ArrayList<DeProducts>();

		Random random = new Random();
		// 先抽取，备选数量的个数
		if(products.size() >= selected){
			for(int i = 0; i < selected; i++){
				// 随机数的范围为0-list.size()-1;
				int target = random.nextInt(products.size());
				reList.add(products.get(target));
				products.remove(target);
			}
		}else{
			selected = products.size();
			for(int i = 0; i < selected; i++){
				// 随机数的范围为0-list.size()-1;
				int target = random.nextInt(products.size());
				reList.add(products.get(target));
				products.remove(target);
			}
		}
		for(DeProducts deProducts : reList){
			// 在循环准备返回的product,里面的商品数量也返回随机的小于存量的
			Integer quantity = deProducts.getProductquantity();// 商品数量
			int quantitysize = (int) (Math.random() * quantity);
			if(quantitysize == 0){
				quantitysize = 1;
			}
			deProducts.setProductquantity(quantitysize);
		}
		return reList;
	}

	/**
	 * 将客户与标签绑定
	 */
	private String addCustomerTag(String customerId,String access_token,String citylevel,String accountlevel){
		String domain = getPropertyInfo(DOMIAN_NAME);
		String url = domain + "/v1/tagservice/addCustomerTag?access_token=" + access_token;
		JSONObject obj = new JSONObject();
		JSONArray tagsarr = new JSONArray();
		obj.put("customerId", customerId);
		List<DeTag> tags = tagdao.selectAllTag();
		List<JSONObject> unrelationtags = new ArrayList<JSONObject>();
		List<JSONObject> relationtags = new ArrayList<JSONObject>();
		Integer differtrelation = 0;
		for(DeTag deTag : tags){
			JSONObject tagjson = new JSONObject();
			String dimension = deTag.getDimension();
			String name = deTag.getTagname();
			Integer relation = deTag.getRelation();// 标签的互斥关系,有相同数字的标签不能出现在同一个用户身上
			if(relation == null){
				int didadd = (int)(Math.random()*10);
				if(didadd >= 5){
				tagjson.put("dimension", dimension);
				tagjson.put("name", name);
				unrelationtags.add(tagjson);
				}
			}else{
				// 有些标签不能同事出现在同一个人身上,如青年标签,老年标签,这个标签有相同的relation
				if(differtrelation == 0 || differtrelation == relation){
					tagjson.put("dimension", dimension);
					tagjson.put("name", name);
					relationtags.add(tagjson);
					differtrelation = relation;
					int size = tagdao.selectCountSize(relation);
					int jsonsize = relationtags.size();
					if(jsonsize == size){
						int index = (int)(Math.random() * jsonsize);
						JSONObject relationjson = relationtags.get(index);
							if(!relationjson.isEmpty()){
								tagsarr.add(relationjson);
							}
						relationtags.clear();
						differtrelation = 0;
					}
				}
			}
		}

		// 开始添加非互斥标签
		int unrelationsize = unrelationtags.size();
		int index = (int) (Math.random() * unrelationsize);
		if(index > 5){
			index = 5;
		}
		for(int i = 0; i < index; i++){
			JSONObject unrelationtag = unrelationtags.get(i);
			if(!unrelationtag.isEmpty()){
				tagsarr.add(unrelationtag);
			}
		}

		JSONObject cityandleveljson = new JSONObject();
		JSONObject accountleveljson = new JSONObject();
		cityandleveljson.put("dimension", "bacis");
		cityandleveljson.put("name", citylevel);
		// 金领,白领标签只有在订单生成后才有该标签
		if(accountlevel != null && !"".equals(accountlevel)){
			accountleveljson.put("dimension", "bacis");
			accountleveljson.put("name", accountlevel);
			tagsarr.add(accountleveljson);
		}
		
		tagsarr.add(cityandleveljson);
		obj.put("tags", tagsarr);
		System.out.println("创建标签的json是 " + obj.toString());
		String returncodes = SendUtils.post(url, obj.toString());
		return returncodes;
	}

	/**
	 * 创建客户组群
	 * @param URL
	 * @param json
	 */
	private String createList(String access_token,String name){
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
	 * 在创建标签群组前删除所有的标签
	 * @return
	 */
	public void deleteAllTag(String access_token){/*
		String domain = getPropertyInfo(DOMIAN_NAME);
		String queryurl =domain+"/v1/tags";
		String json = SendUtils.sendGet(queryurl,"access_token="+access_token+"&rows=10");
		System.out.println("返回的json是"+json);
	 
		JSONObject tagsjson = JSONObject.fromObject(json);
		JSONArray arr = tagsjson.getJSONArray("rows");
		System.out.println(arr.size());
		for (Object object : arr) {
			JSONObject tag = JSONObject.fromObject(object);
			String id = tag.get("id").toString();
			String deleteurl =domain+"/v1/tags/"+id;
			  try {
				  doDelete(deleteurl,"access_token="+access_token);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		
	*/}
	
	/**
	 * 创建标签前先检查群组是否创建
	 * @param access_token
	 * @return
	 */
	@Override
	public String gettaglist(String access_token){
		String domain = getPropertyInfo(DOMIAN_NAME);
		String url =domain+"/v1/tagdimensions" ;
		String param = "access_token=" + access_token;
		String returncode = SendUtils.sendGet(url, param);
		JSONArray arrs = JSONArray.fromObject(returncode);
		List<String> returnarr = new ArrayList<String>();
		if(arrs.size() == 0){
			createTagList(access_token);
		}else{
			List<DeTagList> taglists = taglistdao.selectAllTagList();
			List<String> hadtagdemendions = new ArrayList<String>();
		
			for(Object tag : arrs){
				JSONObject jsontag = JSONObject.fromObject(tag);
				String dimensionkey = jsontag.getString("dimensionKey");
				hadtagdemendions.add(dimensionkey);
			}
			
			for(DeTagList deTagList : taglists){
				String dimensionkey = deTagList.getDimensionkey();
				if(!hadtagdemendions.contains(dimensionkey)){
					//hadtagdemendions中保存的是系统中的标签群组
					DeTagList addtag = taglistdao.selectTagListCheckDidHad(dimensionkey);
					String dimensionKey = addtag.getDimensionkey();
					JSONObject obj = new JSONObject();
					obj.put("name", addtag.getTagname());
					obj.put("dimensionKey", dimensionKey);
					String addlisturl = domain+"/v1/tagdimensions?access_token=" + access_token;
					String returnCode = SendUtils.post(addlisturl, obj.toString());
					//给系统中没有的标签群组添加完群组后,添加该群组对应的标签
					List<DeTag> dimensiontags = tagdao.selectAllTagHavingSameDemension(dimensionKey);
					if(dimensiontags.size()>0){
						String addtagurl = domain+"/v1/tags"+ "?access_token=" + access_token;
						for (DeTag deTag : dimensiontags) {
							JSONObject tag = new JSONObject();
							tag.put("name",deTag.getTagname());
							tag.put("dimension",deTag.getDimension());
							String addtagreturnCode = SendUtils.post(addtagurl, tag.toString());
							returnarr.add(addtagreturnCode);
							
						}
					}
					
					returnarr.add(returnCode);
				}else{
					//系统中有的群组,已经被清空,重新添加标签
					List<DeTag> dimensiontags = tagdao.selectAllTagHavingSameDemension(dimensionkey);
					if(dimensiontags.size()>0){
						String addtagurl = domain+"/v1/tags"+ "?access_token=" + access_token;
						for (DeTag deTag : dimensiontags) {
							JSONObject tag = new JSONObject();
							tag.put("name",deTag.getTagname());
							tag.put("dimension",deTag.getDimension());
							String addtagreturnCode = SendUtils.post(addtagurl, tag.toString());
							returnarr.add(addtagreturnCode);
						}
					}
				}
			}
		}
		return returnarr.toString();
		
	}
	
	/**
	 * 创建客户标签群组
	 * 并把标签放入对应的标签群组中
	 * @return
	 */
	public  String createTagList(String access_token){
		String domain = getPropertyInfo(DOMIAN_NAME);
		String url =domain+"/v1/tagdimensions"+ "?access_token=" + access_token;
		List<DeTagList> taglists = taglistdao.selectAllTagList();
		List<String> returnarr = new ArrayList<String>();
		for (DeTagList deTagList : taglists) {
			JSONObject taglistjson = new JSONObject();
			String dimension = deTagList.getDimensionkey();//通过dimension获得数据库中所有赌赢dimension的标签
			taglistjson.put("name", deTagList.getTagname());
			taglistjson.put("dimensionKey", dimension);
			String returnCode = SendUtils.post(url, taglistjson.toString());
			List<DeTag> dimensiontags = tagdao.selectAllTagHavingSameDemension(dimension);
			if(dimensiontags.size()>0){
				String addtagurl = domain+"/v1/tags"+ "?access_token=" + access_token;
				for (DeTag deTag : dimensiontags) {
					JSONObject tag = new JSONObject();
					tag.put("name",deTag.getTagname());
					tag.put("dimension",deTag.getDimension());
					String addtagreturnCode = SendUtils.post(addtagurl, tag.toString());
					returnarr.add(addtagreturnCode);
					
				}
			}
	
			returnarr.add(returnCode);
		}
		return returnarr.toString();
		
	}

	/**
	 * 将客户放入某个组群中
	 * @param URL
	 * @param json
	 */
	private String addcustomertoList(String customerId,String listId,String access_token){
		String domain = getPropertyInfo(DOMIAN_NAME);
		String url = domain + "/v1/listMembers" + "?access_token=" + access_token;
		JSONObject data = new JSONObject();
		JSONObject list = new JSONObject();
		data.put("customerId", customerId);
		data.put("listId", listId);
		list.put("data", data);
		String returnCode = SendUtils.post(url, list.toString());
		return returnCode;
	}
	
    public Map<String,Object> getwechatid(String access_token){
    	Map<String,Object>  idmap = new HashMap<String, Object>();
    	String domain = getPropertyInfo(DOMIAN_NAME);
    	String url = domain+"/v1/wechataccounts";
    	String param = "access_token="+access_token;
    	String returncode = SendUtils.sendGet(url, param);
    	JSONArray arrs  = JSONArray.fromObject(returncode);
    	if(arrs.size() ==0){
    		idmap.put("id", "11");
    		idmap.put("name", "convertlab学堂");
    		
    	}else{
    		Object object = arrs.get(0);
    		JSONObject wechatjson = JSONObject.fromObject(object);
    		String id = wechatjson.get("id").toString();
    		String name = wechatjson.get("name").toString();
    		try {
				name = new String(name.getBytes("gbk"), "utf-8");
			} catch (UnsupportedEncodingException e) {
			}
    		idmap.put("id", id);
    		idmap.put("name",name);
    	}
    	return idmap;
    }

	/**
	 * 随机返回一个图片的路径
	 */
	private String getImage(){
		String imageurl = "";
		List<DeImage> arrs = imagedao.selectAllImage();
		int size = arrs.size();
		if(size > 0){
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
	@Override
	public String getPropertyInfo(String kind){
		List<String> list = dePropertiesInfoDao.selectPropertyInfoByKind(kind);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return "";
	}
	

	/**
	 * 返回不同的时间
	 * @param time
	 */
	private String paserUtcTime(String time){
		int mintime = (int)(Math.random()*300);
		if(mintime <5){
			mintime = 5;
		}
		Date date = null;
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		df1.setTimeZone(TimeZone.getTimeZone("GMT"));
		try{
			date = df1.parse(time);
		}catch (ParseException e){
			e.printStackTrace();
		}
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.MINUTE,mintime);
		String returndate = df1.format(ca.getTime());
		return returndate;
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
	
}
