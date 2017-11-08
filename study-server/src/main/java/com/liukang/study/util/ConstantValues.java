package com.liukang.study.util;

/**
 * basic-api-server 所需要的常量类
 * 
 * @author Hely
 *
 */
public class ConstantValues {
	
	/** 项目名称 **/
	public static final String PROJECT = "BASIC-API-SERVER";
	public static final String FUNCTION_DEPTDISTRIBUTE = "DEPT_SUPMISSON";
	public static final String FUNCTION_FINDPUSH = "FINDPUSH_SUPMISSON";
	public static final String FUNCTION_FINDDISTRIBUTE = "FINDPUSH_SUPMISSON";
	
	// 临时目录存放地址 (win)
	public static final String TEMP_PATH_WINDOWS =PropertiesUtil.getString("TEMP_PATH_WINDOWS");
	// 临时目录存放地址 (linux)
	public static final String TEMP_PATH_LINUX = PropertiesUtil.getString("TEMP_PATH_LINUX");
	
	
	public static final String 结束流程 = "1";
	public static final String 继续反馈 = "0";
	private static final String FKNR_SPLIT = "#";

	//社区反馈结果
	public static final String 社区反馈_正常开业经营 = "fknr101";
	public static final String 社区反馈_无开门 = "fknr102";
	public static final String 社区反馈_地址与登记不符 = "fknr103";
	public static final String 社区反馈_查无此市场主体 = "fknr104";
	public static final String 社区反馈_其它情况 = "fknr105";
	public static final String 社区反馈_无证经营 = "fknr106";
	
	//部门反馈结果
	public static final String 部门反馈_已办理许可 = "fknr001";
	public static final String 部门反馈_无证经营_立案查处 = "fknr002";
	public static final String 部门反馈_无证经营_案件处理中 = "fknr003";
	public static final String 部门反馈_无证经营_已完成处罚 = "fknr004";
	public static final String 部门反馈_责令整改 = "fknr005";
	public static final String 部门反馈_整改处理中 = "fknr006";
	public static final String 部门反馈_许可办理中 = "fknr007";
	public static final String 部门反馈_没有开门 = "fknr008";
	public static final String 部门反馈_已撤销该关键词的经营范围 = "fknr009";
	public static final String 部门反馈_查无此市场主体 = "fknr010";
	public static final String 部门反馈_已纳入经营异常名录或黑名单 = "fknr011";
	public static final String 部门反馈_其它情况 = "fknr012";
	public static final String 部门反馈_已督促办证 = "fknr013";
	
	public static final String 部门反馈_没有开门_加_案件处理中 = 部门反馈_无证经营_案件处理中 + "+" + 部门反馈_没有开门;
	public static final String 部门反馈_没有开门_加_整改处理中 = 部门反馈_整改处理中 + "+" + 部门反馈_没有开门;
	public static final String 部门反馈_没有开门_加_已督促办证 = 部门反馈_没有开门 + "+" + 部门反馈_已督促办证;

	//镇平台自定义的反馈结果
	public static final String 自定义_无证经营 = "fkjg001";
	public static final String 自定义_许可办理中 = "fkjg002";
	public static final String 自定义_已办理许可 = "fkjg003";
	public static final String 自定义_已变更经营范围 = "fkjg004";
	public static final String 自定义_无证经营未整改 = "fkjg005";	
	public static final String 自定义_变更办理中 = "fkjg006";	
	public static final String 自定义_已变更经营地址 = "fkjg007";	
	public static final String 自定义_地址与登记不符未整改 = "fkjg008";	
	public static final String 自定义_整改处理中 = "fkjg009";	
	public static final String 自定义_已落实整改 = "fkjg010";	
	public static final String 自定义_未落实整改 = "fkjg011";
	
	
	
	//镇平台自定义的反馈结果
	//无证经营
	public static final String 修改版_许可办理中 = 自定义_无证经营+ FKNR_SPLIT + "01";
	public static final String 修改版_已办理许可 = 自定义_无证经营+ FKNR_SPLIT + "02";
	public static final String 修改版_已变更经营范围 = 自定义_无证经营+ FKNR_SPLIT + "03";
	public static final String 修改版_无证经营未整改 = 自定义_无证经营+ FKNR_SPLIT + "04";
	public static final String 修改版_部门已处理 = 自定义_无证经营+ FKNR_SPLIT + "05";
	
	//地址与登记不符
	public static final String 修改版_变更办理中 = 社区反馈_地址与登记不符+ FKNR_SPLIT + "01";
	public static final String 修改版_已变更经营地址 = 社区反馈_地址与登记不符+ FKNR_SPLIT + "02";
	public static final String 修改版_地址与登记不符未整改 = 社区反馈_地址与登记不符+ FKNR_SPLIT + "03";
	
	//其他情况
	public static final String 修改版_整改处理中 = 社区反馈_其它情况+ FKNR_SPLIT + "01";	
	public static final String 修改版_已落实整改 = 社区反馈_其它情况+ FKNR_SPLIT + "02";	
	public static final String 修改版_未落实整改 = 社区反馈_其它情况+ FKNR_SPLIT + "03";	
}
