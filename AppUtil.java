package com.pingan.pinganwifiboss.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.LocationManager;
import android.net.Uri;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import com.pingan.pinganwifiboss.PAActivity;
import com.pingan.pinganwifiboss.R;
import com.pingan.pinganwifiboss.SYBConfig;
import com.pingan.pinganwifiboss.msg.GlobleMsg;
import com.pingan.pinganwifiboss.net.GlobalNetwork;
import com.pingan.pinganwifiboss.view.activity.ActionItemActivity;
import com.pingan.pinganwifiboss.view.activity.LoginActivity;
import com.pingan.pinganwifiboss.view.fragment.BaseWebViewFragment;
import com.pingan.pinganwifiboss.view.fragment.SelfFragment;
import com.pingan.pinganwifiboss.view.webview.JsModelImp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.core.PackerNg;
import cn.core.bean.UserData;
import cn.core.bean.UserInfo;
import cn.core.local.LocalData;
import cn.core.log.Lg;
import cn.core.log.LogUtils;
import cn.core.net.http.ServiceResponse;

public class AppUtil {

    /**
     * 获取当前渠道名称
     *
     * @param context
     * @return
     */
    public static String getChannelName(Context context) {
        String channel = null;
        if (null == context) {
            return channel;
        }
//        PackageManager packageManager = context.getPackageManager();
//        ApplicationInfo applicationInfo;
//        try {
//            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
//            if (applicationInfo != null && applicationInfo.metaData != null) {
//                channel = applicationInfo.metaData.get("TD_CHANNEL_ID").toString();
//            }
//        } catch (NameNotFoundException e) {
//            Lg.w(e);
//        }
        return PackerNg.getMarket(context, "default.com");
    }

    public static String getMpushGateWay(Context context) {
        String channel = null;
        if (null == context) {
            return channel;
        }
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                channel = applicationInfo.metaData.get("MPUSH_GATEWAY").toString();
            }
        } catch (NameNotFoundException e) {
            Lg.w(e);
        }
        return channel;
    }

    /**
     * 获取当前程序的版本号
     */
    public static int getVersionCode(Context context) {
        int code = 0;
        if (null == context) {
            return code;
        }
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            code = packInfo.versionCode;
        } catch (NameNotFoundException e) {
            Lg.w(e);
        }
        return code;
    }

    /**
     * 获取当前程序的版本名
     */
    public static String getVersionName(Context context) {
        String name = "";
        if (null == context) {
            return name;
        }
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            name = packInfo.versionName;
        } catch (NameNotFoundException e) {
            Lg.w(e);
        }
        return name;
    }


    /**
     * 获取手机系统版本
     */
    public static String getSysVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static void printStackTrace() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        Lg.i("===================save data===================");
        for (StackTraceElement stackTraceElement : elements) {
            Lg.i(stackTraceElement.getFileName() + "\t" + stackTraceElement.getClassName() + "\t" + stackTraceElement.getMethodName() + "\t" + stackTraceElement.getLineNumber());
        }
        Lg.i("===================save data end===================");
    }

    public static boolean isRunInBackground(Context context) {
        boolean result = !isScreenOn(context) || isTaskBackground(context);
        Lg.i("isRunInBackground " + result);
        return result;
    }

    public static boolean isScreenOn(Context c) {
        PowerManager pm = (PowerManager) c.getSystemService(Context.POWER_SERVICE);
        return pm.isScreenOn();
    }

    private static boolean isTaskBackground(Context context) {
        if (null == context) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        for (RunningTaskInfo task : tasks) {
            Lg.i("task:" + task.baseActivity + ",numRunning:" + task.numRunning);
            if (task.numRunning == 0 || !context.getPackageName().equals(task.baseActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通知栏的高度获取
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        if (null == context) {
            return 0;
        }
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            LogUtils.e(e1.toString());
        }
        return statusBarHeight;
    }

    /**
     * 获取应用包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        if (null == context) {
            return null;
        }
        String pkgName = context.getPackageName();
        return pkgName;
    }

    /**
     * 获取设备IMEI号
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        if (null == context) {
            return null;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }


    /**
     * 获取application META_DATA
     *
     * @param context
     * @param key
     * @return
     */
    public static String getMetaData(Context context, String key) {
        String value = "";
        if (null == context) {
            return value;
        }
        try {
            value = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData.getString(key);
        } catch (NameNotFoundException e) {
            Lg.w(e);
        }
        return value;
    }

    public static void jumpToActivity(final Activity activity, JsModelImp.H5Response response) {
        if (null == activity) {
            return;
        }
        if (isMustToLogin(activity, response.android)) {
            return;
        }
        if (-1 != isFragmentAtMain(activity, response.android)) {
            //如果在MainActivity点击跳转，不跳转
            if (null != activity && "MainActivity".equals(activity.getClass().getSimpleName())) {
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("fragmentPage", isFragmentAtMain(activity, response.android));
            if (null == activity) {
                return;
            }
            activity.setResult(ActionItemActivity.RESULT_CODE, intent);
            activity.finish();
            return;
        }

        if (!TextUtils.isEmpty(response.android)) {
            Lg.i("h5  response: " + response.android);
            //打开基金首页
            if (response.android.equals("fund")) {
                UserData data = LocalData.Factory.create().getUserData(activity);
                String jsessionid = null;
                if (data == null) {
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                    return;
                }
                jsessionid = data.jsessionid;
                GlobalNetwork.getFundAccountStatu((PAActivity) activity, new GlobalNetwork.AfterRequestSuccessListenter() {
                    @Override
                    public void todo(ServiceResponse response) {
                        if ("200".equals(response.code)) {
                            ActionItemActivity.actionStart(activity, (BaseWebViewFragment) SelfFragment.FundHomePageFragment.newInstance("收益宝钱包", SYBConfig.getFundHomePage(), activity));
                        } else if ("201".equals(response.code)) {
                            ActionItemActivity.actionStart(activity, "收益宝钱包", SYBConfig.getFundGuidePage());
                        }
                    }
                }, jsessionid);
                return;
            }

            StringBuilder className = new StringBuilder();
            //基金的activity在fun包中
            if (response.android.equals("FundTurnInActivity") || response.android.equals("FundTurnOutActivity") || response.android.equals("FundOpenAccountActivity")) {
                className.append("com.pingan.pinganwifiboss.fund.");
            } else {
                className.append("com.pingan.pinganwifiboss.view.activity.");
            }
            className.append(response.android);
            if (!response.android.contains("Activity")) {
                className.append("Activity");
            }
            try {
                Class jClass = Class.forName(className.toString());
                Intent intent = new Intent(activity, jClass);
                if ("com.pingan.pinganwifiboss.view.activity.ActionItemActivity".equals(className.toString()) && !TextUtils.isEmpty(response.url)) {
                    Lg.i("h5  url: " + response.url);
                    intent.putExtra(ActionItemActivity.ActionItemFragment.PARAM_TITLE, "");
                    intent.putExtra(ActionItemActivity.ActionItemFragment.PARAM_URL, response.url);
                }
                activity.startActivity(intent);
            } catch (ClassNotFoundException e) {
                Lg.e(e);
            }
        }
    }


    /**
     * shao sheng
     * 是否需要登录
     *
     * @param android
     * @return
     */
    public static boolean isMustToLogin(Context context, String android) {
        String[] mustLoginPages = context.getResources().getStringArray(R.array.must_login_page);
        for (String page : mustLoginPages) {
            if (page.contains(android)) {
                UserData data = LocalData.Factory.create().getUserData(context);
                if (null == data) {
                    ToastUtil.show(context, "请先登录");
                    Intent intent = new Intent(context, LoginActivity.class);
                    //LoginActivity是SingleTop，不能使用startActivityForResult(),5.1系统没这问题
                    intent.putExtra("requestCode", GlobleMsg.JUMP_TO_LOGIN);
                    context.startActivity(intent);
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * shao sheng
     * 判断是否是主页中的三个fragment
     */
    private static int isFragmentAtMain(Context context, String page) {
        String[] fragemts = context.getResources().getStringArray(R.array.main_fragemt);
        for (int a = 0; a < 4; a++) {
            if (fragemts[a].contains(page)) {
                return a;
            }
        }
        return -1;
    }


    /**
     * 判断手机是否Root
     *
     * @return
     */
    public static boolean isRoot() {
        String binPath = "/system/bin/su";
        String xBinPath = "/system/xbin/su";
        if (new File(binPath).exists() && isExecutable(binPath)) {
            return true;
        }
        if (new File(xBinPath).exists() && isExecutable(xBinPath)) {
            return true;
        }
        return false;
    }

    private static boolean isExecutable(String filePath) {
        Process p = null;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ls -l ").append(filePath);
        try {
//            p = Runtime.getRuntime().exec("ls -l " + filePath);
            p = Runtime.getRuntime().exec(stringBuilder.toString());
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = in.readLine();
            Lg.i(str);
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x') {
                    return true;
                }
            }
        } catch (IOException e) {
            LogUtils.e(e.toString());
        } finally {
            if (null != p) {
                p.destroy();
            }
        }
        return false;
    }

    /**
     * 检查是否打开WLAN定位
     *
     * @param context
     * @return true 打开WLAN定位
     */
    public static boolean checkLocationEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean isOpenNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean isOpenGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return (isOpenNetwork || isOpenGps);
    }

    /**
     * 校验金额是否合法
     *
     * @param price
     * @return true 合法
     */
    public static boolean isPriceValid(String price) {
        if (TextUtils.isEmpty(price)) {
            LogUtils.i("price ->" + price);
            return false;
        }
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");//判断小数点后一位的数字的正则表达式
        Matcher matcher = pattern.matcher(price);
        return matcher.matches();
    }

    public static String getIMSI(Context context) {
        if (context == null) {
            return "";
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imsi = telephonyManager.getSubscriberId();
            if (TextUtils.isEmpty(imsi) || "000000000000000".equals(imsi)) {
                imsi = telephonyManager.getSimOperator();
            }

            return TextUtils.isEmpty(imsi) ? "" : imsi;
        }
    }

    /**
     * 验证手机号码(支持国际格式, +86135xxxxxxxx(中国内地), +00852137xxxx...(香港))
     *
     * @param mobile 移动、联通、电信运营商的号码段
     * @return true 是手机号, false 不是手机号
     */
    public static boolean isMobile(String mobile) {
        String regex = "(\\+\\d+)?1[3458]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 区号+座机号码+分机号码
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isTel(String phoneNumber) {
        String regex = "(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{7,8}";
        return Pattern.matches(regex, phoneNumber);
    }

    /**
     * 验证身份证号码
     *
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";
        return Pattern.matches(regex, idCard);
    }

    public static void jumpToActivity(final Activity context, String url) {
        if (url == null) {
            return;
        }
        Uri uri = Uri.parse(url);
        if (uri != null) {
            // pinganwifiboss://open?pageName=fund
            if ("pinganwifiboss".equals(uri.getScheme())) {
                UserData userdata = LocalData.Factory.create().getUserData(context);
                UserInfo userinfo = LocalData.Factory.create().getUserInfo(context);
                String page = null;
                if (url.contains("pageName")) {
                    page = uri.getQueryParameter("pageName");
                } else {
                    page = uri.getQueryParameter("page");
                }
                if (!TextUtils.isEmpty(page)) {
                    if (null == context) {
                        return;
                    }
                    if (isMustToLogin(context, page)) {
                        return;
                    }
                    if (-1 != isFragmentAtMain(context, page)) {
                        //如果在MainActivity点击跳转，不跳转
                        if (null != context && "MainActivity".equals(context.getClass().getSimpleName())) {
                            return;
                        }
                        Intent intent = new Intent();
                        intent.putExtra("fragmentPage", isFragmentAtMain(context, page));
                        if (null == context) {
                            return;
                        }
                        context.setResult(ActionItemActivity.RESULT_CODE, intent);
                        context.finish();
                        return;
                    }

                    if (!TextUtils.isEmpty(page)) {
                        Lg.i("h5  response: " + page);
                        //打开基金首页
                        if (page.equals("fund")) {
                            UserData data = LocalData.Factory.create().getUserData(context);
                            String jsessionid = null;
                            if (data == null) {
                                context.startActivity(new Intent(context, LoginActivity.class));
                                return;
                            }
                            jsessionid = data.jsessionid;
                            GlobalNetwork.getFundAccountStatu((PAActivity) context, new GlobalNetwork.AfterRequestSuccessListenter() {
                                @Override
                                public void todo(ServiceResponse response) {
                                    if ("200".equals(response.code)) {
                                        ActionItemActivity.actionStart(context, (BaseWebViewFragment) SelfFragment.FundHomePageFragment.newInstance("收益宝钱包", SYBConfig.getFundHomePage(), context));
                                    } else if ("201".equals(response.code)) {
                                        ActionItemActivity.actionStart(context, "收益宝钱包", SYBConfig.getFundGuidePage());
                                    }
                                }
                            }, jsessionid);
                            return;
                        }

                        StringBuilder className = new StringBuilder();
                        //基金的activity在fun包中
                        if (page.equals("FundTurnInActivity") || page.equals("FundTurnOutActivity") || page.equals("FundOpenAccountActivity")) {
                            className.append("com.pingan.pinganwifiboss.fund.");
                        } else {
                            className.append("com.pingan.pinganwifiboss.view.activity.");
                        }
                        className.append(page);
                        if (!page.contains("Activity")) {
                            className.append("Activity");
                        }
                        try {
                            Class jClass = Class.forName(className.toString());
                            Intent intent = new Intent(context, jClass);
                            if ("com.pingan.pinganwifiboss.view.activity.ActionItemActivity".equals(className.toString()) && !TextUtils.isEmpty(page)) {
                                Lg.i("h5  url: " + page);
                                intent.putExtra(ActionItemActivity.ActionItemFragment.PARAM_TITLE, "");
                                intent.putExtra(ActionItemActivity.ActionItemFragment.PARAM_URL, page);
                            }
                            context.startActivity(intent);
                        } catch (ClassNotFoundException e) {
                            Lg.e(e);
                        }
                    }
                }
            }

        }
    }

    /**
     * 重启 app
     *
     * @param context
     */
    public static void restartAPP(Context context) {
        Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }

    public static void hideSoftInput(PAActivity context) {
        InputMethodManager im = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (im.isActive() && context.getCurrentFocus() != null) {
            im.hideSoftInputFromWindow(context.getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
