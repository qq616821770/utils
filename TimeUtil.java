package com.pingan.pinganwifiboss.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import cn.core.log.Lg;

/**
 * 时间工具类
 *
 * @author develop
 */
public class TimeUtil {

    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    /**
     * 获取今天的日期
     *
     * @return
     */
    public static String getToday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 获取当前的年月份
     *
     * @return
     */
    public static String getThisMonth() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 判断日期是否为今天
     *
     * @param day yyyy-MM-dd
     * @return
     */
    public static int isToday(String day) {
        if (getToday() == null || day == null) {
            return -1;
        }
        return getToday().compareTo(day);
    }

    /**
     * 判断两个日期是否为同一天
     *
     * @param ms1
     * @param ms2
     * @return
     */
    public static boolean isSameDayOfMillis(final long ms1, final long ms2) {
        final long interval = ms1 - ms2;
        return interval < MILLIS_IN_DAY && interval > -1L * MILLIS_IN_DAY && toDay(ms1) == toDay(ms2);
    }

    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
    }

    public static String convertTimeStamp(long time) {
        long mTime = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("MM月dd日 HH:mm");
        if (simpleDateFormat.format(new Date()).equals(simpleDateFormat.format(new Date(time * 1000)))) {
            return simpleDateFormat2.format(new Date(time * 1000));
        } else if (simpleDateFormat.format(new Date(mTime - 24 * 60 * 60 * 1000)).equals(simpleDateFormat.format(new Date(time * 1000)))) {
            return "昨天 " + simpleDateFormat2.format(new Date(time * 1000));
        } else {
            return simpleDateFormat3.format(new Date(time * 1000));
        }
    }

    /**
     * 判断两个时间是否是同一天
     *
     * @param dateA
     * @param dateB
     * @return
     */
    public static boolean areSameDay(Date dateA, Date dateB) {
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(dateA);
        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(dateB);
        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR) && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH) && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 将指定字符串格式的日期与当前时间比较
     *
     * @param strDate 需要比较时间
     * @return 如果比当然时间早，则返回true，否则返回false。
     */
    public static boolean compareToCurTime(String strDate) {
        if (StringUtil.isEmpty(strDate)) {
            return false;
        }
        long curTime = new Date().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date parse = sdf.parse(strDate);
            if (parse.getTime() - curTime < 0) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            Lg.w(e);
        }
        return false;
    }

    /**
     * 得到本月剩余多少天
     */
    public static int getLastDayOfThisMonth() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:OO"));
        int day = c.get(Calendar.DAY_OF_MONTH);
        int last = c.getActualMaximum(Calendar.DAY_OF_MONTH);


        return last - day > 0 ? last - day : 0;
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
     */

    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 把YY-MM-DD时间格式转成xx月xx日
     *
     * @param date
     * @return
     */
    public static String dateToMD(String date) {
        String newDate = null;
        if (!TextUtils.isEmpty(date)) {
            String[] aa = date.split("-");
            if (aa.length > 2) {
                newDate = aa[1] + "月" + aa[2] + "日";
            }
        }
        return newDate;
    }
}
