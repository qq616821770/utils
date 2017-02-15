package com.pingan.pinganwifiboss.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.pingan.pinganwifiboss.R;

/**
 * @author wangdenghui E-mail:WANGDENGHUI351@pingan.com.cn
 */
public class PopupWindowUtils {

    public static PopupWindow showPop(Context context, int layoutId, int[] btnIds, View anchoredView, View.OnClickListener clickListener) {
        View pop = LayoutInflater.from(context).inflate(layoutId, null);
        if (null == pop) {
            return null;
        }
        Button button = null;
        for (int btnId : btnIds) {
            button = (Button) pop.findViewById(btnId);
            button.setOnClickListener(clickListener);
        }
        PopupWindow mPopupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.abc_ic_clear_holo_light));
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.showAsDropDown(anchoredView);
        } else {
            mPopupWindow.dismiss();
        }
        return mPopupWindow;
    }


}
