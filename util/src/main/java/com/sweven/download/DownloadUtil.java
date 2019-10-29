package com.sweven.download;

import android.content.Context;

import com.sweven.download.dialog.DownloadDialog;

/**
 * 下载工具类<p>
 * 示例：<p>
 * DownloadUtil.init(this)<p>
 * .url("...")<p>
 * .path("path")<p>
 * .name("download.zip")<p>
 * .build()<p>
 * .label("发现有可更新内容")<p>
 * .loadingLabel("正在更新...")<p>
 * .cancelable(false)<p>
 * .canceledOnTouchOutside(false)<p>
 * .onlyConfirm(false)<p>
 * .start(new DownloadListenerAdapter() {...});<p>
 */
public class DownloadUtil {
    private DownloadDialog dialog;
    private String name;
    private String path;
    private String url;

    public static DownloadUtil init(Context context) {
        DownloadUtil downloadUtil = new DownloadUtil();
        downloadUtil.dialog = new DownloadDialog(context);
        return downloadUtil;
    }

    /**
     * 自定义下载的弹窗组件
     *
     * @param dialog 自定义dialog
     * @param <T>    {@link DownloadDialog}的泛型
     */
    public static <T extends DownloadDialog> DownloadUtil init(T dialog) {
        DownloadUtil downloadUtil = new DownloadUtil();
        downloadUtil.dialog = dialog;
        return downloadUtil;
    }

    public DownloadUtil path(String path) {
        this.path = path;
        return this;
    }

    public DownloadUtil name(String name) {
        this.name = name;
        return this;
    }

    public DownloadUtil url(String url) {
        this.url = url;
        return this;
    }

    public DownloadViewManager build() {
        dialog.setPath(path);
        dialog.setName(name);
        dialog.setUrl(url);
        return new DownloadViewManager(this);
    }

    DownloadDialog getDialog() {
        return dialog;
    }
}
