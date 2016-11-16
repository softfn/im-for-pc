package com.only.common.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 *
 * @author 夏辉
 * @date 2014年6月14日 下午10:31:41
 * @version 0.0.2
 */
public class OnlyDateUtil {

    private static final NumberFormat numberFormat = new DecimalFormat("00");

    /**
     * 获取当前日期时间精确到秒<br>
     * 格式 2013-04-01 11:44:22
     *
     * @return String
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    /**
     * 获取当前时间精确到分钟<br>
     * 格式 2011-06-30 12:30
     *
     * @return String
     */
    public static String getCurrentDateHourMinute() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(new Date());
    }

    /**
     * 获取当前时间精确到小时<br>
     * 格式 2011-06-30 12
     *
     * @return String
     */
    public static String getCurrentDateHour() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        return dateFormat.format(new Date());
    }

    /**
     * 获取当前日期<br>
     * 格式2013-04-01
     *
     * @return String
     */
    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    /**
     * 获取当前时间精确到秒<br>
     * 格式 10:14:33
     *
     * @return String
     */
    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(new Date());
    }

    /**
     * 获得当前年月 <br>
     * 格式 2011-06
     *
     * @return String
     */
    public static String getCurrentYearMonth() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        return dateFormat.format(new Date());
    }

    /**
     * 获取当前年<br>
     * 格式2013
     *
     * @return String
     */
    public static String getCurrentYear() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        return dateFormat.format(new Date());
    }

    /**
     * 获取当前月<br>
     * 格式04
     *
     * @return String
     */
    public static String getCurrentMonth() {
        SimpleDateFormat dFormat = new SimpleDateFormat("MM");
        return dFormat.format(new Date());
    }

    /**
     * 获取当前天<br>
     * 格式 10
     *
     * @return String
     */
    public static String getCurrentDay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        return dateFormat.format(new Date());
    }

    /**
     * 时间转字符日期时间精确到秒<br>
     * 格式2013-04-01 11:44:22
     *
     * @return String
     */
    public static String dateToDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    /**
     * 时间转字符日期<br>
     * 格式2013-04-01
     *
     * @return String
     */
    public static String dateToDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * 时间转字符时间<br>
     * 格式 10:14:33
     *
     * @return String
     */
    public static String dateToTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(date);
    }

    /**
     * 时间转字符年<br>
     * 格式2013
     *
     * @return String
     */
    public static String dateToYear(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        return dateFormat.format(date);
    }

    /**
     * 时间转字符月<br>
     * 格式04
     *
     * @return String
     */
    public static String dateToMonth(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        return dateFormat.format(date);
    }

    /**
     * 时间转字符天<br>
     * 格式04
     *
     * @return String
     */
    public static String dateToDay(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        return dateFormat.format(date);
    }

    /**
     * 时间转字符
     *
     * @param date
     * @param format
     * @return String
     */
    public static String dateToString(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * yyyy-MM-dd HH:mm:ss转Date
     *
     * @param dateTime
     * @return Date
     * @throws ParseException
     */
    public static Date stringDateTimeToDate(String dateTime) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse(dateTime);
        return date;
    }

    /**
     * yyyy-MM-dd转Date
     *
     * @param time
     * @return Date
     * @throws ParseException
     */
    public static Date stringDateToDate(String time) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(time);
        return date;
    }

    /**
     * String(格式 2011-06-30 12:20:22)转换成时间戳
     *
     * @param dateTime
     * @return long
     */
    public static long stringDateTimeToLong(String dateTime) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long date = dateFormat.parse(dateTime).getTime();
        return date;
    }

    /**
     * String(格式 2011-06-30)转换成时间戳
     *
     * @param dateTime
     * @return long
     */
    public static long stringDateToLong(String dateTime) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        long date = df.parse(dateTime).getTime();
        return date;
    }

    /**
     * 时间戳转换成String
     *
     * @param time
     * @return String
     */
    public static String longToString(long time, String format) {
        String date = new SimpleDateFormat(format).format(time);
        return date;
    }

    /**
     * 时间戳转换成String<br>
     * 格式 2011-06-30 12:20:22
     *
     * @param time
     * @return String
     */
    public static String longToDateTimeString(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
    }

    /**
     * 时间戳转换成String 格式 2011-06-30
     *
     * @param time
     * @return String
     */
    public static String longToDateString(long time) {
        return new SimpleDateFormat("yyyy-MM-dd").format(time);

    }

    /**
     * 获取某年某月最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthLastDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DATE);
    }

    /**
     * 获取某年某月最后一天
     *
     * @param year
     * @param month
     * @return int
     */
    public static int getMonthLastDay(String year, String month) {
        return getMonthLastDay(Integer.parseInt(year), Integer.parseInt(month));
    }

    /**
     * 获取某年某月最后一天
     *
     * @param year
     * @param month
     * @return int
     */
    public static int getLastDay(int year, int month) {
        return getMonthLastDay(year, month);
    }

    /**
     * 获取某年某月最后一天
     *
     * @param year
     * @param month
     * @return String
     */
    public static String getLastDay(String year, String month) {
        int last = getMonthLastDay(Integer.parseInt(year), Integer.parseInt(month));
        return numberFormat.format(last);
    }

    /**
     *
     * @param monthOrDay
     * @return
     */
    public static String intMonthOrDayToString(int monthOrDay) {
        return numberFormat.format(monthOrDay);
    }

    /**
     * 获取指定天数后日期<br>
     * 格式2011-06-30
     *
     * @return String
     */
    public static String getDate(int day) {
        Date nowDate = new Date();
        long nowTime = (nowDate.getTime()) + (1000 * 60 * 60 * 24 * day);
        nowDate.setTime(nowTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(nowDate);
    }

    /**
     * 获取指定天数后日期
     *
     * @return String
     */
    public static String getDate(int day, String format) {
        Date nowDate = new Date();
        long nowTime = (nowDate.getTime()) + (long) (1000 * 60 * 60 * 24 * day);
        nowDate.setTime(nowTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(nowDate);
    }

    /**
     * 获取当前星期几
     *
     * @return int
     */
    public static int getCurrentDayOfTheWeek() {
        Calendar calendar = Calendar.getInstance();
        //calendar.setFirstDayOfWeek(Calendar.MONDAY);// 以周一为每周第一天
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取当前日期是当年的第几周
     *
     * @param startDate
     * @return int
     * @throws ParseException
     */
    public static int getCurrentWeek() {
        Calendar calendar = Calendar.getInstance();
        //calendar.setFirstDayOfWeek(Calendar.MONDAY);// 以周一为每周第一天
        calendar.setTime(new Date());
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取指定日期是当年的第几周
     *
     * @param startDate
     * @return int
     * @throws ParseException
     */
    public static int getWeek(String startDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(startDate);
        Calendar calendar = Calendar.getInstance();
        //calendar.setFirstDayOfWeek(Calendar.MONDAY);// 以周一为每周第一天
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * @Description: 获取两个日期之间的间隔天数
     * @param start
     * @param end
     * @return int
     * @throws ParseException
     */
    public static int getBetweenDays(String start, String end) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = format.parse(start);
        Date endDate = format.parse(end);
        long days = (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
        int betweenDays = Integer.parseInt(days + "");
        return betweenDays;
    }

    // //////////////////////////////////////////////////
    /**
     * 比较日期大小d1<d2
     *
     * @return
     */
    public static boolean compareDate(String date1, String date2) {
        boolean flag = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);
            flag = d1.before(d2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 比较日期大小d1<d2
     *
     * @return
     */
    public static boolean compareDateTime(String dateTime1, String dateTime2) {
        boolean flag = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            Date d1 = sdf.parse(dateTime1);
            Date d2 = sdf.parse(dateTime2);
            flag = d1.before(d2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static void main(String a[]) {
        System.out.println(getDate(4));
        //		try {
        //			System.out.println(stringDateTimeToDate("2012-12-01 00:00:00"));
        //		} catch (ParseException e) {
        //			// TODO Auto-generated catch block
        //			e.printStackTrace();
        //		}
    }
}
