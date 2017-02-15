package com.pingan.pinganwifiboss.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.IBinder;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pingan.pinganwifiboss.R;

import java.io.ByteArrayOutputStream;

import cn.core.util.ResourceUtil;
import cn.core.util.UiUtilities;

public class UiUtil {
    public static ShapeDrawable createRoundCornerShapeDrawable(float radius, float borderLength, int borderColor) {
        float[] outerRadii = new float[8];
        float[] innerRadii = new float[8];
        for (int i = 0; i < 8; i++) {
            outerRadii[i] = radius + borderLength;
            innerRadii[i] = radius;
        }

        ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(outerRadii, new RectF(borderLength, borderLength, borderLength, borderLength), innerRadii));
        sd.getPaint().setColor(borderColor);

        return sd;
    }

    private StateListDrawable addStateDrawable(Context context, int idNormal, int idPressed, int idFocused) {
        StateListDrawable sd = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
        Drawable focus = idFocused == -1 ? null : context.getResources().getDrawable(idFocused);
        // 注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
        // 所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
        sd.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focus);
        sd.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        sd.addState(new int[]{android.R.attr.state_focused}, focus);
        sd.addState(new int[]{android.R.attr.state_pressed}, pressed);
        sd.addState(new int[]{android.R.attr.state_enabled}, normal);
        sd.addState(new int[]{}, normal);
        return sd;
    }

    public Drawable getSelectorDrawable(int nColor, int pColor) {
        ColorDrawable cdNormal = new ColorDrawable(Color.parseColor("#88FE9D3D"));
        ColorDrawable cdPressed = new ColorDrawable(Color.parseColor("#EEFE9D3D"));
        StateListDrawable sld = new StateListDrawable();
        sld.addState(new int[]{-android.R.attr.state_pressed}, cdNormal);
        sld.addState(new int[]{android.R.attr.state_pressed}, cdPressed);
        return sld;
    }

    /**
     * 对TextView设置不同状态时其文字颜色
     */
    public static ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[]{pressed, focused, normal, focused, unable, normal};
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    /**
     * 设置Selector。
     */
    public static StateListDrawable newSelector(Context context, int idNormal, int idPressed, int idFocused, int idUnable) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);
        Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
        Drawable focused = idFocused == -1 ? null : context.getResources().getDrawable(idFocused);
        Drawable unable = idUnable == -1 ? null : context.getResources().getDrawable(idUnable);
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focused);
        // View.ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled}, normal);
        // View.FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_focused}, focused);
        // View.WINDOW_FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_window_focused}, unable);
        // View.EMPTY_STATE_SET
        bg.addState(new int[]{}, normal);
        return bg;
    }

    private static Toast toast;

    public static void setPressedSafe(View view, boolean pressed) {
        if (view != null && view.isPressed() != pressed) {
            view.setPressed(pressed);
        }
    }

    public static void setActivatedSafe(View view, boolean activated) {
        if (view != null && view.isActivated() != activated) {
            view.setActivated(activated);
        }
    }

    public static void setVisibilitySafe(View view, int visibility) {
        if (view != null && view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }

    public static void setVisibilitySafe(View parent, int id, int visibility) {
        if (parent != null) {
            View view = parent.findViewById(id);
            if (view != null) {
                setVisibilitySafe(view, visibility);
            }
        }
    }

    public static void setBackgroundResourceSafe(View parent, int id, int resId) {
        if (parent != null) {
            View view = parent.findViewById(id);
            if (view != null) {
                view.setBackgroundResource(resId);
            }
        }
    }

    public static void setEnabledSafe(View parent, int id, boolean enabled) {
        if (parent != null) {
            View view = parent.findViewById(id);
            setEnabledSafe(view, enabled);
        }
    }

    public static void setEnabledSafe(View view, boolean enabled) {
        if (view != null) {
            view.setEnabled(enabled);
        }
    }

    public static void setOnClickListenerSafe(View parent, int id, OnClickListener l) {
        if (parent != null) {
            View view = parent.findViewById(id);
            if (view != null) {
                view.setOnClickListener(l);
            }
        }
    }

    public static void requestFocus(View view) {
        if (view != null) {
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.requestFocus();
        }
    }

    public static boolean isEditTextEmpty(EditText edit) {
        return edit.getText() == null || edit.getText().toString().trim().length() <= 0;
    }

    public static boolean hideInputMethod(Activity activity) {
        return hideInputMethod(activity, activity.getWindow().getDecorView().getWindowToken());
    }

    public static boolean hideInputMethod(Dialog dialog) {
        return hideInputMethod(dialog.getContext(), dialog.getWindow().getDecorView().getWindowToken());
    }

    public static boolean hideInputMethod(Context context, IBinder token) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return im.hideSoftInputFromWindow(token, 0);
    }

    public static void showInputMethod(Context context, View view) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view, 0);
    }

    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    public static void recycleBitmap(BitmapDrawable drawable) {
        if (drawable != null) {
            recycleBitmap(drawable.getBitmap());
        }
    }

    public static void dismissDialog(Dialog pd) {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    public static void toast(Context context, int resId) {
        toast(context, context.getString(resId));
    }

    public static void toast(Context context, String text) {
        if (null == text || "".equals(text)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
     */
    public static int sp2px(Context context, int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * 切换密码显示状态
     */
    public static void changePasswordStatus(EditText edtPwd, boolean isCheck) {
        int index = edtPwd.getSelectionStart();// 获取光标所在位置
        if (isCheck) {
            edtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            edtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        edtPwd.setSelection(index);

    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    public static void setViewTip(Context context, TextView view, boolean isNull) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.verify_error);
        drawable.setBounds(0, 0, UiUtilities.dip2px(context, 14), UiUtilities.dip2px(context, 14));
        view.setTextColor(ResourceUtil.getColorFromResource(context, isNull ? R.color.text_light_gray : R.color.bg_orange));
        view.setCompoundDrawablePadding(UiUtilities.dip2px(context, 8));
        view.setCompoundDrawables(isNull ? null : drawable, null, null, null);
    }
}
