package com.sweven.download.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sweven.download.interf.DownloadListener;
import com.sweven.util.R;

public class DownloadDialog extends BaseDialog {

    private LinearLayout tipsView, downloadView;

    private TextView labelTv, cancelTv, confirmTv, downloadLabelTv;

    private ProgressBar downloadBar;

    private DownloadListener downloadListener;

    private String label;
    private String loadingLabel;
    private String name;
    private String path;
    private String url;
    private boolean onlyConfirm;

    public DownloadDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download);

        bindView();
        initData();
    }

    /**
     * 初始化组件
     */
    private void bindView() {
        tipsView = findViewById(R.id.show_tips_view);
        downloadView = findViewById(R.id.download_view);

        labelTv = findViewById(R.id.label);
        cancelTv = findViewById(R.id.cancel);
        confirmTv = findViewById(R.id.confirm);
        downloadLabelTv = findViewById(R.id.download_label);
        downloadBar = findViewById(R.id.download_bar);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tipsView.setVisibility(View.VISIBLE);
        downloadView.setVisibility(View.GONE);

        downloadBar.setMax(10000);

        downloadLabelTv.setText(loadingLabel);
        labelTv.setText(label);

        cancelTv.setVisibility(onlyConfirm ? View.GONE : View.VISIBLE);
        findViewById(R.id.line2).setVisibility(onlyConfirm ? View.GONE : View.VISIBLE);

        // 设置取消按键事件
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downloadListener != null) {
                    downloadListener.onCancel();
                }
                cancel();
            }
        });

        // 设置确定按键事件
        confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextStep();
                if (downloadListener != null) {
                    downloadListener.onConfirm();
                }
            }
        });
    }

    /**
     * 点击确认后的下一步
     */
    protected void nextStep() {
        downloadView.setVisibility(View.VISIBLE);
        tipsView.setVisibility(View.GONE);

        download(url, path, name, downloadBar, downloadListener);
    }

    public TextView getLabel() {
        return labelTv;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDownloadTitle(String title) {
        downloadLabelTv.setText(title);
    }

    public TextView getTitle() {
        return downloadLabelTv;
    }

    public ProgressBar getDownloadBar() {
        return downloadBar;
    }

    public void setLoadingLabel(String loadingLabel) {
        this.loadingLabel = loadingLabel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setOnlyConfirm(boolean onlyConfirm) {
        this.onlyConfirm = onlyConfirm;
    }

    public void setDownloadListener(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }
}
