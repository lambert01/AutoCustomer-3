package com.autoCustomer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.json.JSONObject;

/**
 * 选择地区工具，包含全国各地省级市级
 * @author LiuJinan
 */
public class LocalUtil2 {

	/**
	 * 读取地区比例配置
	 * @return
	 */
	private static List<String> getLocalSetsByfile(String addressSet){
		List<String> arrs = new ArrayList<String>();
				JSONObject obj = JSONObject.fromObject(addressSet);
				String[] baoyouqus = obj.get("包邮区").toString().split(",");
				List<String> listbaoyouqus = Arrays.asList(baoyouqus);
				String[] yanhais = obj.get("沿海").toString().split(",");
				List<String> listyanhais = Arrays.asList(yanhais);
				String[] bilis = obj.get("比例").toString().split(":");
				int baoyoucount = Integer.parseInt(bilis[0]);
				int yanhaicount = Integer.parseInt(bilis[1]);
				int neidicount = Integer.parseInt(bilis[2]);
				LocalUtil localutil = LocalUtil.getInstance();
				while(true){
					String province = localutil.getProvinces();
					String city = localutil.getCities(province);
					String countys = localutil.getCounty(province, city);
					String str1 = "{\"province\":\""+province+"\",\"city\":\""+city+"\",\"countys\":\""+ countys+"\"}";
					localutil.getCities(province);
					if(listbaoyouqus.contains(province)){
						if (!(arrs.contains(str1))){
							arrs.add(str1);
						}
					}
					if (arrs.size()==baoyoucount){
						break;
					}
				}

				while(true){
					String province = localutil.getProvinces();
					String city = localutil.getCities(province);
					String countys = localutil.getCounty(province, city);
					String str1 = "{\"province\":\""+province +"\",\"city\":\""+city+"\",\"countys\":\""+countys+"\"}";
					localutil.getCities(province);
					if(listyanhais.contains(province)){
						if (!(arrs.contains(str1))){
							arrs.add(str1);
						}
					}
					if((arrs.size()-baoyoucount)==yanhaicount){
						break;
					}
				}

				while(true){
					String province = localutil.getProvinces();
					String city = localutil.getCities(province);
					String countys = localutil.getCounty(province, city);
					String str1 = "{\"province\":\""+province+"\",\"city\":\""+city+"\",\"countys\":\""+countys+"\"}";
					localutil.getCities(province);
					if(!(listbaoyouqus.contains(province))&&!(listyanhais.contains(province))){
						if (!(arrs.contains(str1))){
							arrs.add(str1);
						}
					}
					if((arrs.size()-baoyoucount-neidicount-1) == neidicount){
						break;
					}
				}
			
		return arrs;
	}

	/**
	 * 返回包邮区的概率50%,返回沿海非包邮地区的概率30%,返回内地地区的概率20%
	 * @return
	 */
	public static String getProvinceAndCity(String addressSet){
		List<String> arrs = getLocalSetsByfile(addressSet);
		int size = arrs.size();
		int index = (int)(Math.random()*size);
		String str = arrs.get(index);
		return str;
	}

}
