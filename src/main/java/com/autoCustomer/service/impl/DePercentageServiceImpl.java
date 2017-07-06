package com.autoCustomer.service.impl;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.autoCustomer.dao.DePercentageMapper;
import com.autoCustomer.service.DePercentageService;
import com.autoCustomer.util.Constant;

@Service("dePercentageServiceImpl")
public class DePercentageServiceImpl implements DePercentageService{
	
	
	@Resource
	private DePercentageMapper dePercentageMapper;
	
	
	
	public String getCurrentStage() {
		List<Map<String, Object>> list = dePercentageMapper.selectByType(Constant.CURRENT_STAGE);
		double num = getNum(Constant.DIGIT2);
		String message = "";
		for(int i=0;i<list.size();i++){
			String recursion = getRecursionData(i,list);
			System.out.println("recursion:"+recursion);
			double startNum = Double.valueOf(recursion.split(":")[0]);
			double endNum = Double.valueOf(recursion.split(":")[1]);
			if(num > startNum && num <= endNum){
				message = (String)list.get(i).get("message");
				break;
			}
		}
		return message;
	}
	
	
	public String getActiveData() {
		List<Map<String, Object>> list = dePercentageMapper.selectByType(Constant.ACTIVITY);
		double num = getNum(Constant.DIGIT2);
		String message = "";
		for(int i=0;i<list.size();i++){
			String recursion = getRecursionData(i,list);
			System.out.println("recursion:"+recursion);
			double startNum = Double.valueOf(recursion.split(":")[0]);
			double endNum = Double.valueOf(recursion.split(":")[1]);
			if(num > startNum && num <= endNum){
				message = (String)list.get(i).get("message");
				break;
			}
		}
		return message;
	}
	
	public String getRanCreateTime() {
		double b = Math.random();
		String createTime = "";
		DecimalFormat df = new DecimalFormat(Constant.DIGIT);
		List<Map<String, Object>> monthList = dePercentageMapper.selectByType("0");
		double d = Double.valueOf(df.format(b));
		//double d = 0.607;
		System.out.println("getMonthList:"+d);
		for(int i=0;i<monthList.size();i++){
			String recursion = getRecursionData(i,monthList);
			System.out.println(recursion);
			double start = Double.parseDouble(recursion.split(":")[0]);
			double end = Double.parseDouble(recursion.split(":")[1]);
			if(d > start && d<= end){
				String state_date = ((String) (monthList.get(i).get("start_date"))).split("-")[0];
				String end_date = ((String) monthList.get(i).get("end_date")).split("-")[1];
				createTime = Constant.YEAR+"-"+getRandomMonth(state_date,end_date);
				break;
			}
			System.out.println(createTime);
		}
		return createTime;
	}
	
	
	
	public String getRandomMonth(String state_date,String end_date){
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
		System.out.println("getRandomMonth:"+num);
		if("04".equals(state_date) || "4".equals(state_date) ){
			monthList = dePercentageMapper.selectByType(getString(state_date));
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
					time = getTime(spread);
					data = state_date+"-"+day+" "+time;
					break;
				}
			}
		}else if("05".equals(state_date) || "5".equals(state_date)){
			monthList = dePercentageMapper.selectByType(getString(state_date));
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
					time = getTime(spread);
					data = state_date+"-"+day+" "+time;
					break;
				}
			}
		}else{
			day = getRanTime(Integer.valueOf(state_date),Integer.valueOf(end_date));
			time = getTime("");
			data = state_date+"-"+day+" "+time;
		}
		return data;
	}
	
	
	public String getTime(String spread){
		List<Map<String,Object>> monthList = null;
		String recursion = "";
		double start = 0.0;
		double end = 0.0;
		String state_date = "";
		String end_date = "";
		String time = "";
		double num = getNum(Constant.DIGIT2);
		System.out.println("getTime:"+num);
		if(null != spread && !"".equals(spread) && spread.equals(Constant.MAJORITY_DATE)){
			monthList = dePercentageMapper.selectByType(Constant.MAJORITY);
			for(int i = 0;i<monthList.size();i++){
				recursion =  getRecursionData(i,monthList);
				start = Double.parseDouble(recursion.split(":")[0]);
				end = Double.parseDouble(recursion.split(":")[1]);
				if(num > start && num <= end){
					state_date = ((String) (monthList.get(i).get("start_date"))).split(":")[0];
					end_date = ((String) (monthList.get(i).get("end_date"))).split(":")[0];
					time = getRanTime(Integer.valueOf(state_date),Integer.valueOf(end_date))+":00:00";
				}
			}
		}else{
			monthList = dePercentageMapper.selectByType(Constant.AVERAGE_DATE);
			for(int i = 0;i<monthList.size();i++){
				recursion =  getRecursionData(i,monthList);
				start = Double.parseDouble(recursion.split(":")[0]);
				end = Double.parseDouble(recursion.split(":")[1]);
				if(num > start && num <= end){
					state_date = ((String) (monthList.get(i).get("start_date"))).split(":")[0];
					end_date = ((String) (monthList.get(i).get("end_date"))).split(":")[0];
					time = getRanTime(Integer.valueOf(state_date),Integer.valueOf(end_date))+":00:00";
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
		
}