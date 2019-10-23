package com.sweven.download;

import com.sweven.download.dialog.DownloadDialog;
import com.sweven.download.interf.DownloadListener;

/**
 * 下载弹窗管理类
 * <br>可以设置弹窗的</br>
 * <br>是否可以点击返回关闭弹窗</br>
 * <br>是否可以点击其他位置关闭弹窗</br>
 * <br>未下载时的提示信息、</br>
 * <br>下载进行时的提示信息、</br>
 * <br>可以隐藏取消按钮</br>
 * <br>{onlyConfirm}默认为false</br>
 * <br>{onlyConfirm=true}时隐藏取消按钮</br>
 * <br>{onlyConfirm=false}时显示取消按钮</br>
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

    public void start() {
        dialog.show();
    }

    public void start(DownloadListener listener) {
        dialog.setDownloadListener(listener);
        dialog.setLabel(label);
        dialog.setLoadingLabel(loadingLabel);
        dialog.setOnlyConfirm(onlyConfirm);
        dialog.show();
    }
}
