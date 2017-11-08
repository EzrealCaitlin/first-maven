package com.liukang.study.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 处理日期时间的工具类<br>
 * 包括格式化和时间字符串之间的转换
 * 
 * @author xian.yq、huang.tao
 * @date 2013-8-7 上午10:26:41
 * @version 0.1.0 
 * @copyright yougou.com
 */
public class DateUtil {
	
	public static final String FORMAT_MONTH = "yyyyMM";
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_TIME = "HH:mm:ss";
	/**
	 * YYYY-MM-DD
	 */
	public static final SimpleDateFormat ymdformat = new SimpleDateFormat(
			"yyyy-MM-dd");
	
	/**
	 * 将时间格式化成: "yyyyMM"
	 * @param date 
	 * @return "2014-06-27"
	 */
	public static String formatMonth(Date date) {
		return format(date, FORMAT_MONTH);
	}
	/**
	 * 将时间格式化成: "yyyy-MM-dd"
	 * @param date 
	 * @return "2014-06-27"
	 */
    public static String formatDate(Date date) {
        return format(date, FORMAT_DATE);
    }
    
    /**
     * 将时间格式化成: "yyyy/MM/dd"
     * @param date 
     * @return "2014/06/27"
     */
    public static String format2(Date date) {
        return format(date, "yyyy/MM/dd");
    }
    
    /**
     * 将时间格式化成："HH:mm:ss"
     * @param date 
     * @return "16:46:32"
     */
    public static String formatTime(Date date) {
        return format(date, FORMAT_TIME);
    }
    
    /**
     * 将时间格式化成："yyyy-MM-dd HH:mm:ss"
     * @param date 
     * @return "2014-06-27 16:46:32" 
     */
    public static String formatDateTime(Date date) {
        return format(date, FORMAT_DATETIME);
    }
    
    /**
     * 获取当前时间, 并格式化成："yyyy-MM-dd HH:mm:ss" 输出
     * @return "2014-06-27 16:46:32"
     */
    public static String getCurrentDateTime2Str() {
		return format(new Date(), FORMAT_DATETIME);
	}
    
    /**
     * 获取当前时间, 并格式成："yyyyMMddHHmmss" 输出
     * @return "20140627164632"
     */
    public static String getCurrentDateTime2Str2() {
		return format(new Date(), "yyyyMMddHHmmss");
	}
    
    /**
     * 将<code>format["yyyy-MM-dd"]</code>字符串解析成对象
     * @param date "2014-06-27"
     * @return 
     * @throws Exception 
     */
    public static Date parseDate(String date) throws Exception {
        return parse(date, FORMAT_DATE);
    }
    
    /**
     * 将<code>format["yyyy-MM-dd HH:mm:ss"]</code>字符串解析成对象
     * @param date "2014-06-27 16:46:32"
     * @return
     * @throws Exception
     */
    public static Date parseTime(String date) throws Exception {
        return parse(date, FORMAT_DATETIME);
    }
    
    /**
     * 将<code>format["yyyy-MM-dd"]</code>字符串 + " 23:59:59" <br>
     * 然后再解析成对象
     * @param date
     * @return
     * @throws Exception
     */
    public static Date getMaxTimeByStringDate(String date) throws Exception {
    	String maxTime = date + " 23:59:59";
    	return parse(maxTime, FORMAT_DATETIME);
    }
    
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    public static int getMonth(java.util.Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static int getHour(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MINUTE);
    }

    public static int getSecond(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.SECOND);
    }

    public static long getMillis(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis();
    }

    public static int getWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        dayOfWeek = dayOfWeek - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        return dayOfWeek;
    }

    /**
     * 日期相加
     * @param date Date
     * @param day int
     * @return Date
     */
    public static Date addDate(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000);
        return c.getTime();
    }

    /**
     * 日期相减
     * @param date Date
     * @param date1 Date
     * @return int
     */
    public static int diffDate(Date date, Date date1) {
        return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
    }
    
    /**
     * 日期相减(返回秒值)
     * @param date Date
     * @param date1 Date
     * @return int
     * @author 
     */
    public static Long diffDateTime(Date date, Date date1) {
        return (Long) ((getMillis(date) - getMillis(date1))/1000);
    }
    
    
    public static Integer getLeftSeconds(String date)throws Exception{
    	Date condition = parse(date, FORMAT_DATETIME);
    	long n = condition.getTime();
    	long s = new Date().getTime();
    	return (int)((s-n)/1000);
    }
    
    /**
     * 获得时间戳
     * @return
     * @throws Exception
     */
	public static String getTime() {
		Date date = new Date();
		return String.valueOf(date.getTime());
	}
    
    
	/** 判断日期格式是否正确 */	
	public static boolean validateDate(String dateString){
		//使用正则表达式 测试 字符 符合 dddd-dd-dd 的格式(d表示数字)
		Pattern p = Pattern.compile("\\d{4}+[-]\\d{2}+[-]\\d{2}+");
		Matcher m = p.matcher(dateString);
		if(!m.matches()){	
			return false;
			} 
 		//得到年月日
		String[] array = dateString.split("-");
		int year = Integer.valueOf(array[0]);
		int month = Integer.valueOf(array[1]);
		int day = Integer.valueOf(array[2]);
		
		if(month<1 || month>12){	
			return false;
			}
		int[] monthLengths = new int[]{0, 31, -1, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		if(isLeapYear(year)){
			monthLengths[2] = 29;
		}else{
			monthLengths[2] = 28;
		}
		int monthLength = monthLengths[month];
		if(day<1 || day>monthLength){
			return false;	
		}
		return true;
	}
    
	/** 是否是闰年 */
	private static boolean isLeapYear(int year){
		return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) ;
	}	
	
	
    public static java.sql.Date parseSqlDate(Date date) {
        if (null == date) {
        	return null;
        }
        return new java.sql.Date(date.getTime());
    }
    
    /**
     * 时间格式化方法：<br>
     * 提供将时间转换成指定格式的字符串
     * 
     * @param date 
     * @param format 
     * @return
     */
    public static String format(Date date, String format) {
        String result = "";
        if (null == date) {
        	return result;
        }
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }
    
    /**
     * 时间字符串解析方法：<br>
     * 提供将指定格式的时间字符串解析成时间对象
     * 
     * @param date
     * @param format
     * @return
     * @throws Exception 
     */
    public static Date parse(String date, String format) throws Exception {
    	DateFormat df = new SimpleDateFormat(format);
    	return df.parse(date);
    }
    
    /**
     * 砍掉 <b>时分秒</b>  得到 ： yyyy-MM-dd
     * @param date
     * @return
     * @throws Exception
     */
    public static Date getDate(Date date) throws Exception {
    	return parseDate(formatDate(date));
    }
    
    /**
     * 判断是否为当前日期
     * @param date 
     * @return
     */
    public static boolean isCurrentDate(Date date) {
    	return formatDate(date).equals(formatDate(new Date()));
    }
    
    public static Date getNextDate(int days) 
	{
		return getNextDate(new Date(),days);
	}
    
    public static Date getNextDate2(String date ,int days) throws ParseException 
	{
    	DateFormat df = new SimpleDateFormat(FORMAT_DATE);
		return getNextDate(df.parse(date),days);
	}
	
	
	/**
	 * 按指定日期往前或往后推指定天数
	 * @param begin  指定日期
	 * @param step
	 * @return
	 */
	public static Date getNextDate(Date begin,int step) 
	{
		Calendar date = Calendar.getInstance();		
		date.setTime(begin);		
		date.set(Calendar.DATE, date.get(Calendar.DATE) + step);		
		Date endDate =  null;
		try {
			endDate = ymdformat.parse(ymdformat.format(date.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return endDate;
	}
	
	public static void main(String[] args) {
    	System.out.println(DateUtil.getNextDate(0));
    }
	
}
