package com.pingan.pinganwifiboss.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.pingan.pinganwifiboss.PAActivity;

import cn.core.util.PatternUtil;

public class ToastUtil {

    public static void show(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    public static void show(Context context, int info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    public static boolean checkInputParam(PAActivity activity, String userName, String pwd) {
        boolean isAccess = false;

        if (!checkMobile(activity, userName)) {
            return isAccess;
        }
        if (!checkPassword(activity, pwd)) {
            return isAccess;
        }
        return true;
    }

    public static boolean checkMobile(PAActivity activity, String mobile) {
        boolean isAccess = false;
        if (activity.isEmpty(mobile)) {
            activity.toast("请输入手机号");
            return isAccess;
        } else if (mobile.length() < 11) {
            activity.toast("手机号长度不正确");
            return isAccess;
        }
        return true;

    }

    public static boolean checkPassword(PAActivity activity, String pwd) {
        boolean isAccess = false;

        if (activity.isEmpty(pwd)) {
            activity.toast("请输入密码");
            return isAccess;
        } else if (!PatternUtil.checkPassword(pwd)) {
            activity.toast("请输入4~16位密码");
            return isAccess;
        }
        return true;
    }

    public static void showOnUiThread(final Activity activity, final String msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                show(activity, msg);
            }
        });
    }

}
