package com.liukang.study.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class Form {
	private HttpServletRequest request;
	public Form(HttpServletRequest request)
	{
		this.request = request;
	}
	
	public long getPageNum()
    {
        return getLong("page",1l);
    }
	
	public long getPageSize()
    {
        return getLong("rows",10l);
    }
	
	public Object get(String key)
	{
		return request.getParameter(key);
	}
	
	public String getString(String key,String defaultValue)
	{
		Object re = get(key);
		return (re == null || "".equals(re)) ? defaultValue :  (String)re;
	}
	
	public String getString(String key)
	{
		return this.getString(key,null);
	}
	
	public Integer getInt(String key,Integer defaultValue)
	{
		String re = getString(key,null);
		return null == re ? null : Integer.parseInt(re);
	}
	
	public Integer getInt(String key)
	{
		return getInt(key,null);
	}
	
	public Long getLong(String key,Long defaultValue)
	{
		String re = getString(key,null);
		return null == re ? defaultValue : Long.parseLong(re);
	}
	
	public Long getLong(String key)
	{
		return getLong(key,null);
	}
	
	public Date getDate(String key,Date defaultValue)
	{
		String re = getString(key);
		if(re == null)
		{
			return null;
		}
		Date date = null;
		try {
			
			if(re.matches("^\\d{2,4}-\\d{2}-\\d{2}$"))
			{
				date = new SimpleDateFormat("yyyy-mm-dd").parse(re);
			}else if(re.matches("^\\d{2,4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$"))
			{
				date = new SimpleDateFormat("yyyy-mm-dd hh:mm").parse(re);
			}else if(re.matches("^\\d{2,4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$"))
			{
				date = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(re);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public Date getDate(String key)
	{
		return getDate(key,null);
	}

    @Override
    public String toString()
    {
        Map<String,Object> map = new HashMap<String, Object>();
        Enumeration<?> attrs = request.getParameterNames();
        
        String attr = null;
        while(attrs.hasMoreElements())
        {
            attr = attrs.nextElement()+"";
            map.put(attr, request.getParameter(attr));
        }
        return JsonUtil.createJsonString(map);
    }
	
}
