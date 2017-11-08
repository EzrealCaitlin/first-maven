package com.liukang.study.db;

/**
 * 数据源操作
 * 
 * @author liukang
 * @date 2015年9月11日 下午9:35:44
 * @version 0.1.0 
 * @copyright liukang.com
 */
public class DataSourceHolder {
	
	//线程本地环境
    private static final ThreadLocal<String> dataSources = new ThreadLocal<String>();
    //设置数据源
    public static void setDataSource(String customerType) {
        dataSources.set(customerType);
    }
    //获取数据源
    public static String getDataSource() {
    	String s = (String) dataSources.get();
    	System.out.println("===============>" + s);
        return s;
    }
    //清除数据源
    public static void clearDataSource() {
        dataSources.remove();
    }
}
