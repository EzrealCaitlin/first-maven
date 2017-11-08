package com.liukang.study.util;
/**
 * 基础要素编码规则工具类
 * @author Hely
 *
 */
public class ElementsUtil {
	
	/** 建筑物对象 **/
	public static String OBJECT_BUILDINGS = "040";
	
	/** 楼层对象 **/
	public static String OBJECT_FLOOR = "041";
	
	
//	建筑物	040
//	建筑物楼层	041
//	建筑物内部房间	042
//	人口	050
//	水表	060
//	电表	061
//	市场主体	070
//	水资源	080
//	土地资源	081
//	专业部门	090
//	管理事项	091
//	部件	100
//	事件	101
//	房屋安全信用	110
//	市场主体信用	111
//	个人综合信用	112
//	政府部门信用	113
//	人员角色	120

	/** 单元网格编码长度**/
	public static int UNIT_GRID_LENGTH = 2;
	
	/** 建筑物编码长度**/
	public static final int BUILDINGS_LENGTH = 9;
	
	/** 专业部门编码 **/
	public static final int DEPARTMENT_LENGTH = 4;
	
	/** 市场主体编码 **/
	public static final int MARKET_LENGTH = 9;
	
	/** 管理事项编码 **/
	public static final int matter_LENGTH = 2;
	
	/** 自然资源编码 **/
	public static final int NATURAL_RESOURCES_LENGTH = 6;
	
	/** 人口编码 **/
	//public static final int POPULATION_LENGTH = "population";
	
	
	/**
	  * 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回
	  * @param sourceDate
	  * @param formatLength
	  * @return 重组后的数据
	  */
	 public static String frontCompWithZore(int sourceDate,int formatLength)
	 {
	  /*
	   * 0 指前面补充零
	   * formatLength 字符总长度为 formatLength
	   * d 代表为正数。
	   */
	  String newString = String.format("%0"+formatLength+"d", sourceDate);
	  return  newString;
	 }

}
