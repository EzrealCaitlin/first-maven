package com.liukang.study.constants;

/**
 * 常量类
 * 
 * @author liukang 
 * @date 2015年6月24日 下午4:23:06
 * @version 0.1.0
 * @copyright zuojian.com
 */
public final class Constant {
	/**
	 * 项目名
	 */
	public static final String PROJECT_NAME = "ccms";
	/**
	 * 缓存目录分隔符
	 */
	public static final String CACHE_SPLIT = ":";
	/**
	 * 缓存根目录
	 */
	public static final String CACHE_PREFIX = PROJECT_NAME + CACHE_SPLIT;
	/**
	 * 客户端主密钥缓存目录
	 */
	public static final String MKEY_KEY = CACHE_PREFIX + "app_main_key:";

	/** 单元网格编码 **/
	public static final String UNIT_GRID_CODE = "unit_grid";

	/** 建筑编码 **/
	public static final String BUILDINGS = "buildings";
	
	/** 专业部门编码 **/
	public static final String DEPARTMENT = "department";
	
	/** 市场主体编码 **/
	public static final String MARKET = "market";
	
	/** 管理事项编码 **/
	public static final String MATTER = "matter";
	
	/** 自然资源编码 **/
	public static final String NATURAL_RESOURCES = "natural_resources";
	/**
	 * 场所类型:出租屋
	 */
	public static final String SUBJECT_TYPE_RENTAL = "03";
	/**
	 * 场所类型:经营场所
	 */
	public static final String SUBJECT_TYPE_OPERATE_PLACE = "02";
	/**
	 * 场所类型:企业
	 */
	public static final String SUBJECT_TYPE_ENTERPRISE = "01";
	/**
	 * 场所类型:楼栋
	 */
	public static final String SUBJECT_TYPE_BUILDING = "04";
	/**
	 * RELATION_TYPE:楼层
	 */
	public static final String RELATION_TYPE_FLOOR = "02";
	/**
	 * RELATION_TYPE:建筑
	 */
	public static final String RELATION_TYPE_BUILDING = "01";
	/**
	 * RELATION_TYPE:房间
	 */
	public static final String RELATION_TYPE_ROOM = "03";
	/**
	 * 主体类型
	 */
	public static final String SUBJECT_TYPE = "SUBJECT_TYPE";
	/**
	 * 建筑物关联类型
	 */
	public static final String RELATION_TYPE = "RELATION_TYPE";
	/**
	 * E警通市民版－－帐号注册短信验证码
	 */
	public static final String REG_SMS = CACHE_PREFIX + "reg_sms";
	
	public static final String 用户报警_当前经纬度 = PROJECT_NAME+":ga_alarm_tracks:";
	/**
	 * 平安志愿者当日申请注册次数
	 */
	public static final String CITIZEN_REG_COUNT = PROJECT_NAME+":ga_reg_count:";
	/**
	 * 平安志愿者当日申请注册短信验证码
	 */
	public static final String CITIZEN_REG_SMS_CODE = PROJECT_NAME+":ga_reg_sms_code:";

}
