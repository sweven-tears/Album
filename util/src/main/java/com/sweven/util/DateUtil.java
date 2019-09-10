package com.sweven.util;

import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sweven on 2019/3/22.
 * Email:sweventears@Foxmail.com
 */
public class DateUtil {

    /**
     * 需要再 OnCreate() 方法中添加以下代码来解决
     * android.os.NetworkOnMainThreadException 异常
     * StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
     * StrictMode.setThreadPolicy(policy);
     *
     * @return date
     */
    public static Date getNetTime() {

        String webUrl = "http://www.ntsc.ac.cn";//中国科学院国家授时中心
        try {
            URL url = new URL(webUrl);
            URLConnection uc = url.openConnection();
            uc.setReadTimeout(3000);
            uc.setConnectTimeout(3000);
            uc.connect();
            long correctTime = uc.getDate();
            return new Date(correctTime);
        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }

    /**
     * @param year  year
     * @param month month
     * @return days of month
     */
    public static int getDaysOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.get(Calendar.DATE);
    }

    /**
     * @param year  year
     * @param month month
     * @return Get the day of the week for the 1st of the month
     */
    public static int getWeekdayOfMonthFirstDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, 22);
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return day == 0 ? 7 : day;
    }

    public static String getYearMonth() {
        Date date = DateUtil.getNetTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return year + (month < 10 ? "0" + month : month + "");
    }

    /**
     * @param year   year
     * @param month  month
     * @param day    day of month
     * @param hour   hour
     * @param minute minute
     * @param second second
     * @return get '昨天'.'星期一'.'3-6'...
     */
    public static String getDateString(int year, int month, int day, int hour, int minute, int second) {
        Date now = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (nowYear == year) {
            if (nowMonth == month) {
                if (day == nowDay) {
                    return (hour > 9 ? hour : "0" + hour) + ":" + (minute > 9 ? minute : "0" + minute);
                } else {
                    if (nowDay - day == 1) {
                        return "昨天";
                    } else if (nowDay - day < 7) {
                        return getWeek(year, month, day);
                    } else {
                        return (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day);
                    }
                }
            } else {
                nowDay = getDaysOfMonth(year, month) + nowDay;
                if (nowDay - day == 1) {
                    return "昨天";
                } else if (nowDay - day < 7) {
                    return getWeek(year, month, day);
                } else {
                    return (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day);
                }
            }
        } else {
            return year + "-" + (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day);
        }
    }

    /**
     * @param year   year
     * @param month  month
     * @param day    day of month
     * @param hour   hour
     * @param minute minute
     * @return get '昨天'.'星期一'.'3-6'...
     */
    public static String getDateString(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMonth = calendar.get(Calendar.MONTH) + 1;
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (nowYear == year) {
            if (nowMonth == month) {
                if (day == nowDay) {
                    return (hour > 9 ? hour : "0" + hour) + ":" + (minute > 9 ? minute : "0" + minute);
                } else {
                    if (nowDay - day == 1) {
                        return "昨天";
                    } else if (nowDay - day < 7) {
                        return getWeek(year, month, day);
                    } else {
                        return (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day);
                    }
                }
            } else {
                nowDay = getDaysOfMonth(year, month) + nowDay;
                if (nowDay - day == 1) {
                    return "昨天";
                } else if (nowDay - day < 7) {
                    return getWeek(year, month, day);
                } else {
                    return (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day);
                }
            }
        } else {
            return year + "-" + (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day);
        }
    }

    /**
     * @param time long time
     * @return get '昨天'.'星期一'.'3-6'...
     */
    public static String getDateString(long time) {
        Calendar now = Calendar.getInstance();
        int nowYear = now.get(Calendar.YEAR);
        int nowMonth = now.get(Calendar.MONTH) + 1;
        int nowDay = now.get(Calendar.DAY_OF_MONTH);

        Date date = new Date(time);
        Calendar put = Calendar.getInstance();
        put.setTime(date);
        int year = put.get(Calendar.YEAR);
        int month = put.get(Calendar.MONTH) + 1;
        int day = put.get(Calendar.DAY_OF_MONTH);
        int hour = put.get(Calendar.HOUR_OF_DAY);
        int minute = put.get(Calendar.MINUTE);

        if (nowYear == year) {
            if (nowMonth == month) {
                if (day == nowDay) {
                    return (hour > 9 ? hour : "0" + hour) + ":" + (minute > 9 ? minute : "0" + minute);
                } else {
                    if (nowDay - day == 1) {
                        return "昨天";
                    } else if (nowDay - day < 7) {
                        return getWeek(year, month, day);
                    } else {
                        return (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day);
                    }
                }
            } else {
                nowDay = getDaysOfMonth(year, month) + nowDay;
                if (nowDay - day == 1) {
                    return "昨天";
                } else if (nowDay - day < 7) {
                    return getWeek(year, month, day);
                } else {
                    return (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day);
                }
            }
        } else {
            return year + "-" + (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day);
        }
    }

    /**
     * @param year  year
     * @param month month
     * @param day   day od month
     * @return week
     */
    public static String getWeek(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        String[] array = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        return array[i - 1];
    }

}
