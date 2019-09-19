package com.sweven.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FileUtil {

    private static LogUtil log = new LogUtil("FileUtil");

    /**
     * 安全获取文件夹路径：
     * 不存在则创建
     *
     * @param path 文件夹路径
     * @return File
     */
    public static File directory(String path) {
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                log.w("Unable to create external files directory");
                return null;
            }
        }
        return file;
    }

    /**
     * 安全获取文件路径：
     * 不存在则创建
     *
     * @param path 文件路径
     * @return File
     */
    public static File file(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    log.w("Unable to create external files");
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.w("Unable to create external files");
                return null;
            }
        }
        return file;
    }

    /**
     * 获取app数据默认储存路径的files路径
     *
     * @param context 上下文
     * @return File
     */
    public static File getExternalFilesDir(Context context) {
        File dataDir = new File(new File(getSDCardPath(), "Android"), "data");
        File appFilesDir = new File(new File(dataDir, context.getPackageName()), "files");
        if (!appFilesDir.exists()) {
            if (!appFilesDir.mkdirs()) {
                log.w("Unable to create external files directory");
                return null;
            }
        }
        return appFilesDir;
    }

    /**
     * 获取app数据默认储存路径的cache路径
     *
     * @param context 上下文
     * @return File
     */
    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(getSDCardPath(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                log.w("Unable to create external cache directory");
                return null;
            }
        }
        return appCacheDir;
    }

    /**
     * @return 手机根目录File
     */
    public static File getSDCard() {
        return Environment.getExternalStorageDirectory();
    }

    /**
     * @return 手机根目录path
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory() + "/";
    }

    /**
     * 安装软件
     *
     * @param context 上下文
     * @param apkPath apk文件路径
     */
    public static void installApk(Context context, String apkPath) {
        File apkFile = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //如果没有添加这条，安装完毕后不会有打开选项，而是直接退出。
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 创建文件或文件夹
     *
     * @param fileName 文件名或问文件夹名
     */
    public static void createFile(String fileName) {
        File file = new File(getSDCardPath() + fileName);
        if (fileName.indexOf(".") != -1) {
            // 说明包含，即使创建文件, 返回值为-1就说明不包含.,即使文件
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("创建了文件");
        } else {
            // 创建文件夹
            file.mkdir();
            System.out.println("创建了文件夹");
        }

    }

    /**
     * delete directory
     */
    public static boolean deleteDirectory(File root) {
        File files[] = root.listFiles();
        if (files != null) {
            for (File f : files) {
                if (!f.isDirectory() && f.exists()) { // 判断是否存在
                    if (!f.delete()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * delete file
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isFile()) {
                if (!file.delete()) {
                    return false;
                }
            } else {
                String[] filePaths = file.list();
                for (String path : filePaths) {
                    deleteFile(filePath + File.separator + path);
                }
                if (!file.delete()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param folder  文件夹
     * @param endName 后缀名
     * @return 同后缀名的所有文件
     */
    public static List<String> getFilesByEndName(String folder, String... endName) {
        List<String> files = new ArrayList<>();
        File f = directory(folder);
        if (f == null) {
            return files;
        }
        for (File file : f.listFiles()) {
            for (String end : endName) {
                if (!file.isDirectory() && file.getName().endsWith("."+end)) {
                    files.add(file.getPath());
                }
            }
        }
        return files;
    }

    /**
     * @param folder  文件夹
     * @param endName 后缀名
     * @return 同后缀名的所有文件
     */
    public static List<String> getFilesByEndName(String folder, Set<String> endName) {
        List<String> files = new ArrayList<>();
        File f = directory(folder);
        if (f == null) {
            return files;
        }
        for (File file : f.listFiles()) {
            for (String end : endName) {
                if (!file.isDirectory() && file.getName().endsWith("."+end)) {
                    files.add(file.getPath());
                }
            }
        }
        return files;
    }

    public static boolean isEndName(String path, String... endName) {
        File file = new File(path);
        for (String end : endName) {
            if (!file.isDirectory() && file.getName().endsWith("."+end)) {
                return true;
            }
        }
        return false;
    }
}
