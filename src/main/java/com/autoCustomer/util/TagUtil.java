package com.autoCustomer.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class TagUtil {

	/**
	 * 读取标签配置
	 * @return
	 */
	private  Map<String, String> getAllTagsByfile(){
		Map<String, String> tagmap = new HashMap<String, String>();
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null; // 用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
		try {
			String str = "";
			fis = new FileInputStream(this.getClass().getClassLoader().getResource("tagSet2.txt").getPath());// FileInputStream
			// 从文件系统中的某个文件中获取字节
			isr = new InputStreamReader(fis, "utf-8");// InputStreamReader 是字节流通向字符流的桥梁,
			br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
			while ((str = br.readLine())!=null){
				String[] strs = str.split(":");
				String dimension = strs[0].trim();
				String name = strs[1].trim();
				tagmap.put(name, dimension);

			}
		}catch(FileNotFoundException e){
			System.out.println("找不到指定文件");
		}catch(IOException e){
			System.out.println("读取文件失败");
		}finally{
			try{
				br.close();
				isr.close();
				fis.close();
				// 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return tagmap;
	}

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
	public  Map<String, Object> getTags(){
		Map<String, Object> returnmap = new LinkedHashMap<String, Object>();
		Map<String, Object> returnmap2 = new LinkedHashMap<String, Object>();
		Map<String,String> allTagsmap = getAllTagsByfile();
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
