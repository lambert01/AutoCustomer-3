package com.autoCustomer.util;

import java.util.Random;
import java.util.UUID;

public class MessageUtil {
	public static String base = "0123456789";
	private static String firstName = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛范彭鲁韦马苗花方俞任袁柳鲍史唐费岑薛雷贺倪汤滕"
			+ "殷罗毕郝邬安常乐于时傅皮卞齐康伍余元顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闵席季麻强贾路娄江童颜郭梅盛林刁钟徐邱骆高夏蔡田樊胡凌霍虞万"
			+ "柯管卢莫经房裘解应宗宣丁邓郁单杭洪包诸左石崔吉龚程邢裴陆荣翁羊惠魏段牧山谷车侯宓蓬全班仰秋仲伊宫宁栾甘祖武符刘姜詹龙叶印宿白";
	private static String girl = "秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽 ";
	private static String boy = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘";
	private static final String[] email_suffix = "@yahoo.com,@msn.com,@hotmail.com,@live.com,@qq.com,@163.com,@googlemail.com,@126.com,@sina.com,@sohu.com,@yahoo.com.cn".split(",");

	public static int getNum(int start, int end) {
		return (int) (Math.random() * (end - start + 1) + start);
	}

	/**
	 * 返回Email
	 * @param lMin 最小长度
	 * @param lMax 最大长度
	 */
	public static String getEmail(int lMin, int lMax) {
		int length = getNum(lMin, lMax);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = (int) (Math.random() * base.length());
			sb.append(base.charAt(number));
		}
		sb.append(email_suffix[(int) (Math.random() * email_suffix.length)]);
		return sb.toString();
	}

	/**
	 * 返回中文姓名
	 */
	public static String getChineseName(int sex) {
		int index = getNum(0, firstName.length() - 1);
		String first = firstName.substring(index, index + 1);
		String str = boy;
		int length = boy.length();
		if (sex == 2) {
			str = girl;
			length = girl.length();
		}
		index = getNum(0, length - 1);
		String second = str.substring(index, index + 1);
		int hasThird = getNum(0, 1);
		String third = "";
		if (hasThird == 1){
			index = getNum(0, length - 1);
			third = str.substring(index, index + 1);
		}
		return first + second + third;
	}

	/**
	 * 随机生成手机号
	 * @return
	 */
	public static String getMobile(){
		// 定义手机号
		String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");
		int index = getNum(0, telFirst.length - 1);
		String first = telFirst[index];
		String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
		String thrid = String.valueOf(getNum(1, 9100) + 10000).substring(1);
		String mobile = first + second + thrid;
		return mobile;
	}

	public static int getGender(){
		Random r=new Random();
		int a=r.nextInt()>0?1:2;
		return a;
	}
	
	public static String getOpenId(){
		UUID uuid = UUID.randomUUID();
		String openId = uuid.toString().replaceAll("-", "");
		return openId;
	}
	
}
