package com.autoCustomer.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class TagUtil {


	/**
	 * 决定返回几个标签
	 * @return
	 */
	public static int getNum(){
		double a = Math.random()*10;
		int b = (int)a/2+1;
		return b;
	}
	
	/***
	 * 返回标签
	 */
	public  Map<String, Object> getTags(Map<String,String> allTagsmap){
		Map<String, Object> returnmap = new LinkedHashMap<String, Object>();
		Map<String, Object> returnmap2 = new LinkedHashMap<String, Object>();
		int foreachcount = getNum();
		HashSet<String> set = new HashSet<String>();
		for (int j = 0; j < foreachcount; j++) {
			int size = (int) (Math.random()*(allTagsmap.size())); //决定随机返回第几位数据
		       int i = 0;
		        for (String key : allTagsmap.keySet()) {
		            if(i==size){
		            	String name = (String)allTagsmap.get(key);
		            	String lastword = name.substring(name.length()-1);
		            	 Pattern pattern = Pattern.compile("[0-9]*");
		            	   boolean isMath =  pattern.matcher(lastword).matches();   
	
		            	    if(isMath){
		            	    	 set.add(name);
		            	     }else{
		            	    	 returnmap.put(key, name);
		            	     }
		            	     }
		            i++;
		        }
		}
		if(set.size()>0){
			for (String setkey : set) {
				Set<String> keys = allTagsmap.keySet();
				Iterator<String> iter = keys.iterator();
				while(iter.hasNext()){
					String key = iter.next();
					if(allTagsmap.get(key).equals(setkey)){
						returnmap2.put(setkey, key);
					}
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("map1", returnmap);
		map.put("map2", returnmap2);
		return map;
	}
}
