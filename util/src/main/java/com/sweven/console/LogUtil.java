package com.sweven.console;

import android.util.Log;

/**
 * Created by Sweven on 2018/9/15.
 * Email:sweventears@Foxmail.com
 */
public class LogUtil {
    private static LogUtil instance;

    /**
     * 可配置一键式开关Log
     *
     * @return 获取全局log打印
     */
    public static LogUtil with() {
        if (instance == null) {
            synchronized (Log.class) {
                if (instance == null) {
                    instance = new LogUtil();
                }
            }
        }
        instance.tag = "";
        return instance;
    }

    /**
     * 可配置一键式开关Log
     *
     * @param TAG tag
     * @return 获取全局log打印
     */
    public static LogUtil with(String TAG) {
        if (instance == null) {
            synchronized (Log.class) {
                if (instance == null) {
                    instance = new LogUtil();
                }
            }
        }
        instance.tag = TAG;
        return instance;
    }

    /**
     * 未配置tag的构造函数
     */
    public LogUtil() {
        this.tag = "";
    }

    /**
     * 配置了tag的Log
     *
     * @param tag tag
     */
    public LogUtil(String tag) {
        this.tag = tag + "=====>";
    }

    private boolean debug = true;
    private String tag;

    /**
     * 配置log输出状态
     *
     * @param debug 是否输出log
     */
    public void debug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDebug() {
        return debug;
    }

    public void v(Object... msg) {
        if (msg == null || !debug) {
            return;
        }
        StringBuilder buffer = new StringBuilder();
        for (Object object : msg) {
            buffer.append(object.toString()).append("\t");
        }
        android.util.Log.v(tag, buffer.toString());
    }

    public void d(Object... msg) {
        if (msg == null || !debug) {
            return;
        }
        StringBuilder buffer = new StringBuilder();
        for (Object object : msg) {
            buffer.append(object.toString()).append("\t");
        }
        android.util.Log.d(tag, buffer.toString());
    }


    public void i(Object... msg) {
        if (msg == null || !debug) {
            return;
        }
        StringBuilder buffer = new StringBuilder();
        for (Object object : msg) {
            buffer.append(object.toString()).append("\t");
        }
        android.util.Log.i(tag, buffer.toString());
    }

    public void w(Object... msg) {
        if (msg == null || !debug) {
            return;
        }
        StringBuilder buffer = new StringBuilder();
        for (Object object : msg) {
            buffer.append(object.toString()).append("\t");
        }
        android.util.Log.w(tag, buffer.toString());
    }

    public void e(Object... msg) {
        if (msg == null || !debug) {
            return;
        }
        StringBuilder buffer = new StringBuilder();
        for (Object object : msg) {
            buffer.append(object.toString()).append("\t");
        }
        android.util.Log.e(tag, buffer.toString());
    }

    public void wft(Object... msg) {
        if (msg == null || !debug) {
            return;
        }
        StringBuilder buffer = new StringBuilder();
        for (Object object : msg) {
            buffer.append(object.toString()).append("\t");
        }
        android.util.Log.wtf(tag, buffer.toString());
    }
}
