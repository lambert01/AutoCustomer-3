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
public class StressTagTest {
    public static void main(String[] args) throws ParseException{

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;  //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
        PrintStream p = null;
        try {
        FileOutputStream fs = new FileOutputStream(new File("D:\\tagsql.txt"));
        p = new PrintStream(fs);


         String str = "";
         StringBuffer sql = new StringBuffer();
         sql.append("insert into de_tag(dimension,tagname) values");
         fis = new FileInputStream("D:\\tagSet.txt");// FileInputStream
         // 从文件系统中的某个文件中获取字节
          isr = new InputStreamReader(fis,"utf-8");// InputStreamReader 是字节流通向字符流的桥梁,
          br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
         while ((str = br.readLine()) != null) {
        	 String[] strs = str.split(":");
        	 String dimension = strs[0];
        	 String name = strs[1];
        	 String add = "'"+dimension+"'"+","+"'"+name+"'";
        	 
 
        	 try{
        		   sql.append("("+add+"),");

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
