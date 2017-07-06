package com.autoCustomer.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


/**
 * 选择地区工具，包含全国各地省级市级
 * @author LiuJinan
 *
 */
public class LocalUtil {
	//各地区xml文件路径
	private static final String LOCAL_LIST_PATH = "WEB-INF/LocList.xml";
	//所有国家名称List
	private static final List<String> COUNTRY_REGION = new ArrayList<String>();
	private static LocalUtil localutil;
	private SAXReader reader;
	private Document document;
	private Element rootElement;//根元素
	
	//初始化
	private LocalUtil(){
		//1.读取
		reader = new SAXReader();
		try {
			document = reader.read(LOCAL_LIST_PATH);		
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		//2.获得根元素
		rootElement =  document.getRootElement();	
		//3.初始化所有国家名称列表
		Iterator it =  rootElement.elementIterator();
		Element ele = null;
		while(it.hasNext()){
			ele = (Element)it.next();
			COUNTRY_REGION.add(ele.attributeValue("Name"));
		}
	}
	
	/**
	 * 
	 * @author		LiuJinan
	 * @TODO		功能：	获取所有国家名称
	 * @time		2016-8-26 上午9:02:05
	 * @return		String[]
	 */
	public List<String> getCountry(){
		return COUNTRY_REGION;
	}
	
	/**
	 * 
	 * @author		LiuJinan
	 * @TODO		功能：	根据国家名获取该国所有省份
	 * @time		2016-8-26 上午9:07:21
	 * @param countryName	国家名，从getCountry()从取出
	 * @return		List<Element>
	 */
	private List<Element> provinces(String countryName){
		Iterator it =  rootElement.elementIterator();
		List<Element> provinces = new ArrayList<Element>();
		Element ele = null;
		while(it.hasNext()){
			ele = (Element)it.next();
			COUNTRY_REGION.add(ele.attributeValue("Name"));
			if(ele.attributeValue("Name").equals(countryName)){
				provinces = ele.elements();
				break;
			}
		}
		return provinces;
	}
	
	/**
	 * 
	 * @author		LiuJinan
	 * @TODO		功能：	根据国家名获取该国所有省份
	 * @time		2016-8-26 上午9:07:21
	 * @param countryName	国家名，从getCountry()从取出
	 * @return		List<Element>
	 */
	public String getProvinces(){
		String countryName ="中国";
		List<Element> tmp = this.provinces(countryName);
		List<String> list = new ArrayList<String>();
		for(int i=0; i<tmp.size(); i++){
			list.add(tmp.get(i).attributeValue("Name"));
		}
		int index = (int) (Math.random() * list.size());
		return list.get(index);
	}
	
	/**
	 * 
	 * @author		LiuJinan
	 * @TODO		功能：根据国家名和省份名，获取该省城市名列表
	 * @time		2016-8-26 上午9:15:24
	 * @param province
	 * @param provinceName
	 * @return
	 */
	private List<Element> cities(String countryName, String provinceName){
		List<Element> provinces =  this.provinces(countryName);
		List<Element> cities = new ArrayList<Element>();
		if(provinces==null || provinces.size()==0){		//没有这个城市
			return cities;
		}
		
		for(int i=0; i<provinces.size(); i++){
			if(provinces.get(i).attributeValue("Name").equals(provinceName)){
				cities = provinces.get(i).elements();
				break;
			}
		}
		return cities;
	}
	
	
	/**
	 * 
	 * @author		LiuJinan
	 * @TODO		功能：根据国家名和省份名，获取该省城市区名名列表
	 * @time		2016-8-26 上午9:15:24
	 * @param province
	 * @param provinceName
	 * @return
	 */
	private List<Element> countys(String countryName, String provinceName,String cityName){
		List<Element> provinces =  this.provinces(countryName);
		List<Element> cities = this.cities(countryName, provinceName);
		List<Element> cityNames = new ArrayList<Element>();
		if(cities==null || cities.size()==0){		//没有这个城市
			return cities;
		}
		
		for(int i=0; i<provinces.size(); i++){
			if(cities.get(i).attributeValue("Name").equals(cityName)){
				cityNames = cities.get(i).elements();
				break;
			}
		}
		return cityNames;
	}
	
	/**
	 * 
	 * @author		LiuJinan
	 * @TODO		功能：根据国家名和省份名获取城市列表
	 * @time		2016-8-26 下午4:55:55
	 * @param countryName
	 * @param provinceName
	 * @return		List<String>
	 */
	public String getCities(String provinceName){
		String countryName ="中国";
		List<Element> tmp =  this.cities(countryName, provinceName);
		List<String> cities = new ArrayList<String>();
		for(int i=0; i<tmp.size(); i++){
			cities.add(tmp.get(i).attributeValue("Name"));
		}
		int index = (int) (Math.random() * cities.size());
		return cities.get(index);
	}
	
	
	/**
	 * 
	 * @author		LiuJinan
	 * @TODO		功能：根据国家名和省份名获取城市列表
	 * @time		2016-8-26 下午4:55:55
	 * @param countryName
	 * @param provinceName
	 * @return		List<String>
	 */
	public String getCounty(String provinceName,String cityName){
		String countryName ="中国";
		List<Element> tmp =  this.countys(countryName, provinceName,cityName);
		
		if(tmp.size()== 0){
			return "";
		}
		List<String> countys = new ArrayList<String>();
		for(int i=0; i<tmp.size(); i++){
			countys.add(tmp.get(i).attributeValue("Name"));
		}
		int index = (int) (Math.random() * countys.size());
		return countys.get(index);
	}
	
	
	
	public static LocalUtil getInstance(){
		if(localutil==null){
			localutil = new LocalUtil();
		}
		return localutil;
	}

}
