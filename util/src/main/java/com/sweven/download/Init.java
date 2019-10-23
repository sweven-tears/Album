package com.sweven.download;

import com.sweven.download.interf.OKHttpResourceListener;
import com.sweven.download.interf.ProgressListener;
import com.sweven.download.response.ProgressResponseBody;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 下载线程初始类
 */
public class Init {
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10000, TimeUnit.MILLISECONDS)
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .writeTimeout(10000, TimeUnit.MILLISECONDS).build();

    /**
     * 对外下载方法
     * @param url 下载地址
     * @param OKHttpResourceListener 自定义接口
     */
    public static void downloadFile(String url, OKHttpResourceListener OKHttpResourceListener) {
        ProgressListener progressListener = new ProgressListener() {
            @Override
            public void onProgress(long currentBytes, long contentLength, boolean done) {
                OKHttpResourceListener.onProgress(currentBytes, contentLength, done);
            }
        };
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                OKHttpResourceListener.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                OKHttpResourceListener.onResponse(call, response);
            }
        };
        downloadFile(url, progressListener, callback);
    }

    //下载文件方法
    private static void downloadFile(String url, final ProgressListener listener, Callback callback) {
        //增加拦截器
        OkHttpClient client = okHttpClient.newBuilder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                System.out.println(response.body());
                return response.newBuilder().body(new ProgressResponseBody(response.body(), listener)).build();
            }
        }).build();

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}
