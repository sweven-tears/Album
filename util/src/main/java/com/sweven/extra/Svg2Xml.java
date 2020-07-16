package com.sweven.extra;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Created by Sweven on 2020/3/1--14:17.
 * Email: sweventears@foxmail.com
 */
public class Svg2Xml {

    public static void main(String[] arg) {
        File file = new File("C:\\Users\\Administrator\\Desktop\\1.svg");
        svg2xml(file);

        file = new File("C:\\Users\\Administrator\\Desktop\\2.svg");
        svg2xml(file);

    }

    /**
     * 将.svg文件转换为安卓可用的.xml
     *
     * @param file 文件路径
     */
    public static void svg2xml(File file) {
        if (!file.exists() && file.isDirectory()) {
            return;
        }

        FileWriter fw = null;
        FileReader fr = null;
        ArrayList<String> paths = new ArrayList<>();
        try {
            fr = new FileReader(file);

            //字符数组循环读取
            char[] buf = new char[1024];
            int len = 0;
            StringBuilder sb = new StringBuilder();
            while ((len = fr.read(buf)) != -1) {
                sb.append(new String(buf, 0, len));
            }

            //收集所有path
            collectPaths(sb.toString(), paths);
            //拼接字符串
            StringBuilder outSb = contactStr(paths);
            //写出到磁盘
            File outFile = new File(file.getParentFile(), file.getName().substring(0, file.getName().lastIndexOf(".")) + ".xml");
            fw = new FileWriter(outFile);
            fw.write(outSb.toString());

            System.out.println("OK:" + outFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拼接字符串
     *
     * @param paths
     * @return
     */
    private static StringBuilder contactStr(ArrayList<String> paths) {
        StringBuilder outSb = new StringBuilder();
        outSb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<vector xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "        android:width=\"48dp\"\n" +
                "        android:height=\"48dp\"\n" +
                "        android:viewportWidth=\"1024\"\n" +

                "        android:viewportHeight=\"1024\">\n");

        for (String path : paths) {
            outSb.append("    <path\n" +
                    "        android:fillColor=\"#FF7F47\"\nandroid:pathData=");
            outSb.append(path);
            outSb.append("/>");
        }

        outSb.append("</vector>");
        return outSb;
    }

    /**
     * 收集所有path
     *
     * @param result
     * @param paths
     */
    private static void collectPaths(String result, ArrayList<String> paths) {
        String[] split = result.split("<path");
        for (String s : split) {
            if (s.contains("path")) {
                int endIndex;
                if (!s.contains("fill")) {
                    endIndex = s.indexOf("p");
                } else {
                    endIndex = Math.min(s.indexOf("f"), s.indexOf("p"));
                }
                String path = s.substring(s.indexOf("\""), endIndex);
                paths.add(path);
            }
        }
    }
}
