package com.sweven.util;

import java.io.PrintStream;

public class Console {
    private PrintStream print=null;

    public static void log(Object... log) {
        StringBuilder x = new StringBuilder();
        for (Object s : log) {
            x.append(s).append("\t");
        }
        System.out.println(x.toString());
    }

    public static void err(Object... err) {
        StringBuilder x = new StringBuilder();
        for (Object s : err) {
            x.append(s).append("\t");
        }
        System.err.println(x.toString());
    }
}
