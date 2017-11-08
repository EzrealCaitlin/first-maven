package com.liukang.study.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import com.liukang.study.model.JsonMapBean;
import com.amass.framework.util.Function;

public class WarnUtil {
	static final String URL = "http://sc.ftqq.com/SCU7936T7ebc2f7ab56d03fccfebfffcfc3e8f5159029130e20e5.send";
	public static final String CONTENT_TYPE_DEFAULT = "application/x-www-form-urlencoded; charset=UTF-8";
	public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
	private static final String CHARSET = "UTF-8";
	private static final boolean DEBUG = PropertiesUtil.getInt("WARN_DEBUG",0) == 1;
	private static final String CONTEXT = PropertiesUtil.getString("SYSTEM_CONTEXT","商改监管任务下载");
	
	//是否使用代理访问网络
	private static final boolean USE_PROXY = PropertiesUtil.getInt("USE_PROXY",0) == 1;
	
	//代理服务器地址
	private static final String PROXY = PropertiesUtil.getString("PROXY","192.168.1.251:808");
	
	private static void post(String title,Object content) 
	{
		PostMethod postMethod = new PostMethod(URL);
		try {
			HttpClient client = new HttpClient();
			if(USE_PROXY)
			{
				String[] proxy = Function.split(PROXY, ":");
				client.getHostConfiguration().setProxy(proxy[0], Integer.parseInt(proxy[1]));
			}
			
			String desp = "";
			if(content instanceof String)
			{
				desp = (String)content;
			}else
			{
				desp = JsonMapBeanUtil.toJson(content);
			}
			
			
			//设置请求对象头信息
			postMethod.setRequestHeader("Content-Type",CONTENT_TYPE_DEFAULT);
			postMethod.setRequestHeader("Accept-Charset",CHARSET);
			postMethod.setParameter("text", title);		
			postMethod.setParameter("desp", desp);	
			
			//发送请求
			client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
			int status = client.executeMethod(postMethod);
			
			if (status == HttpStatus.SC_OK) {
				String responseBody = "";
				responseBody = new String(postMethod.getResponseBody(),CHARSET);
				JsonMapBean response = JsonMapBean.fromJson(responseBody);
				
				System.out.println(responseBody);
				
				if(response == null)
				{
					throw new Exception("返回结果为空!");
				}
			}else
			{
				System.out.println("send fail! status:" + status);
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
	}
	
	
	/**
	 * 服务器报警工具,用于服务器出现异常时向指定微信帐号推送报警信息,具体见 @link http://sc.ftqq.com/
	 * @param text 标题
	 * @param desp 内容,如果不是字符中，将转为json格式
	 */
	public static void execute(String text,Object desp){
		text = CONTEXT + ":" + text;
		if(DEBUG)
		{
			System.out.println(text + ":" + desp);
			return;
		}
		Executer model = new Executer(text,desp);
		Thread thread = new Thread(model);
		thread.start();
	}
	
	private static class Executer implements Runnable{
		String text;
		Object desp;
		public Executer(String title,Object content) {
			this.text = title;
			this.desp = content;
		}
		public void run() {
			post(text, desp);
		}
	}
}
