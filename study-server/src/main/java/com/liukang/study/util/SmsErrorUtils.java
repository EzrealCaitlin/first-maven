package com.liukang.study.util;

/**
 * 发送短信回执码错误解析
 * 
 * @author Hely
 *
 */
public class SmsErrorUtils {

	public static String parseSmsBackCode(String code) {
		String result = "";
		switch (Integer.parseInt(code)) {
		case 1:
			result = "非法登录，如登录名、口令出错、登录名与口令不符等。";
			break;
		case 2:
			result = "重复登录，如在同一TCP/IP连接中连续两次以上请求登录。";
			break;
		case 3:
			result = "连接过多，指单个节点要求同时建立的连接数过多。";
			break;
		case 4:
			result = "参数格式错，指命令中参数值与参数类型不符或与协议规定的范围不符。";
			break;
		case 5:
			result = "非法手机号码。";
			break;
		case 6:
			result = "消息ID错";
			break;
		case 7:
			result = "信息长度错";
			break;
		case 8:
			result = "非法序列号，包括序列号重复、序列号格式错误等";
			break;
		case 9:
			result = "余额不足";
			break;
		case 10:
			result = "短信内容中有非法字符";
			break;
		case 11:
			result = "短信内容太长";
			break;
		case 12:
			result = "手机号码数量错误";
			break;
		case 13:
			result = "发送速度太快";
			break;
		case 14:
			result = "超过最大发送上限";
			break;
		case 15:
			result = "系统失败";
			break;
		default:
			result = "其它错误码(待定义)";
			break;
		}
		return result;
	}
}
