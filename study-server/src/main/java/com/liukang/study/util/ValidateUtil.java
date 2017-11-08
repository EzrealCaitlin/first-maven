package com.liukang.study.util;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import com.liukang.study.tools.SpringContextHolder;
import com.google.common.collect.Lists;

public class ValidateUtil {
	/**
	 * 坐标经度验证规则
	 */
	public static final String GMLNG_REGEX = "^11[234]\\.[0-9]{3,25}$";
	/**
	 * 坐标纬度验证规则
	 */
	public static final String GMLAT_REGEX = "^2[123]\\.[0-9]{3,25}$";
	/**
	 * 电话号码验证规则
	 */
	public static final String TELNO_REGEX = "^(13[0-9]|14[57]|15[0-35-9]|17[0]|18[02356789])[0-9]{8}$|^$";
	/**
	 * 固定电话号码验证规则
	 */
	public static final String PHONE_REGEX = "^(13[0-9]|14[57]|15[0-35-9]|17[0]|18[02356789])[0-9]{8}$";
	/**
	 * 密码验证规则
	 */
	public static final String PASSWORD_REGEX = "[0-9a-zA-Z([^<>])]{5,20}";
	/**
	 * 帐号验证规则
	 */
	public static final String USERNAME_REGEX = "[a-zA-Z][a-zZ-Z0-9]{3,20}";
	/**
	 * IP验证规则
	 */
	public static final String IP_REGEX = "(((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d)).(((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d)).(((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d)).(((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))";
	public static final String 社区编号 = "^4419001220[01][0-9]$";
	private static Validator validator = (Validator)SpringContextHolder.getBean("validator");
	
	public static void validateWithException(Object object, Class<?>... groups)
			throws ConstraintViolationException {
		Set constraintViolations = validator.validate(object, groups);
		if (!constraintViolations.isEmpty()) {
			throw new ConstraintViolationException(constraintViolations);
		}
	}
	
	/**
	 * 校验一个bean
	 * @param object
	 * @param groups
	 * @return 通过时返回null,不通过时返回校验失败的所有描述,用;隔开
	 */
	public static String validate(Object object, Class<?>... groups){
		return validate(object, ";", groups);
    	
	}
	
	/**
	 * 校验一个bean
	 * @param object
	 * @param spliter
	 * @param groups
	 * @return 通过时返回null,不通过时返回校验失败的所有描述,用spliter隔开
	 */
	public static String validate(Object object,String spliter, Class<?>... groups){
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
		StringBuffer sb = new StringBuffer();
		boolean ok = true;
    	for (ConstraintViolation<?> violation : constraintViolations) {
    		sb.append(violation.getMessage())
    		  .append(spliter);
    		ok = false;
		}
    	
    	if(ok)
    	{
    		return null;
    	}
    	return sb.toString();
    	
	}
	
	/**
	 * 辅助方法, 转换ConstraintViolationException中的Set<ConstraintViolations>为List<propertyPath +separator+ message>.
	 */
	public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e, String separator) {
		return extractPropertyAndMessageAsList(e.getConstraintViolations(), separator);
	}
	
	/**
	 * 辅助方法, 转换Set<ConstraintViolation>为List<propertyPath +separator+ message>.
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> extractPropertyAndMessageAsList(Set<? extends ConstraintViolation> constraintViolations,
			String separator) {
		List<String> errorMessages = Lists.newArrayList();
		for (ConstraintViolation violation : constraintViolations) {
			errorMessages.add(violation.getPropertyPath() + separator + violation.getMessage());
		}
		return errorMessages;
	}
}
