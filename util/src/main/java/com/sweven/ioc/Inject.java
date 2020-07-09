package com.sweven.ioc;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.sweven.ioc.annotation.BindView;
import com.sweven.ioc.annotation.ContentView;

import java.lang.reflect.Field;

/**
 * 手写注解
 * <p>Create by Sweven on 2020/7/8 -- 14:27</p>
 * Email: sweventears@163.com
 */
public class Inject {
    public static void init(Activity activity) {
        injectContent(activity);
        injectView(activity);
    }

    private static void injectContent(Activity activity) {
        Class<? extends Context> clazz = activity.getClass();
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null) {
            int layoutId = contentView.value();
            activity.setContentView(layoutId);
        }
    }

    private static void injectView(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            BindView bindView = field.getAnnotation(BindView.class);
            if (bindView != null) {
                int resId = bindView.value();
                View view = activity.findViewById(resId);
                field.setAccessible(true);
                try {
                    field.set(activity, view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
