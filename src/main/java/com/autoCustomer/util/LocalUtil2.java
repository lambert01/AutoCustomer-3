package com.autoCustomer.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
	private static List<String> getLocalSetsByfile(){
		List<String> arrs = new ArrayList<String>();
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null; // 用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
		try {
			String str = "";
			fis = new FileInputStream("src/main/resources/localset.txt");// FileInputStream
			// 从文件系统中的某个文件中获取字节
			isr = new InputStreamReader(fis, "utf-8");// InputStreamReader 是字节流通向字符流的桥梁,
			br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
			while ((str = br.readLine())!=null){
				JSONObject obj = JSONObject.fromObject(str);
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
		return arrs;
	}

	/**
	 * 返回包邮区的概率50%,返回沿海非包邮地区的概率30%,返回内地地区的概率20%
	 * @return
	 */
	public static String getProvinceAndCity(){
		List<String> arrs = getLocalSetsByfile();
		int size = arrs.size();
		int index = (int)(Math.random()*size);
		String str = arrs.get(index);
		return str;
	}

}
