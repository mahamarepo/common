/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mahama.common.utils;


import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
    private static final Object LOCK = new Object();

    private static final Map<String, ThreadLocal<SimpleDateFormat>> POOL = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

    public static Date toDayStart() {
        long current = System.currentTimeMillis();
        long zero = current - (current + TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24);
        return new Date(zero);
    }

    public static Date toDayStart(Date date) {
        long current = date.getTime();
        long zero = current - (current + TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24);
        return new Date(zero);
    }

    public static Date toDayStart(String date) {
        return toDayStart(parseTime(date));
    }

    public static Date toDayEnd() {
        long current = System.currentTimeMillis() + 1000 * 3600 * 24;
        long zero = current - (current + TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24) - 1;
        return new Date(zero);
    }

    public static Date toDayEnd(Date date) {
        long current = date.getTime() + 1000 * 3600 * 24;
        long zero = current - (current + TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24) - 1;
        return new Date(zero);
    }

    public static Date toDayEnd(String date) {
        return toDayEnd(parseTime(date));
    }

    /**
     * 获取YYYY格式
     *
     * @return
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 获取YYYY格式
     *
     * @return
     */
    public static String getYear(Date date) {
        return formatDate(date, "yyyy");
    }

    /**
     * 获取YYYY-MM-DD格式
     *
     * @return
     */
    public static String getDay() {
        return formatDate(new Date(), "yyyy-MM-dd");
    }

    public static String getDayStart() {
        return formatDate(new Date(), "yyyy-MM-dd 00:00:00");
    }

    public static String getDayEnd() {
        return formatDate(new Date(), "yyyy-MM-dd 23:59:59");
    }

    /**
     * 获取YYYY-MM-DD格式
     */
    public static String getDay(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }


    public static String getDayStart(Date date) {
        return formatDate(date, "yyyy-MM-dd 00:00:00");
    }

    public static String getDayEnd(Date date) {
        return formatDate(date, "yyyy-MM-dd 23:59:59");
    }


    /**
     * 获取YYYYMMDD格式
     *
     * @return
     */
    public static String getDays() {
        return formatDate(new Date(), "yyyyMMdd");
    }

    /**
     * 获取YYYYMMDD格式
     *
     * @return
     */
    public static String getDays(Date date) {
        return formatDate(date, "yyyyMMdd");
    }

    public static int getYearInt() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static int getYearInt(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static String getMonth() {
        return formatDate(new Date(), "yyyy-MM");
    }

    public static String getMonth(Date date) {
        return formatDate(date, "yyyy-MM");
    }


    public static int getMonthInt() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getMonthInt(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getDayInt() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    public static int getDayInt(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     *
     * @return
     */
    public static String getTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss.SSS格式
     *
     * @return
     */
    public static String getMsTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
    }

    /**
     * 获取YYYYMMDDHHmmss格式
     *
     * @return
     */
    public static String getAllTime() {
        return formatDate(new Date(), "yyyyMMddHHmmss");
    }

    public static String getTime(String pattern) {
        return formatDate(new Date(), pattern);
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     *
     * @return
     */
    public static String getTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatDate(String pattern) {
        return formatDate(new Date(), pattern);
    }

    public static String formatDate(Date date, String pattern) {
        String formatDate = null;
        if (StringUtil.isNotEmpty(pattern)) {
            formatDate = DateFormatUtils.format(date, pattern);
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * @param s
     * @param e
     * @return boolean
     * @throws
     * @Title: compareDate
     * @Description:(日期比较，如果s>=e 返回true 否则返回false)
     * @author luguosui
     */
    public static boolean compareDate(String s, String e) {
        if (parseDate(s) == null || parseDate(e) == null) {
            return false;
        }
        return parseDate(s).getTime() >= parseDate(e).getTime();
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date parseDate(String date) {
        return parse(date, "yyyy-MM-dd");
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date parseTime(String date) {
        return parse(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date parse(String date, String pattern) {
        if (date != null) {
            if (pattern == null || "".equals(pattern)) {
                return null;
            }
            DateFormat format = getDFormat(pattern);
            try {
                return format.parse(date);
            } catch (ParseException e) {
//				e.printStackTrace();
            }
        }
        return null;
    }

    public static SimpleDateFormat getDFormat(String pattern) {
        ThreadLocal<SimpleDateFormat> tl = POOL.get(pattern);
        if (tl == null) {
            synchronized (LOCK) {
                tl = POOL.get(pattern);
                if (tl == null) {
                    final String p = pattern;
                    tl = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected synchronized SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(p);
                        }
                    };
                    POOL.put(p, tl);
                }
            }
        }
        return tl.get();
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    /**
     * 把日期转换为Timestamp
     *
     * @param date
     * @return
     */
    public static Timestamp format(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 得到n天之后的日期
     */
    public static Date getAfterDayDate(int days) {
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, days); // 日期减 如果不够减会将月变动
        return canlendar.getTime();
    }

    /**
     * 得到n天之后的日期
     */
    public static Date getAfterDayDate(Date date, int days) {
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.setTime(date);
        canlendar.add(Calendar.DATE, days); // 日期减 如果不够减会将月变动
        return canlendar.getTime();
    }

    /**
     * 获取范围时间内所有日期
     *
     * @param begin   开始时间
     * @param end     结束时间
     * @param pattern 格式化,默认yyyy-MM-dd
     * @return 格式化后的时间列表
     */
    public static List<String> getBetweenDates(Date begin, Date end, String pattern) {
        if (StringUtil.isNullOrEmpty(pattern)) {
            pattern = "yyyy-MM-dd";
        }
        return getBetween(begin,end,pattern,Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取范围时间内所有月份
     *
     * @param begin   开始时间
     * @param end     结束时间
     * @param pattern 格式化,默认yyyy-MM
     * @return 格式化后的月份列表
     */
    public static List<String> getBetweenMonths(Date begin, Date end, String pattern) {
        if (StringUtil.isNullOrEmpty(pattern)) {
            pattern = "yyyy-MM";
        }
        return getBetween(begin,end,pattern,Calendar.MONTH);
    }

    /**
     * 获取范围时间内所有年份
     *
     * @param begin   开始时间
     * @param end     结束时间
     * @param pattern 格式化,默认yyyy
     * @return 格式化后的年份列表
     */
    public static List<String> getBetweenYears(Date begin, Date end, String pattern) {
        if (StringUtil.isNullOrEmpty(pattern)) {
            pattern = "yyyy";
        }
        return getBetween(begin,end,pattern,Calendar.YEAR);
    }

    private static List<String> getBetween(Date begin, Date end, String pattern,int field){
        long beginTime = begin.getTime();
        long zero = beginTime - (beginTime + TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24);
        begin=new Date(zero);
        List<String> list = new ArrayList<>();
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(begin);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(end);
        while (end.after(calBegin.getTime())) {
            list.add(formatDate(calBegin.getTime(), pattern));
            calBegin.add(field, 1);
        }
        return list;
    }
}
