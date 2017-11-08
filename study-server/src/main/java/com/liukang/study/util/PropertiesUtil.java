package com.liukang.study.util;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
	public static final String SYSTEM_BASE_DIR = "user.dir";
	private static final String CONFIG = "config.properties";
	private static final Properties pp = new Properties();
	private static Class<PropertiesUtil> c = PropertiesUtil.class;
	
	public static final String 商事指南办事流程 = "SGXT_BSZN_URL";
	public static final String 商改平台_调试模式 = "SGXT_DEBUG";
	
	static{
		try {
			pp.load(c.getClassLoader().getResourceAsStream(CONFIG));
		} catch (IOException e) {
			System.out.println("无法读取配置文件 : " + CONFIG);
		}
	}
	
	public static String getString(String key,String defaultValue)
	{
		String re = pp.getProperty(key);
		if(null == re || "".equals(re.trim()))
		{
			return defaultValue;
		}
		return re;
	}
	
	public static String getString(String key){
		return getString(key, null);
	}
	
	public  static Integer getInt(String key,Integer defaultValue)
	{
		String re = pp.getProperty(key);
		if(null == re || "".equals(re.trim()))
		{
			return defaultValue;
		}
		return new Integer(re);
	}
	
	public static Integer getInt(String key)
	{
		return getInt(key,null);
	}
	
	public static void main(String[] args) {
//		System.out.println(PropertiesUtil.getString("SGXT_TOUNTACCOUNT"));
	}
}
