package com.liukang.study.tools;

import com.amass.logger.model.AmassLogger;
import com.amass.logger.server.AmassLoggerServer;

public class LoggerCachePool {
	private static LoggerCachePool instance;
	//private List<AmassLogger> pool;
	//private static final int poolSize = 30;
	private  LoggerCachePool()
	{
		//pool = new ArrayList<AmassLogger>();
	}
	public static LoggerCachePool getInstance(){
		synchronized (LoggerCachePool.class) {
			if(instance == null)
			{
				instance = new LoggerCachePool();
			}
		}
		
		return instance;
	}
	
//	public void putCacheItem(final AmassLogger item){
//		PoolExceuterServer.execute(new Runnable() {
//			@Override
//			public void run() {
//				try{
//					AmassLoggerServer server = SpringContextHolder.getBean(AmassLoggerServer.class);
//					server.add(item);
//				}catch(Exception e)
//				{
//					//调用日志服务器出错，改为写本地日志文件
//					AppLog.debug("调用日志服务出错");
//					//AppLog.debug(JsonUtil.createJsonString(logger));
//				}
//			}
//		});
//	}
	
	/*
	public void putCacheItem(final AmassLogger item){
		pool.add(item);
		if(pool.size() % poolSize == 0)
		{
			final List<AmassLogger> list = new LinkedList<AmassLogger>();
			synchronized(LoggerCachePool.class)
			{
	            for (int i=0; i<pool.size(); i++) {
	            	list.add(pool.get(i));
	            }
				pool.clear();
			}
			PoolExceuterServer.execute(new Runnable() {
				@Override
				public void run() {
					try{
						AmassLoggerServer server = SpringContextHolder.getBean(AmassLoggerServer.class);
						server.batchInsert(list);
					}catch(Exception e)
					{
						//调用日志服务器出错，改为写本地日志文件
						AppLog.debug("调用日志服务出错");
						//AppLog.debug(JsonUtil.createJsonString(logger));
					}
				}
			});
		}
	}
	*/
		
}
