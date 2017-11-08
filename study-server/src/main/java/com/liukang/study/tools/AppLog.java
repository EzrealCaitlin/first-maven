package com.liukang.study.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppLog {
	private static final Logger logger = LoggerFactory.getLogger(AppLog.class);
	public static void debug(String message){
	    logger.debug(message);
	}
	public static void debug(String message,Throwable ex){
	    logger.debug(message,ex);
	}
	
	public static void error(String message){
	    logger.error(message);
	}
	public static void error(String message,Throwable ex){
	    logger.error(message,ex);
	}
	
	public static void warn(String message){
	    logger.warn(message);
	}
	public static void warn(String message,Throwable ex){
	    logger.warn(message,ex);
	}
	
	public static void info(String message){
	    logger.error(message);
	}
	public static void info(String message,Throwable ex){
	    logger.error(message,ex);
	}
}
