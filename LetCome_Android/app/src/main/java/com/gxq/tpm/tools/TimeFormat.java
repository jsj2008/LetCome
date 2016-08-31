package com.gxq.tpm.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.letcome.R;

public class TimeFormat {
	
	/**
	 * 时间戳格式化,时:分
	 * @param millisecond  时间戳
	 * @return   HH:mm
	 */
	public static String milliToHourMinute(long millisecond) {
		return milliToFormat(millisecond, "HH:mm");
	}

	/**
	 * 时间戳格式化,时:分:秒
	 * @param millisecond  时间戳
	 * @return   HH:mm:ss
	 */
	public static String milliToHourSecond(long millisecond) {
		return milliToFormat(millisecond, "HH:mm:ss");
	}
	
//	/**
//	 * 时间戳格式化,月-日
//	 * @param millisecond 时间戳
//	 * @return MM-dd
//	 */
//	public static String milliToMonthDateShort(long millisecond) {
//		return milliToFormat(millisecond, "MM-dd");
//	}
	/**
	 * 时间戳格式化,月-日 
	 * @param millisecond
	 * @return  MM-dd 
	 */
	public static String milliToMonthMonth(long millisecond) {
		return milliToFormat(millisecond, "MM-dd");
	}
	/**
	 * 时间戳格式化,月-日  时:分
	 * @param millisecond
	 * @return  MM-dd HH:mm
	 */
	public static String milliToMonthMinute(long millisecond) {
		return milliToFormat(millisecond, "MM-dd HH:mm");
	}
  
	/**
	 * 时间戳格式化,月-日  时:分:秒
	 * @param millisecond
	 * @return  MM-dd HH:mm:ss
	 */
	public static String milliToMonthSecond(long millisecond) {
		return milliToFormat(millisecond, "MM-dd HH:mm:ss");
	}
	
//	/**
//	 * 时间戳格式化,年-月-日  时:分
//	 * @param millisecond
//	 * @return  yyyy-MM-dd HH:mm
//	 */
//	public static String milliToYearMinute(long millisecond) {
//		return milliToFormat(millisecond, "yyyy-MM-dd HH:mm");
//	}
	
	/**
	 * 时间戳格式化,年-月-日  时:分:秒
	 * @param millisecond
	 * @return  yyyy-MM-dd HH:mm:ss
	 */
	public static String milliToYearSecond(long millisecond) {
		return milliToFormat(millisecond, "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 时间戳格式化,年-月-日
	 * @param millisecond
	 * @return  yyyy-MM-dd
	 */
	public static String milliToYearDate(long millisecond) {
		return milliToFormat(millisecond, "yyyy-MM-dd");
	}
	
	/**
	 * 时间戳格式化,年月日
	 * @param millisecond
	 * @return  yyyyMMdd
	 */
	public static String milliToYearDateOther(long millisecond) {
		return milliToFormat(millisecond, "yyyyMMdd");
	}
	
	// 当年MM-DD hh:mm 往年 YYYY-MM-DD
    public static String dateDefaultFormat(long time) {
    	Date date = new Date(System.currentTimeMillis());
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    	
    	String curDateStr = df.format(date);
    	
    	String dateStr = df.format(new Date(time * 1000));
    	
    	if (curDateStr.substring(0, 4).equals(dateStr.substring(0, 4))) {
    		return dateStr.substring(5);
    	} else {
    		return dateStr.substring(0, 10);
    	}
    }
 // 当年MM-DD hh:mm:ss 往年 YYYY-MM-DD
    public static String dateDefaultFormatSecond(long time) {
    	Date date = new Date(System.currentTimeMillis());
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    	
    	String curDateStr = df.format(date);
    	
    	String dateStr = df.format(new Date(time * 1000));
    	
    	if (curDateStr.substring(0, 4).equals(dateStr.substring(0, 4))) {
    		return dateStr.substring(5);
    	} else {
    		return dateStr.substring(0, 10);
    	}
    }
  //YYYY-MM-DD hh:mm
    public static String dateDefaultAllFormat(long time) {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    	return df.format(new Date(time * 1000));
    }
    public static long strTimeToMillisecond(String time, String pattern) {
    	SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);
    	Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			return 0;
		}
		
		return date.getTime();
    }
    
    public static long klineTimeToMillisecond(String time) {
    	if (time == null || time.length() == 0)
    		return 0;
    	return strTimeToMillisecond(time, "yyyy-MM-dd HH:mm:ss");
    }
	
	public static String milliToFormat(long millisecond, String format) {
		if (millisecond == 0)
			return "";
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		if (millisecond < 1000000000000L) {
			millisecond *= 1000;
		}
		date.setTime(millisecond);
		return sdf.format(date);
	}
	
	/**
	 * 时间为0，返回默认值"--"
	 */
	public static String timeDefaultValue(long time) {
		String timeStr = time == 0 ? Util.transformString(R.string.default_value)
				: TimeFormat.milliToMonthSecond(time);
		return timeStr;
	}
}
