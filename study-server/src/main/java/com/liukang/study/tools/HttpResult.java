package com.liukang.study.tools;

import java.util.HashMap;

public class HttpResult extends HashMap<String, Object>
{
    //操作成功
    public static final int SUCCESS = 200;
    //未登陆或登陆超时
    public static final int LOGIN = 201;
    //请求有误
    public static final int ERROR = 202;
    //请求有误
    public static final int NO_RIGHT = 208;
    //MAC校验失败
    public static final int MAC = 203;
    private static final long serialVersionUID = 1L;
    public HttpResult()
    {
        this.put("status", SUCCESS);
        this.put("message", "操作成功");
    }
    
    public void toError(String message)
    {
    	toError(ERROR, message);
    }
    public void toError(int status,String message)
    {
        this.put("status", status);
        this.put("message", message);
    }
    
    public void toMacError()
    {
        this.put("status", MAC);
        this.put("message", "MAC校验失败");
    }
    
    public void toSystemError()
    {
        this.put("status", ERROR);
        this.put("message", "系统错误,请联系管理员!");
    }
    
    public void toLogin()
    {
        this.put("status", LOGIN);
        this.put("message", "未登陆或登陆超时!");
    }
    
    public void setTotal(long total)
    {
        this.put("total", total);
    }
    
    public void setRows(Object rows)
    {
    	if(rows != null)
    	{
    		 this.put("rows", rows);
    	}
    }
    
    public void setModel(Object model)
    {
        this.put("model", model);
    }
}
