package com.sweven.download;

import android.content.Context;

import com.sweven.download.dialog.DownloadDialog;

/**
 * 下载工具类
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
