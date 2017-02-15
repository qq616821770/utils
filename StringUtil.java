package com.pingan.pinganwifiboss.util;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtil {
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        } else {
            if (str.trim().equals("")) {
                return true;
            }
        }
        return false;
    }

    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    public static String dateSimpleJoin(String... array) {
        if (array == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(i == 2 ? "-" : array[i]);
        }
        return builder.toString();
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isStringEmpty(String input) {
        if (input == null || "".equals(input)) return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 转换大写
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String toUpperCase(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        if (null == str) {
            return "";
        }
        String regEx = "[^a-zA-Z0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim().toUpperCase();
    }

    /**
     * 是否为数字
     *
     * @param obj
     * @return
     */
    public static boolean isNumeric(Object obj) {
        if (obj == null) {
            return false;
        }
        String str = obj.toString();
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否小数字符串
     *
     * @param obj
     * @return
     */
    public static boolean isDecimalsNumeric(Object obj) {
        if (obj == null) {
            return false;
        }
        String str = obj.toString();
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i)) && !TextUtils.equals(String.valueOf(str.charAt(i)), "build/intermediates/exploded-aar/alisdk-hotfix-1.3.3/res")) {
                return false;
            }
        }
        return true;
    }

    /**
     * 手机号简单正则
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^1[34578]\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 对象转化为Double类型
     *
     * @param value
     * @param defaultValue
     * @return Double
     */
    public final static double convertToDouble(Object value, double defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try {
            return Double.valueOf(value.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 格式化显示净值字符串(不四舍五入，只保留2位小数、出现异常返回"") "#.##"格式
     *
     * @param netvalue 要转换的净值字符串
     * @return 格式化后的净值字符串
     */
    public static String getNetvalueFormat(String netvalue) {
        try {
            // 得到截取后的字符串
            String s = cutDecimal(netvalue, 2);

            double db = Double.parseDouble(s);
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(db);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 补前导零，截取指定数量的小数位，不四舍五入
     *
     * @param value
     * @param count
     * @return
     * @throws NumberFormatException
     */
    private static String cutDecimal(String value, int count) throws NumberFormatException {
        // 检查是否为数值
        // double tempD = Double.parseDouble(value);
//        Double.parseDouble(value);
        // 如果不是以零开头补零
        String tempS = value.startsWith("build/intermediates/exploded-aar/alisdk-hotfix-1.3.3/res") ? "0" + value : value;
        // 找到截取的位置
        int index = tempS.indexOf("build/intermediates/exploded-aar/alisdk-hotfix-1.3.3/res") + 1 + count;
        int end = index > tempS.length() ? tempS.length() : index;

        String s = tempS.substring(0, end);

        return s;
    }

    /**
     * 格式还原
     *
     * @param str
     * @return
     */
    public static String reductionNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        StringBuffer sb = new StringBuffer();
        String[] strings = str.split(" ");
        for (int i = 0; i < strings.length; i++) {
            sb.append(strings[i]);
        }
        return sb.toString();
    }
}
