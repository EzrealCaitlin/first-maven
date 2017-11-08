package com.liukang.study.tools;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 异步操作服务
 * @author LGN
 *
 */
public class PoolExceuterServer {
	//等待队列
	//private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(200);
	private static final ExecutorService pool = Executors.newFixedThreadPool(PpUtil.getInt("corePoolSize", 100));//= new ThreadPoolExecutor(PpUtil.getInt("corePoolSize", 100), PpUtil.getInt("maximumPoolSize", 200), PpUtil.getInt("keepAliveTime", 60),
	//TimeUnit.SECONDS, queue);
	
	/**
	 * 用线程池执行一个异步操作
	 * @param runnable
	 */
	public static void execute(Runnable runnable){
		pool.execute(runnable);	
	}
}
