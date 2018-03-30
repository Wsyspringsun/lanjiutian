package utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/1 0001.
 */

public class DateUtils {

    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final int MIN_YEAR = 1970;
    public static final int MAX_YEAR = 2099;

    /**
     * Wheel TYPE Year
     */
    public static final int TYPE_YEAR = 1;
    /**
     * Wheel TYPE Month
     */
    public static final int TYPE_MONTH = 2;
    /**
     * Wheel TYPE Day
     */
    public static final int TYPE_DAY = 3;
    /**
     * Wheel TYPE Hour
     */
    public static final int TYPE_HOUR = 4;
    /**
     * Wheel TYPE Minute
     */
    public static final int TYPE_MINUTE = 5;
    private static final String NULL_DATE = "--/--";


    /**
     * date转制定格式的string
     *
     * @param d
     * @param format
     * @return
     */
    public static String date2Str(Date d, String format) {// yyyy-MM-dd HH:mm:ss
        if (d == null) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(d);
        return s;
    }


    /**
     * 格式化时间
     *
     * @param s long类型的日期字符串
     * @return
     */
    public static String parseTime(String s) {
        return parseTime(s, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * @param s      Long类型的时间字符串
     * @param format 格式化
     * @return
     */
    public static String parseTime(String s, String format) {
        if (s == null || "".equals(s)) return "";
        //将字符串转化为Long类型
        try {
            Long timeLong = Long.parseLong(s);
            return parseTime(timeLong, format);
        } catch (Exception ex) {
            ex.printStackTrace();
            return NULL_DATE;
        }

    }


    public static String parseTime(Long timeLong, String format) {
        if (timeLong == null) return NULL_DATE;
        //将Long类型转化为Date
        Date date = new Date(timeLong);

        //将Date类型格式化
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateString = sdf.format(date);

        return dateString;
    }

    /**
     * 创建年份数据
     *
     * @return
     */

    public static List<String> getItemList(int type) {
        if (type == TYPE_YEAR) {
            return createdYear();
        } else if (type == TYPE_MONTH) {
            return createdMonth();
        } else if (type == TYPE_DAY) {
            return createdDay();
        } else if (type == TYPE_HOUR) {
            return createdHour();
        } else if (type == TYPE_MINUTE) {
            return createdMniter();
        } else {
            throw new IllegalArgumentException("type is illegal");
        }
    }

    private static List<String> createdYear() {
        List<String> wheelString = new ArrayList<>();
        for (int i = MIN_YEAR; i <= MAX_YEAR; i++) {
            wheelString.add(Integer.toString(i));
        }
        return wheelString;
    }

    /**
     * 创建月份数据
     *
     * @return
     */
    private static List<String> createdMonth() {
        List<String> wheelString = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            wheelString.add(String.format("%02d", i));
        }
        return wheelString;
    }


    /**
     * 创建日期数据
     *
     * @return
     */
    private static List<String> createdDay() {
        List<String> wheelString = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            wheelString.add(String.format("%02d", i));
        }
        return wheelString;
    }

    /**
     * 根据年 月创建日期数据
     *
     * @return
     */
    public static List<String> createdDay(int year, int month) {
        List<String> wheelString = new ArrayList<>();
        int size = 0;
        if (isLeapMonth(month)) {
            size = 31;
        } else if (month == 2) {
            if (isLeapYear(year)) {
                size = 29;
            } else {
                size = 28;
            }
        } else {
            size = 30;
        }

        for (int i = 1; i <= size; i++) {
            wheelString.add(String.format("%02d", i));
        }
        return wheelString;
    }


    /**
     * 创建小时数据
     *
     * @return
     */
    private static List<String> createdHour() {
        List<String> wheelString = new ArrayList<>();
        for (int i = 0; i <= 23; i++) {
            wheelString.add(String.format("%02d", i));
        }
        return wheelString;
    }

    /**
     * 创建分钟数据
     *
     * @return
     */
    private static List<String> createdMniter() {
        List<String> wheelString = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            wheelString.add(String.format("%02d", i));
        }
        return wheelString;
    }

    /**
     * 计算大小月
     *
     * @param month
     * @return
     */
    private static boolean isLeapMonth(int month) {
        return month == 1 || month == 3 || month == 5 || month == 7
                || month == 8 || month == 10 || month == 12;
    }

    /**
     * 计算闰年
     *
     * @param year
     * @return
     */
    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }
}
