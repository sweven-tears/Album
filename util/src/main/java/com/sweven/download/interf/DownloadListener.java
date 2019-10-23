package com.sweven.download.interf;

import java.io.IOException;

public interface DownloadListener {
    /**
     * 下载时第一步的取消按钮监听
     */
    void onCancel();

    /**
     * 下载时第一步的确定按钮监听
     */
    void onConfirm();

    /**
     * @param currentBytes 当前下载量
     * @param totalBytes   总大小
     */
    void onProgress(long currentBytes, long totalBytes);

    /**
     * 下载成功的回调事件响应
     *
     * @param path 下载路径
     * @param name 下载文件名
     */
    void onSuccess(String path, String name);

    /**
     * 下载失败的回调事件响应
     *
     * @param e error
     */
    void onFails(IOException e);
}
