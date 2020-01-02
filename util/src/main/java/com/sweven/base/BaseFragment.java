package com.sweven.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sweven.console.LogUtil;
import com.sweven.console.ToastUtil;

/**
 * Created by Sweven on 2019/7/2.
 * Email:sweventears@Foxmail.com
 */
public class BaseFragment extends Fragment {

    public final String TAG = this.getClass().getSimpleName();

    protected View fragment;
    protected Activity activity;
    protected Context context;

    protected ToastUtil toast;
    protected LogUtil log;

    @Override
    public void onAttach(Context context) {
        new LogUtil(TAG).d("onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        new LogUtil(TAG).d("onCreate");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        new LogUtil(TAG).d("onCreateView");
        // 绑定view inflater.inflate(R.layout.xxx,null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        new LogUtil(TAG).d("onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        context = getContext();
        toast = new ToastUtil(activity);
        log = new LogUtil(TAG);
    }

    /**
     * 绑定view中的组件
     */
    protected void bindView() {
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 该方法只能在上级activity中
     * 所有相关联的layout中
     * <bold>没有重复的resId的时候使用</bold>
     *
     * @param resId 资源id
     * @param <T>   继承view的泛型
     * @return 组件
     */
    protected <T extends View> T bindId(@IdRes int resId) {
        return activity.findViewById(resId);
    }

    /**
     * 通过onCreateView()获取的view来绑定组件
     *
     * @param view  该fragment的容器view
     * @param resId 资源id
     * @param <T>   继承view的泛型
     * @return 组件
     */
    protected <T extends View> T bindId(View view, @IdRes int resId) {
        return view.findViewById(resId);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        new LogUtil(TAG).d("Hidden change--" + hidden);
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onStart() {
        new LogUtil(TAG).d("onStart");
        super.onStart();
    }

    @Override
    public void onStop() {
        new LogUtil(TAG).d("onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        new LogUtil(TAG).d("onDestroy");
        super.onDestroy();
    }
}
