package com.sweven.util;

import android.content.Context;
import android.os.Handler;

/**
 * 延时操作
 * delay默认2000ms
 * </p>
 * Created by Sweven on 2019/5/3.
 * Email:sweventears@Foxmail.com
 */
public class WaitManager {
    private Handler handler = new Handler();
    private CallBack callBack;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (callBack != null) {
                callBack.call();
            }
        }
    };
    private Context context;
    private long delay = 2000L;

    public WaitManager(Context context, CallBack callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    public WaitManager(Context context, CallBack callBack, Long delay) {
        this.context = context;
        this.callBack = callBack;
        if (delay == null) {
            delay = 2000L;
        }
        this.delay = delay;
    }

    public void pause() {
        try {
            handler.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        start(delay);
    }

    public void stop() {
        handler.removeCallbacks(runnable);
        handler = null;
    }

    public long getDelay() {
        return delay;
    }

    private void start(long delay) {
        handler.postDelayed(runnable, delay);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        void call();
    }
}
