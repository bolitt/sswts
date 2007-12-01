package org.net9.redbud.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	/**
	 * return the current day as format yyyymmdd<br>
	 * written by golden@9#&sillywolf@9#,May,27,2007<br>
	 * 
	 * @return:the current day which format is yyyymmdd
	 */
	public static String getCurrentDay() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		String monthStr, dayStr;
		if (month < 10) {
			monthStr = "0" + Integer.toString(month);
		} else {
			monthStr = Integer.toString(month);
		}
		if (day < 10) {
			dayStr = "0" + Integer.toString(day);
		} else {
			dayStr = Integer.toString(day);
		}
		String dateFormat = Integer.toString(year) + monthStr + dayStr;
		return dateFormat;
	}
	
	/**
	 * @author urong@9#, 2007.08.04
	 * @return 用字符串表示的当前时间，以 "yyyy年MM月dd日 HH:mm:ss" 格式化
	 */
	public static String getCurrentTime(){
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.MEDIUM);
		Date date = new Date(System.currentTimeMillis());
		return dateFormat.format(date);
	}
	
	/**
	 * @author urong@9#, 2007.08.04
	 * @param t 与协调世界时 1970 年 1 月 1 日午夜之间的时间差，以毫秒为单位
	 * @return 用字符串表示的时间，以 "yyyy年MM月dd日 HH:mm:ss" 格式化
	 */
	public static String getFormatTime(long t){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		Date date = new Date(t);
		return sdf.format(date);
	}
	
	/**
	 * @author urong@9#, 2007.08.24
	 * @param t 与协调世界时 1970 年 1 月 1 日午夜之间的时间差，以毫秒为单位
	 * @return 用字符串表示的时间，以 "MM.dd HH:mm" 格式化
	 */
	public static String getSimpleFormatTime(long t){
		SimpleDateFormat sdf = new SimpleDateFormat("MM.dd HH:mm");
		Date date = new Date(t);
		return sdf.format(date);
	}
	
	/**
	 * @author urong@9#, 2007.08.07
	 * @param t 以 "yyyyMMddHH:mm:ss" 格式表示的时间
	 * @return 与协调世界时 1970 年 1 月 1 日午夜之间的时间差，以毫秒为单位
	 * @throws ParseException
	 */
	public static long getParseTime(String t){
		SimpleDateFormat bdf =  new SimpleDateFormat("yyyyMMddHH:mm:ss"); 
		bdf.setLenient(true);
		Date date = null;
		try{
			date = bdf.parse(t);			
			return date.getTime();
		}
		catch(ParseException e){
			return Long.MIN_VALUE;
		}
	}
}
