package com.sweven;

import java.util.Map;

/**
 * <p>Create by Sweven on 2020/9/16 -- 12:57</p>
 * Email: sweventears@163.com
 */
public class App {
    private String result = null;

    public static void main(String[] args) {
        App app = new App();
        int a = app.up("ok!!!");
        Map<Thread, StackTraceElement[]> traces = Thread.getAllStackTraces();
        Thread[] threads = traces.keySet().toArray(new Thread[]{});
        for (Thread thread : threads) {
            StackTraceElement[] stackTrace = thread.getStackTrace();
            if (stackTrace.length>0) {
                System.out.println(stackTrace[1].getMethodName());
            }
        }
        Thread stop = threads[3];
        System.out.println(stop.getStackTrace()[1].getMethodName());
        do {
            if (stop.getStackTrace().length == 0) {
                System.out.println(app.getResult());
                break;
            }
        } while (true);
        if (a == 0) {
            get(app);
        }
    }

    public static int count = 0;

    public static void get(App app) {
        try {
            String result = app.getResult();
            System.out.println(result);
            System.out.println("count:" + count);
            count = 0;
        } catch (Exception e) {
            count++;
            try {
                Thread.sleep(100);
                get(app);
            } catch (InterruptedException ignore) {
            }
        }
    }

    public String getResult() {
        if (result == null) {
            throw new NullPointerException();
        }
        return result;
    }

    public int up(String str) {
        if (str == null) {
            return -1;
        }
        if (!str.isEmpty()) {
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    a(str);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }).start();
            return 0;
        }
        return 1;
    }

    public int a(String str) {
        if (str == null) return -1;
        if (!str.isEmpty()) {
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    result = "result: " + str;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }).start();
            return 0;
        }
        return 1;
    }
}
