package com.liukang.study.constants;

public class ConstantSystemValue {
	// 系统编码
	public static final String 部门巡检版 = "SYS0011";
	public static final String 社区巡检版 = "SYS0010";

	// admin_level等级划分
	public static final String ADMIN_LEVEL_COMMUNITY = "4";// 社区
	public static final String ADMIN_LEVEL_TOWN = "3";// 镇
	public static final String ADMIN_LEVEL_COUNTY = "2";// 区县
	public static final String ADMIN_LEVEL_CITY = "1";// 市级
	
	//帐号级别
	public static final String 督导版_非部门 = "SYS0020";
	public static final String 督导版_部门 = "SYS0021";
	
	//巡检事项审核状态
	public static final String ITEM_STATUS_镇领导审核 = "1";
	public static final String ITEM_STATUS_部门领导审核 = "2";
	public static final String ITEM_STATUS_社区领导审核 = "3";
	
	public static final String ITEM_STATUS_通过 = "4";
	public static final String ITEM_STATUS_不通过 = "5";
}
