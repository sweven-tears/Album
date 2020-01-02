package com.sweven.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sweven.console.LogUtil;
import com.sweven.console.ToastUtil;

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
    }

    private void init() {
        toast = new ToastUtil(this);
        log = new LogUtil(TAG);
    }

    /**
     * @param res 资源id
     * @param <T> 子类view
     * @return view
     */
    protected <T extends View> T bindId(@IdRes int res) {
        return findViewById(res);
    }

    /**
     * 绑定组件id
     */
    protected void bindView() {
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * @param cls 目标activity class
     */
    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    /**
     * @param cls         目标activity class
     * @param requestCode code
     */
    protected void startActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, requestCode);
    }

    /**
     * @param cls   目标activity class
     * @param value 键值对 例如：key:value
     */
    protected void startActivity(Class<?> cls, String... value) {
        Intent intent = new Intent(this, cls);
        for (String s : value) {
            String[] v = s.split(":", 2);
            intent.putExtra(v[0], v[1]);
        }
        startActivity(intent);
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

    /**
     * 返回键的事件
     */
    protected boolean onBack() {
        finish();
        log.v("onBack()");
        return true;
    }
}
