package com.autocustomer.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
/**
 * Created by house on 17-5-15.
 */
public class StressCityLevelTest {
    public static void main(String[] args) throws ParseException{

        PrintStream p = null;
        try {
        FileOutputStream fs = new FileOutputStream(new File("D:\\citysql.txt"));
        p = new PrintStream(fs);

       String[] yixiancity = {"北京","上海","广州","深圳"};
       String[] erxianxiancity = {"成都","杭州","武汉","重庆","南京","天津","苏州","西安","长沙","沈阳","青岛","郑州","大连","东莞","宁波",
    		   "厦门","福州","无锡","合肥","昆明","哈尔滨","济南","佛山","长春","温州","石家庄","南宁","常州",
    		   "泉州","南昌","贵阳","太原","烟台","嘉兴","南通","金华","珠海","徐州","海口"};
         String str = "";
         StringBuffer sql = new StringBuffer();
         sql.append("insert into de_city_level(city,level) values");

    	 String imageurl  ="'"+str +"'"; 
    	List<String> yixian =  Arrays.asList(yixiancity);
    	List<String> erxianxian =  Arrays.asList(erxianxiancity);
    	for (String city : yixian) {
    		String name ="一线城市";
    		String add = "'"+city+"'"+","+"'"+name+"'";
    		sql.append("("+add+"),");
		}
    	
    	for (String city : erxianxian) {
    		String name ="二线城市";
    		String add = "'"+city+"'"+","+"'"+name+"'";
    		sql.append("("+add+"),");
    	}
    	 
    	 

    	 try{
    		  

	} catch (Exception e) {
		System.out.println("error:"+str);
	}

     
         sql.deleteCharAt(sql.length() - 1);
         sql.append(";");
         p.println(sql);
        
         // 当读取的一行不为空时,把读到的str的值赋给str1
        // System.out.println(str1);// 打印出str1
        } catch (FileNotFoundException e) {
         System.out.println("找不到指定文件");
        } catch (IOException e) {
         System.out.println("读取文件失败");
        } finally {}
       
    }
 
    
}
