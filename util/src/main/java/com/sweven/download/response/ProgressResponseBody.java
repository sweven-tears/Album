package com.sweven.download.response;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.sweven.download.ProgressModel;
import com.sweven.download.interf.ProgressListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 自定义加载进度 ResponseBody
 */
public class ProgressResponseBody extends ResponseBody {
    private static final int UPDATE = 0x01;
    private ResponseBody responseBody;
    private ProgressListener mListener;
    private BufferedSource bufferedSource;
    private Handler myHandler;

    public ProgressResponseBody(ResponseBody body, ProgressListener listener) {
        responseBody = body;
        mListener = listener;
        myHandler = new MyHandler();
    }

    /**
     * 将进度放到主线程中显示
     */
    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {

        MyHandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE) {
                ProgressModel progressModel = (ProgressModel) msg.obj;
                //接口返回
                if (mListener != null)
                    mListener.onProgress(progressModel.getCurrentBytes(), progressModel.getContentLength(), progressModel.isDone());
            }
        }
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {

        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(mySource(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source mySource(Source source) {

        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                //发送消息到主线程，ProgressModel为自定义实体类
                Message msg = Message.obtain();
                msg.what = UPDATE;
                msg.obj = new ProgressModel(totalBytesRead, contentLength(), totalBytesRead == contentLength());
                myHandler.sendMessage(msg);

                return bytesRead;
            }
        };
    }
}
