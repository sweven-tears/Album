package com.sweven.util;

import android.util.Log;

/**
 * Created by Sweven on 2018/9/15.
 * Email:sweventears@Foxmail.com
 */
public class LogUtil {
    private String TAG;

    private boolean show = true;

    public LogUtil(String TAG) {
        this.TAG = TAG;
    }

    /**
     * 打印意义最小的日志信息
     *
     * @param msg 需要记录的信息
     */
    public void v(String msg) {
        if (isShow()) {
            Log.v(TAG + "-------->>", msg);
        } else {
            Log.v(TAG + "-------->>", "Log记录已关闭");
        }
    }

    /**
     * 打印调试信息
     *
     * @param msg 需要记录的信息
     */
    public void d(String msg) {
        if (isShow()) {
            Log.d(TAG + "-------->>", msg);
        } else {
            Log.v(TAG + "-------->>", "Log记录已关闭");
        }
    }

    /**
     * 打印重要数据
     *
     * @param msg 需要记录的信息
     */
    public void i(String msg) {
        if (isShow()) {
            Log.i(TAG + "-------->>", msg);
        } else {
            Log.v(TAG + "-------->>", "Log记录已关闭");
        }
    }

    /**
     * 打印警告信息，程序可能存在潜在风险
     *
     * @param msg 需要记录的信息
     */
    public void w(String msg) {
        if (isShow()) {
            Log.w(TAG + "-------->>", msg);
        } else {
            Log.v(TAG + "-------->>", "Log记录已关闭");
        }
    }

    /**
     * 打印错误信息
     *
     * @param msg 需要记录的信息
     */
    public void e(String msg) {
        if (show) {
            Log.e(TAG + "-------->>", msg);
        } else {
            Log.v(TAG + "-------->>", "Log记录已关闭");
        }
    }

    /**
     * 打印最严重的信息
     *
     * @param msg 需要记录的信息
     */
    public void a(String msg) {
        if (show) {
            Log.wtf(TAG + "-------->>", msg);
        } else {
            Log.v(TAG + "-------->>", "Log记录已关闭");
        }
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public void show() {
        if (!isShow()) {
            setShow(true);
        }
    }

    public void hidden() {
        if (isShow()) {
            setShow(false);
        }
    }
}
