package com.sweven.download.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sweven.download.Init;
import com.sweven.download.interf.DownloadListener;
import com.sweven.download.interf.OKHttpResourceListener;
import com.sweven.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Response;

public abstract class BaseDialog extends Dialog {
    private final String TAG = this.getClass().getSimpleName();

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
    }

    protected abstract void nextStep();

    /**
     * 开始下载
     *
     * @param url              下载地址
     * @param path             文件保存路径
     * @param name             文件名
     * @param progressBar      进度条
     * @param downloadListener 下载监听
     */
    protected void download(String url, String path, String name, ProgressBar progressBar, DownloadListener downloadListener) {
        Init.downloadFile(url, new OKHttpResourceListener() {
            @Override
            public void onProgress(long currentBytes, long contentLength, boolean done) {
                float progress = currentBytes * 10000 / contentLength;
                progressBar.setProgress((int) progress);
                downloadListener.onProgress(currentBytes, contentLength);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                downloadListener.onFails(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response != null) {
                        new LogUtil(TAG).i(response.toString());
                        new LogUtil(TAG).i(call.toString());
                        File dest = new File(path, name);

                        InputStream is = response.body().byteStream();
                        FileOutputStream fos = new FileOutputStream(dest);
                        int len = 0;
                        byte[] buffer = new byte[2048];
                        while (-1 != (len = is.read(buffer))) {
                            fos.write(buffer, 0, len);
                        }
                        fos.flush();
                        fos.close();
                        is.close();
                    }
                    downloadListener.onSuccess(path, name);
                } catch (IOException e) {
                    e.printStackTrace();
                    downloadListener.onFails(e);
                }
            }
        });
    }
}
