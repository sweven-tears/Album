package com.sweven.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sweven on 2019/4/25.
 * Email:sweventears@Foxmail.com
 */
public class ViewUtil {
    public static void notifyMeasure(View view) {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int height;
        int tempHeight = p.height;
        if (tempHeight > 0) {
            height = View.MeasureSpec.makeMeasureSpec(tempHeight,
                    View.MeasureSpec.EXACTLY);
        } else {
            height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, height);
    }

    /**
     * {@link View view}的父类组件必须是{@link ViewGroup}类型的才行
     * 好像不用啊？？
     * 设置 {@param scale=0} 即表示高度自适应
     *
     * @param activity .
     * @param view     组件
     * @param multiple 占真个屏幕宽度的倍数
     * @param scale    宽高比例 例：宽高比例为16:9则输入16/9.0
     */
    public static void setWidthHeight(Activity activity, View view, double multiple, double scale) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int width = (int) (WindowUtil.getWindowWidth(activity) * multiple);
        layoutParams.width = width;
        if (scale != 0) {
            layoutParams.height = (int) (width / scale);
        }
        view.setLayoutParams(layoutParams);
    }

    /**
     * 同时为多个组件设置相同宽高
     *
     * @param activity .
     * @param views    多个组件
     * @param multiple 占真个屏幕宽度的倍数
     * @param scale    宽高比例 例：宽高比例为16:9则输入16/9.0
     */
    public static void setWidthHeightForViews(Activity activity, View[] views, double multiple, double scale) {
        for (View view : views) {
            setWidthHeight(activity, view, multiple, scale);
        }
    }

    /**
     * {@link View view}的父类组件必须是{@link ViewGroup}类型的才行
     *
     * @param view      组件
     * @param width_dp  view的宽度 等于0时为不设置
     * @param height_dp view的高度 等于0时为不设置
     */
    public static void setWidthHeight(View view, int width_dp, int height_dp) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (width_dp != 0) {
            layoutParams.width = width_dp;
        }
        if (height_dp != 0) {
            layoutParams.height = height_dp;
        }
        view.setLayoutParams(layoutParams);
    }

    private void animOpen(final View view, int height) {
        view.setVisibility(View.VISIBLE);
        ValueAnimator va = createDropAnim(view, 0, height);
        va.start();
    }

    private void animClose(final View view) {
        ViewUtil.notifyMeasure(view);
        int origHeight = view.getMeasuredHeight();
        ValueAnimator va = createDropAnim(view, origHeight, 0);
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        va.start();
    }

    /**
     * 使用动画的方式来改变高度解决visible不一闪而过出现
     *
     * @param view  组件
     * @param start 初始状态值
     * @param end   结束状态值
     * @return
     */
    private ValueAnimator createDropAnim(final View view, int start, int end) {
        ValueAnimator va = ValueAnimator.ofInt(start, end);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();//根据时间因子的变化系数进行设置高度
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);//设置高度
            }
        });
        return va;
    }
}
