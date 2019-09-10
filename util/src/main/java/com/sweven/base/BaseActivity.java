package com.sweven.base;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sweven.util.LogUtil;
import com.sweven.util.ToastUtil;

/**
 * Created by Sweven on 2019/9/10--15:54.
 * Email: sweventears@foxmail.com
 */
public class BaseActivity extends AppCompatActivity {

    public String TAG = getClass().getSimpleName();

    public ToastUtil toast;
    public LogUtil log;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        bindView();
        initData();
    }

    private void init() {
        toast = new ToastUtil(this);
        log = new LogUtil(TAG);
    }

    protected void bindID(@IdRes int res) {
        findViewById(res);
    }

    protected void bindView() {
    }

    protected void initData() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        log.v("onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        log.v("onResume()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        log.v("onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        log.v("onDestroy()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        log.v("onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        log.v("onPause()");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return onBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    protected boolean onBack() {
        finish();
        log.v("onBack()");
        return true;
    }
}
