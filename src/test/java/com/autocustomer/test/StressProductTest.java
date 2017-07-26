package com.autocustomer.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.ParseException;
/**
 * Created by house on 17-5-15.
 */
public class StressProductTest {
    public static void main(String[] args) throws ParseException{

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;  //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
        PrintStream p = null;
        try {
        FileOutputStream fs = new FileOutputStream(new File("D:\\product.txt"));
        p = new PrintStream(fs);


         String str = "";
         StringBuffer sql = new StringBuffer();
         sql.append("insert into de_products(productName,productPrice,productQuantity,amountDiscount) values");
         fis = new FileInputStream("D:\\product.csv");// FileInputStream
         // 从文件系统中的某个文件中获取字节
          isr = new InputStreamReader(fis,"gb2312");// InputStreamReader 是字节流通向字符流的桥梁,
          br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
          int a = 0;
         while ((str = br.readLine()) != null) {
        	 a = ++a;
         	 if(a==1){
         		 continue;
         	 }
         	String[] strs = str.split(",");
         	String productName ="";
			String productPrice = "";
			String productQuantity ="";
			String amountDiscount ="";
			try {
				productName = "'"+strs[0].trim()+"'";
				productPrice = "'"+strs[1].trim()+"'";
				productQuantity = "'"+strs[3].trim()+"'";
				amountDiscount = "'"+strs[5].trim()+"'";
			} catch (Exception e1) {
			 
			 
			} 
        	 
 
        	 try{
        		 sql.append("("+productName+","+productPrice+","+productQuantity+","+amountDiscount+"),");

		} catch (Exception e) {
			System.out.println("error:"+str);
		}

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
        } finally {
         try {
        	 p.close();
           br.close();
           isr.close();
           fis.close();
          // 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
         } catch (IOException e) {
          e.printStackTrace();
         }
        }
       
    }
 
    
}
