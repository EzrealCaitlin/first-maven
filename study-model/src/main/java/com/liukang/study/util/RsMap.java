package com.liukang.study.util;

import java.io.Serializable;
import java.util.HashMap;

public class RsMap extends HashMap<String,Object> implements Serializable {
	private static final long serialVersionUID = 7987461225579121548L;
	public RsMap() {
		super();
	}
	
	/**
	 * 从map里面取一个字段串类型的值
	 * @param key
	 * @return  
	 */
	public String getString(String key) {
		return get(key)+"";
	}

	/**
	 * 从map里面取一个字段串类型的值,不存在时用默认值代替
	 * @param key
	 * @param defaultValue 不存在时的默认值
	 * @return
	 */
	public String getString(String key, String defaultValue) {
		String re = getString(key);
		if (re == null || re.equals("null")) {
			re = defaultValue;
		}
		return re;
	}

	public int getInt(String key) {
		return getInt(key, 0);
	}

	public int getInt(String key, int defaultValue) {
		Object v = this.get(key);
		if(null == v)
		{
			return defaultValue;
		}
		if("".equals(v))
		{
			return defaultValue;
		}
		return v == null ? defaultValue : 
			   v instanceof Double ? ((Double)v).intValue() :
			   v instanceof String ? Integer.parseInt(v.toString()) :
			   v instanceof Integer ? ((Integer)v) : defaultValue;
	}

	public Long getLong(String key) {
		return getLong(key, 0L);
	}

	public Long getLong(String key, Long defaultValue) {
		Object v = this.get(key);
		if(null == v)
		{
			return defaultValue;
		}
		if("".equals(v))
		{
			return defaultValue;
		}
		return v instanceof Double ? ((Double)v).longValue() :
			   v instanceof String ? Long.parseLong(v.toString()) :
			   v instanceof Integer ? Long.parseLong(v.toString()) :
			   v instanceof Long ? ((Long)v) : defaultValue;
	}
	
	public double getDouble(String key) {
		return getDouble(key,0.0);
	}
	
	public double getDouble(String key, double defaultValue) {
		Object v = this.get(key);
		if(null == v)
		{
			return defaultValue;
		}
		if("".equals(v))
		{
			return defaultValue;
		}
		return v == null ? defaultValue : 
			   v instanceof Double ? ((Double)v) :
			   v instanceof String ? Double.parseDouble(v.toString()) : defaultValue;
	}
	
	@Override
	public String toString()
	{
		return super.toString();
	}
}
