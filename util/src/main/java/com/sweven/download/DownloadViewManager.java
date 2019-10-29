package com.sweven.download;

import com.sweven.download.dialog.DownloadDialog;
import com.sweven.download.interf.DownloadListener;

/**
 * 下载弹窗管理类<p>
 * 可以设置弹窗的<p>
 * 是否可以点击返回关闭弹窗<p>
 * 是否可以点击其他位置关闭弹窗<p>
 * 未下载时的提示信息、<p>
 * 下载进行时的提示信息、<p>
 * 可以隐藏取消按钮<p>
 * {onlyConfirm}默认为false<p>
 * {onlyConfirm=true}时隐藏取消按钮<p>
 * {onlyConfirm=false}时显示取消按钮<p>
 */
public class DownloadViewManager {
    private DownloadDialog dialog;
    private String label;
    private String loadingLabel;
    private boolean onlyConfirm;


    DownloadViewManager(DownloadUtil downloadUtil) {
        this.dialog = downloadUtil.getDialog();
    }

    public DownloadViewManager onlyConfirm(boolean onlyConfirm) {
        this.onlyConfirm = onlyConfirm;
        return this;
    }

    public DownloadViewManager label(String label) {
        this.label = label;
        return this;
    }

    public DownloadViewManager loadingLabel(String label) {
        this.loadingLabel = label;
        return this;
    }

    public DownloadViewManager cancelable(boolean flag) {
        dialog.setCancelable(flag);
        return this;
    }

    public DownloadViewManager canceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 不设置监听事件直接开始下载
     */
    public void start() {
        dialog.show();
    }

    /**
     * 设置监听事件再开始下载
     *
     * @param listener 监听事件
     */
    public void start(DownloadListener listener) {
        dialog.setDownloadListener(listener);
        dialog.setLabel(label);
        dialog.setLoadingLabel(loadingLabel);
        dialog.setOnlyConfirm(onlyConfirm);
        start();
    }
}
