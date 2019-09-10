package com.sweven.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import static com.sweven.util.ToastUtil.Gravity.BOTTOM;
import static com.sweven.util.ToastUtil.Gravity.CENTER;
import static com.sweven.util.ToastUtil.Gravity.DEFAULT;
import static com.sweven.util.ToastUtil.Gravity.TOP;
import static com.sweven.util.ToastUtil.Gravity.getBottom;
import static com.sweven.util.ToastUtil.Gravity.getTop;

/**
 * Created by Sweven on 2018/9/15.
 * Email:sweventears@Foxmail.com
 * <p>
 * Toast工具类
 */
public class ToastUtil {

    private static Toast toast;
    private Context context;

    // 默认底部
    private static int gravity = DEFAULT;

    public ToastUtil(Activity activity) {
        this.context = activity;
    }

    public ToastUtil(Context context) {
        this.context = context;
    }

    public ToastUtil(Activity activity, int gravity) {
        this.context = activity;
        ToastUtil.gravity = gravity;
    }

    public ToastUtil(Context context, int gravity) {
        this.context = context;
        ToastUtil.gravity = gravity;
    }

    /**
     * 显示一个较短时间的提示
     * 适用于所有类
     *
     * @param context 上下文
     * @param msg     显示的文字
     */
    @SuppressLint("ShowToast")
    public static void showShort(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        setGravity(context);
        toast.show();
    }

    /**
     * 显示一个较长时间的提示
     * 适用于所有类
     *
     * @param context 上下文
     * @param msg     显示的文字
     */
    @SuppressLint("ShowToast")
    public static void showLong(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        setGravity(context);
        toast.show();
    }

    /**
     * 显示一个较短时间的提示
     * 适用于所有类
     *
     * @param context 上下文
     * @param resId   显示的文字
     */
    @SuppressLint("ShowToast")
    public static void showShort(Context context, int resId) {
        if (toast == null) {
            toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        } else {
            toast.setText(resId);
        }
        setGravity(context);
        toast.show();
    }

    /**
     * 显示一个较长时间的提示
     * 适用于所有类
     *
     * @param context 上下文
     * @param resId   显示的文字
     */
    @SuppressLint("ShowToast")
    public static void showLong(Context context, int resId) {
        if (toast == null) {
            toast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
        } else {
            toast.setText(resId);
        }
        setGravity(context);
        toast.show();
    }

    /**
     * 取消显示
     */
    public static void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }

    /**
     * 显示一个较短时间的提示
     * 适用于activity类
     *
     * @param msg 显示的文字
     */
    @SuppressLint("ShowToast")
    public void showShort(String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        setGravity(context);
        toast.show();
    }

    /**
     * 显示一个较长时间的提示
     * 适用于activity类
     *
     * @param msg 显示的文字
     */
    @SuppressLint("ShowToast")
    public void showLong(String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        setGravity(context);
        toast.show();
    }

    /**
     * 显示一个较短时间的提示
     * 适用于activity类
     *
     * @param resId 显示的文字
     */
    @SuppressLint("ShowToast")
    public void showShort(int resId) {
        if (toast == null) {
            toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        } else {
            toast.setText(resId);
        }
        setGravity(context);
        toast.show();
    }

    /**
     * 显示一个较长时间的提示
     * 适用于activity类
     *
     * @param resId 显示的文字
     */
    @SuppressLint("ShowToast")
    public void showLong(int resId) {
        if (toast == null) {
            toast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
        } else {
            toast.setText(resId);
        }
        setGravity(context);
        toast.show();
    }

    public int getGravity() {
        return gravity;
    }

    public static void setGravity(int gravity) {
        ToastUtil.gravity = gravity;
    }

    /**
     * 需要在使用之前设置
     * 仅需设置一次
     *
     * @param context 上下文
     */
    public static void setGravity(Context context) {
        if (toast != null) {
            if (gravity != DEFAULT) {
                switch (gravity) {
                    case TOP:
                        toast.setGravity(android.view.Gravity.TOP, 0, -getTop(context));
                        break;
                    case CENTER:
                        toast.setGravity(android.view.Gravity.CENTER, 0, 0);
                        break;
                    case BOTTOM:
                        toast.setGravity(android.view.Gravity.BOTTOM, 0, getBottom());
                        break;
                    default:
                        toast = null;
                        break;
                }
            }
        }
    }

    /**
     * 需要在使用之前设置
     * 仅需设置一次
     *
     * @param context 上下文
     * @param gravity toast显示位置
     */
    public static void setGravity(Context context, int gravity) {
        ToastUtil.gravity = gravity;
        if (toast != null) {
            switch (gravity) {
                case TOP:
                    toast.setGravity(android.view.Gravity.TOP, 0, -getTop(context));
                    break;
                case CENTER:
                    toast.setGravity(android.view.Gravity.CENTER, 0, 0);
                    break;
                case BOTTOM:
                    toast.setGravity(android.view.Gravity.BOTTOM, 0, getBottom());
                    break;
                default:
                    toast = null;
                    break;
            }
        }
    }

    public static class Gravity {
        public static final int DEFAULT = -1;
        public static final int CENTER = 1;
        public static final int TOP = 2;
        public static final int BOTTOM = 3;

        private static final int defaultHeight = 50;

        private static int getStatusHeight(Context context) {
            return WindowUtil.getStatusBarHeight(context);
        }

        public static int getTop(Context context) {
            return getStatusHeight(context) + defaultHeight;
        }

        public static int getBottom() {
            return defaultHeight * 2;
        }
    }
}
