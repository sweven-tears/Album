package com.sweven.console;

/**
 * Created by Sweven on 2020/1/7--14:46.
 * Email: sweventears@foxmail.com
 */
public class Console {
    public static void log(Object... msg) {
        if (msg == null) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (Object o : msg) {
            builder.append(o.toString()).append("\t");
        }
        System.out.println(builder.toString());
    }

    public static void err(Object... msg) {
        if (msg == null) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (Object o : msg) {
            builder.append(o.toString()).append("\t");
        }
        System.err.println(builder.toString());
    }
}
