package com.sweven.page;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

public class Page {
    private static String DEFAULT_PACKAGE = "luoluna.sweven.album";

    private static Map<String, String> pages;

    static {
        pages = new HashMap<>();
    }

    public Context context;

    public Page(Context context) {
        this.context = context;
    }

    public Page(Context context, String page) {
        this.context = context;
        openPage(page);
    }


    /**
     * @param packageName 默认包名
     */
    public Page setPackage(String packageName) {
        DEFAULT_PACKAGE = packageName;
        return this;
    }

    /**
     * @param alias 别名
     * @param page  对应页面
     */
    public Page addPage(String alias, String page) {
        pages.put(alias, DEFAULT_PACKAGE + "." + page);
        return this;
    }

    public void openPage(String page) {
        if (DEFAULT_PACKAGE == null || DEFAULT_PACKAGE.trim().equals("")) {
            throw new RuntimeException("请在配置文件中配置 new Page(this).setPackage(\"com.cn.example\")");
        }
        try {
            if (!pages.keySet().contains(page)) {
                throw new RuntimeException(page + "不存在");
            }
            Class<?> clz = Class.forName(pages.get(page));
            Intent intent = new Intent(context, clz);
            context.startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
