package com.eiisys.ipcc.core.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class TimeUtils {
    
    private static ZoneId zoneIdOfChina = ZoneId.of("Asia/Shanghai");

    private static DateTimeFormatter ymFormatter = DateTimeFormatter.ofPattern("yyyyMM");
    private static DateTimeFormatter ymdFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    
	private static DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	private static DateTimeFormatter dtFormatterWithZone = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(zoneIdOfChina);
	
	
	public static void main(String[] arg) {
		
	    System.out.println(nextYearMonth(10));
	    
	    System.out.println("主叫开始:" + utcToChinaTime(""));
	    System.out.println("被叫开始:" + utcToChinaTime(""));
	    System.out.println("结束：" + utcToChinaTime("2018-09-25 01:27:59"));
	}
	
	public static long hUtcToSecond(String huaweiDateTime) {
		long epochSeconds = 0;
		
		try {
		    if (!StringUtils.isEmpty(huaweiDateTime)) {
		        epochSeconds = LocalDateTime.parse(huaweiDateTime, dtFormatter).toEpochSecond(ZoneOffset.UTC);
		    }
            
        } catch (Exception e) {
            // TODO: handle exception
        }
		
		return epochSeconds;
	}
	
	/**
	 * 将yyyyMMdd转换为yyyy-MM-dd
	 * @param yyyyMMdd
	 * @return
	 */
	public static String formatDate(String yyyyMMdd) {
//	    return LocalDate.parse(ymd, ymdFormatter).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));// slow
	    if (yyyyMMdd == null || yyyyMMdd.length() != 8) {
	        return yyyyMMdd; 
	    }
	    
	    return yyyyMMdd.substring(0, 4) + "-" + yyyyMMdd.substring(4,6) + "-" + yyyyMMdd.substring(6);
	}
	
	/**
	 * 当前系统时间
	 * @return ISO instant格式系统时间 中国东部时间
	 */
	public static String currentTime() {
//		return Instant.now().toString().replaceFirst("T", " ").replaceFirst("Z", "");
	    return LocalDateTime.now(zoneIdOfChina).toString().replaceFirst("T", " ").replaceFirst("Z", "");
	}
	
	/**
	 * 返回当前年月 (中国东部时间)
	 * yyyyMM格式
	 * @return
	 */
	public static String currentYearMonth() {
	    return LocalDate.now(zoneIdOfChina).format(ymFormatter);
	}
	
	/**
	 * 返回之前年月(中国东部时间)
	 * @param monthsToSubtract 减去的月份
	 * @return
	 */
	public static String preYearMonth(long monthsToSubtract) {
	    if (monthsToSubtract < 0) {
	        return currentYearMonth();
	    }
	    
	    return LocalDate.now(zoneIdOfChina).minusMonths(monthsToSubtract).format(ymFormatter);
	}
	
	/**
     * 返回今日之后年月(中国东部时间)
     * @param monthsToAdd 加的月份
     * @return
     */
    public static String nextYearMonth(long monthsToAdd) {
        if (monthsToAdd < 0) {
            return currentYearMonth();
        }
        
        return LocalDate.now(zoneIdOfChina).plusMonths(monthsToAdd).format(ymFormatter);
    }
	
	/**
     * 返回当前年月日 (中国东部时间)
     * yyyyMMdd格式
     * @return
     */
    public static String currentDate() {
        return LocalDate.now(zoneIdOfChina).format(ymdFormatter);
    }
    
    /**
     * 返回往日 (中国东部时间)
     * @param daysToSubtract 减去的天数
     * @return
     */
    public static String preDate(long daysToSubtract) {
        if (daysToSubtract < 0) {
            return currentDate();
        }
        
        return LocalDate.now(zoneIdOfChina).minusDays(daysToSubtract).format(ymdFormatter);
    }
    
    public static long between(String startDate, String endDate) {
        LocalDate startLocalDate = LocalDate.parse(startDate, ymdFormatter);
        LocalDate endLocalDate = LocalDate.parse(endDate, ymdFormatter);
        return endLocalDate.toEpochDay() - startLocalDate.toEpochDay();
    }
    
    public static String utcToChinaTime(String utcTime) {
        String cst = null;
        
        if (!StringUtils.isEmpty(utcTime)) {
            cst = LocalDateTime.parse(utcTime, dtFormatter).plusHours(8).toString().replaceFirst("T", " ");
        }
        
        return cst;
    }
	
	/**
	 * 将秒转为分钟
	 * <p> 不到60秒算1分钟，以此类推
	 * @param seconds
	 * @return
	 */
	public static int secToMin(int seconds) {
	    int minutes = 0;
	    
        if (seconds > 0) {
            minutes = seconds / 60;

            if (seconds % 60 > 0) {// 有超出的秒
                minutes += 1;
            }
        }
	    
	    return minutes;
	}
	
	public static String dateToString(Date date) {
	    if (date == null) {
	        return "";
	    }
	    
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    return formatter.format(date);
	}
	
    public static String datetimeToString(Date date) {
        if (date == null) {
            return "";
        }

        return dtFormatterWithZone.format(date.toInstant());
    }
	
	/**
	 *  月份有几天
	 * @param yearMonth yyyyMM
	 * @return
	 */
	public static int lengthOfMonth(String yearMonth) {
	    
	    int year = Integer.parseInt(yearMonth.substring(0, 4));
	    int month = Integer.parseInt(yearMonth.substring(4));
	    
	    return YearMonth.of(year, month).lengthOfMonth();
	}
	
//	public static List<String> ymdBetweenFormat(String month) {
//        List<String> list = new ArrayList<>();
//        if (d1 == null || d2 == null) {
//            return list;
//        }
//        Date da1;
//        Date da2;
//        if (d1.getTime() < d2.getTime()) {
//            da1 = d1;
//            da2 = d2;
//        } else {
//            da1 = d2;
//            da2 = d1;
//
//        }
//        for (Date d = d1; getDateDiff(d, d2) >= 0; d = diffDay(d, 1)) {
//            list.add(ymdFormat(d));
//        }
//        return list;
//    }
	
    
//	/**
//	 * String 转 Date类型  , 必须符合ISO instant格式的
//	 * @param utcDateTime 
//	 * @return
//	 */
//	public static Date strToDate(String utcDateTime) {
//		LocalDateTime localDateTime = null;
//		Instant timeInstant = null;
//		
//		try {
//			localDateTime = LocalDateTime.parse(utcDateTime, utcDtFormatter);
//			timeInstant = localDateTime.toInstant(ZoneOffset.UTC);
//		} catch (DateTimeParseException e) {
//			
//		}
//		
//		if (timeInstant != null) {
//			return Date.from(timeInstant);
//		} else {
//			return null;
//		}
//
//	}
//	
//	/**
//	 * 日期格式转String
//	 * @param date utc时间
//	 * @return ISO instant格式时间
//	 */
//	public static String dateToStr(Date date) {
//		if (date == null) {
//			return "";
//		}
//		
//		Instant instant = date.toInstant();
//
//		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, utcZoneId);
//		return localDateTime.format(utcDtFormatter);
//	}
}
