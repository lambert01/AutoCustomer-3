package com.autoCustomer.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.autoCustomer.dao.DePercentageMapper;
import com.autoCustomer.service.DePercentageService;
import com.autoCustomer.util.Constant;
import com.sun.mail.imap.protocol.MailboxInfo;

@Service("dePercentageServiceImpl")
public class DePercentageServiceImpl implements DePercentageService{
	
	
	@Resource
	private DePercentageMapper dePercentageMapper;
	
	private final static int time = 90;
	
	
	public Map<String, Object> getCurrentStage(String accounttype) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("accounttype", accounttype);
		map.put("type", Constant.CURRENT_STAGE);
		List<Map<String, Object>> list = dePercentageMapper.selectByType(map);
		double num = getNum(Constant.DIGIT2);
		String message = "";
		for(int i=0;i<list.size();i++){
			String recursion = getRecursionData(i,list);
			double startNum = Double.valueOf(recursion.split(":")[0]);
			double endNum = Double.valueOf(recursion.split(":")[1]);
			if(num > startNum && num <= endNum){
				message = (String)list.get(i).get("message");
				Integer id = Integer.parseInt (list.get(i).get("id").toString());
				returnMap.put("message", message);
				returnMap.put("id", id);
				break;
			}
		}
		return returnMap;
	}
	
	
	public String getActiveData(String accounttype) {
		Map<String, Object> querymap = new HashMap<String, Object>();
		querymap.put("accounttype", accounttype);
		querymap.put("type", Constant.ACTIVITY);
		List<Map<String, Object>> list = dePercentageMapper.selectByType(querymap);
		double num = getNum(Constant.DIGIT2);
		String message = "";
		for(int i=0;i<list.size();i++){
			String recursion = getRecursionData(i,list);
			double startNum = Double.valueOf(recursion.split(":")[0]);
			double endNum = Double.valueOf(recursion.split(":")[1]);
			if(num > startNum && num <= endNum){
				message = (String)list.get(i).get("message");
				break;
			}
		}
		return message;
	}
	
	public String getRanCreateTime(String accounttype) {
		double b = Math.random();
		String createTime = "";
		DecimalFormat df = new DecimalFormat(Constant.DIGIT);
		Map<String, Object> querymap = new HashMap<String, Object>();
		querymap.put("accounttype", accounttype);
		querymap.put("type", 0);
		
		List<Map<String, Object>> monthList = dePercentageMapper.selectByType(querymap);
		double d = Double.valueOf(df.format(b));
		//double d = 0.607;
		for(int i=0;i<monthList.size();i++){
			String recursion = getRecursionData(i,monthList);
			double start = Double.parseDouble(recursion.split(":")[0]);
			double end = Double.parseDouble(recursion.split(":")[1]);
			if(d > start && d<= end){
				String state_date = ((String) (monthList.get(i).get("start_date"))).split("-")[0];
				String end_date = ((String) monthList.get(i).get("end_date")).split("-")[1];
				createTime = Constant.YEAR+"-"+getRandomMonth(state_date,end_date,accounttype);
				break;
			}
		}
		 SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		 SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 df1.setTimeZone(TimeZone.getTimeZone("UTC"));
		 Date date = null;
		 try {
			  date = df2.parse(createTime);
			  createTime = df1.format(date);
		} catch (Exception e) {
			createTime = null;
		}
		 
		return createTime;
	}
	
	
	
	public String getRandomMonth(String state_date,String end_date,String accounttype){
		List<Map<String,Object>> monthList = null;
		String recursion = "";
		double start = 0.0;
		double end = 0.0;
		String day = "";
		String data = "";
		String spread = "";
		String time = "";
		String start_time = "";
		String end_time = "";
		double num = getNum(Constant.DIGIT2);
		//double num = 0.25;
		if("04".equals(state_date) || "4".equals(state_date) ){
			Map<String, Object> querymap = new HashMap<String, Object>();
			querymap.put("accounttype", accounttype);
			querymap.put("type", getString(state_date));
			monthList = dePercentageMapper.selectByType(querymap);
			//double num = 0.6;
			for(int j = 0;j<monthList.size();j++){
				recursion =  getRecursionData(j,monthList);
				start = Double.parseDouble(recursion.split(":")[0]);
				end = Double.parseDouble(recursion.split(":")[1]);
				if(num > start && num <= end){
					start_time = ((String) (monthList.get(j).get("start_date"))).split("-")[1];
					end_time = ((String) (monthList.get(j).get("end_date"))).split("-")[1];
					spread = (String)monthList.get(j).get("spread");
					day = getRanTime(Integer.valueOf(start_time),Integer.valueOf(end_time));
					time = getTime(spread,accounttype);
					data = state_date+"-"+day+" "+time;
					break;
				}
			}
		}else if("05".equals(state_date) || "5".equals(state_date)){
			Map<String, Object> querymap = new HashMap<String, Object>();
			querymap.put("accounttype", accounttype);
			querymap.put("type", getString(state_date));
			
			monthList = dePercentageMapper.selectByType(querymap);
			//double num = 0.6;
			for(int j = 0;j<monthList.size();j++){
				recursion =  getRecursionData(j,monthList);
				start = Double.parseDouble(recursion.split(":")[0]);
				end = Double.parseDouble(recursion.split(":")[1]);
				if(num > start && num <= end){
					start_time = ((String) (monthList.get(j).get("start_date"))).split("-")[1];
					end_time = ((String) (monthList.get(j).get("end_date"))).split("-")[1];
					spread = (String)monthList.get(j).get("spread");
					day = getRanTime(Integer.valueOf(start_time),Integer.valueOf(end_time));
					time = getTime(spread,accounttype);
					data = state_date+"-"+day+" "+time;
					break;
				}
			}
		}else{
			day = getRanTime(Integer.valueOf(state_date),Integer.valueOf(end_date));
			time = getTime("",accounttype);
			data = state_date+"-"+day+" "+time;
		}
		return data;
	}
	
	
	public String getTime(String spread,String accounttype){
		List<Map<String,Object>> monthList = null;
		String recursion = "";
		double start = 0.0;
		double end = 0.0;
		String state_date = "";
		String end_date = "";
		String time = "";
		double num = getNum(Constant.DIGIT2);
		if(null != spread && !"".equals(spread) && spread.equals(Constant.MAJORITY_DATE)){
			Map<String, Object> querymap = new HashMap<String, Object>();
			querymap.put("accounttype", accounttype);
			querymap.put("type", Constant.MAJORITY);
			
			monthList = dePercentageMapper.selectByType(querymap);
			for(int i = 0;i<monthList.size();i++){
				recursion =  getRecursionData(i,monthList);
				start = Double.parseDouble(recursion.split(":")[0]);
				end = Double.parseDouble(recursion.split(":")[1]);
				if(num > start && num <= end){
					state_date = ((String) (monthList.get(i).get("start_date"))).split(":")[0];
					end_date = ((String) (monthList.get(i).get("end_date"))).split(":")[0];
					time = getRanTime(Integer.valueOf(state_date),Integer.valueOf(end_date))+":"+getRanMinute()+":"+getRanMinute();
				}
			}
		}else{
			Map<String, Object> querymap = new HashMap<String, Object>();
			querymap.put("accounttype", accounttype);
			querymap.put("type", Constant.AVERAGE_DATE);
			monthList = dePercentageMapper.selectByType(querymap);
			for(int i = 0;i<monthList.size();i++){
				recursion =  getRecursionData(i,monthList);
				start = Double.parseDouble(recursion.split(":")[0]);
				end = Double.parseDouble(recursion.split(":")[1]);
				if(num > start && num <= end){
					state_date = ((String) (monthList.get(i).get("start_date"))).split(":")[0];
					end_date = ((String) (monthList.get(i).get("end_date"))).split(":")[0];
					time = getRanTime(Integer.valueOf(state_date),Integer.valueOf(end_date))+":"+getRanMinute()+":"+getRanMinute();
					break;
				}
			}
		}
		return time;
		
	}
	
	public String getRecursionData(int i,List<Map<String,Object>> list){
		double start = 0.0;
		double end = 0.0;
		if(i == 0){
			start = 0;
			end = (Double) list.get(i).get("percentage");
			return start + ":" +end;
		}else{
			for(int j = 0;j<=i;j++){
				if(j<=i-1){
					start += (Double)list.get(j).get("percentage");
				}
				end += (Double)list.get(j).get("percentage");
			}
			return start + ":" +end;
		}
	}
	
	//获取自定义随机数
		public double getNum(int digit){
			String num = "0123456789";
			Random ran = new Random();
			StringBuffer sb = new StringBuffer();
			sb.append("0.");
			for(int i=0;i<digit;i++){
				int a = ran.nextInt(num.length());
				sb.append(num.charAt(a));
			}
			return Double.valueOf(sb.toString());
		}
		
		public String getRanTime(int start,int end){
			DecimalFormat df = new DecimalFormat("00");
			Random ran = new Random();
			String[] str = "00,01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31".split(",");
			int num = ran.nextInt(end - start);
			int time = Integer.valueOf(str[start + num]);
			String strTime = df.format(time);
			return strTime;
		}
		
		public static String getString(String str){
			return String.valueOf(Integer.parseInt(str));
		}
		
		public String getRanMinute(){
		    //DecimalFormat df = new DecimalFormat("00");
		    Random ran = new Random();
		    String[] str = "00,01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,52,52,53,54,55,56,56,58,59".split(",");
		    int num = ran.nextInt(60);
		    String strTime = str[num];
		    return strTime;
		  }
		
		public  String frontPercentage(String accounttype){
			double ranNum = getNum(2);
		    Random ran = new Random();
		    int num = ran.nextInt(91);
		    Date date = new Date();
		    Calendar c = Calendar.getInstance();
		    SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    c.setTime(date);
		    c.add(Calendar.DAY_OF_MONTH, -30);
		    date = c.getTime();
		    c.setTime(date);
		    if(ranNum >=0.0 && ranNum <= 0.4){
		      c.add(Calendar.DAY_OF_MONTH, -(time+num));
		    }else{
		      c.add(Calendar.DAY_OF_MONTH, -num);
		    }
		    date = c.getTime();
		    String createTime ="";
			 SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			 df1.setTimeZone(TimeZone.getTimeZone("UTC"));
			 try {
				  createTime = df1.format(date);
			} catch (Exception e) {
				createTime = null;
			}
			 return createTime;
		  //  String dateStr = sim.format(date);
		  //  return dateStr;
		  }


		
}
