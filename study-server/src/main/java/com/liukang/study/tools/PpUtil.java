package com.liukang.study.tools;

import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.amass.framework.util.Str;

public class PpUtil {
	static final String PATH = "config.properties";
	private static Properties pp =null;
	static {
		try
		{
			pp = PropertiesLoaderUtils.loadProperties(new ClassPathResource(PATH));
		}catch(Exception e)
		{
			AppLog.error("加载配置文件失败:" + PATH, e);
		}
	}
	public static String getString(String name,String defaultValue)
	{
		String str = pp.getProperty(name);
		return Str.IsEmpty(str) ? defaultValue : str;
	}
	public static String getString(String name)
	{
		return getString(name, null);
	}
	public static int getInt(String name,Integer defaultValue)
	{
		String str = getString(name, null);
		return str == null ? defaultValue : Integer.valueOf(str);
	}
	public static int getInt(String name)
	{
		return getInt(name,null);
	}
	public static long getLong(String name,Long defaultValue)
	{
		String str = getString(name, null);
		return str == null ? defaultValue : Long.valueOf(str);
	}
	public static long getLong(String name)
	{
		return getLong(name,null);
	}
}
