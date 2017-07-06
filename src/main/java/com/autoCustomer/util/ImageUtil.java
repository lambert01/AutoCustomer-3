package com.autoCustomer.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ImageUtil {
	private String imagepath = this.getClass().getClassLoader().getResource("image.txt").getPath();

	/**
	 * 把图片链接放入集合中
	 * @return
	 */
	private  List<String> getHttpLinksByfile(){
		List<String> arrs = new ArrayList<String>();
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null; // 用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
		try {
			String str = "";
			fis = new FileInputStream(imagepath);// FileInputStream
			// 从文件系统中的某个文件中获取字节
			isr = new InputStreamReader(fis, "gb2312");// InputStreamReader  是字节流通向字符流的桥梁,
			br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new  InputStreamReader的对象
			while ((str = br.readLine()) != null) {
				try {
					String imgurl = str.trim();
					if(!("".equals(imgurl))){
					arrs.add(str);
					}
				} catch (Exception e) {
					System.out.println("error:" + str);
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("找不到指定文件");
		} catch (IOException e) {
			System.out.println("读取文件失败");
		} finally {
			try {
				br.close();
				isr.close();
				fis.close();
				// 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return arrs;
	}
	
	/**
	 * 随机返回集合中的一个链接
	 */
	public  String getHttpLink(){
		List<String> arrs = getHttpLinksByfile();
		int size = arrs.size();
		int index = (int) (Math.random() * size);
		return arrs.get(index);
	}

}
