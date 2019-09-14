package com.sweven.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;
import com.sweven.util.R;

/**
 * Created by Sweven on 2019/7/19--17:33.
 * Email: sweventears@foxmail.com
 */
public class RefreshRecyclerHeadView extends FrameLayout implements IHeaderView {
    private Context context;

    private RelativeLayout layout;
    private ImageView loading;
    private ImageView arrow;
    private TextView refreshingText;

    private AnimationDrawable animationDrawable;
    private String pullDownStr = "下拉刷新";
    private String releaseRefreshStr = "释放刷新";
    private String releaseDoneStr = "刷新成功";

    public RefreshRecyclerHeadView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerHeadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_refresh_recycler_head_view, null);
        layout = view.findViewById(R.id.refresh_recycler_head_view);
        loading = view.findViewById(R.id.imageView);
        arrow = view.findViewById(R.id.iv_arrow);
        refreshingText = view.findViewById(R.id.refreshing_text);
        addView(view);
        reset();
    }

    @Override
    public View getView() {
        return this;
    }

    public void setRefreshDrawable(int resId) {
        loading.setImageResource(resId);
    }

    public void setRefreshingStr(String str) {
        refreshingText.setText(str);
    }

    public void setBackground(int color) {
        layout.setBackgroundColor(color);
    }

    /**
     * 下拉准备刷新动作
     *
     * @param fraction      当前下拉高度与总高度的比
     * @param maxHeadHeight
     * @param headHeight
     */
    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f) setRefreshingStr(pullDownStr);
        if (fraction > 1f) setRefreshingStr(releaseRefreshStr);
        if (arrow.getVisibility() == GONE) {
            arrow.setVisibility(VISIBLE);
        }
        arrow.setRotation(fraction * headHeight / maxHeadHeight * 180);
    }

    /**
     * 下拉释放过程
     *
     * @param fraction
     * @param maxHeadHeight
     * @param headHeight
     */
    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f) {
            setRefreshingStr(pullDownStr);
            arrow.setRotation(fraction * headHeight / maxHeadHeight * 180);
            if (arrow.getVisibility() == GONE) {
                arrow.setVisibility(VISIBLE);
                refreshingText.setVisibility(VISIBLE);
                loading.setVisibility(GONE);
            }
        }
    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        arrow.setVisibility(GONE);
        refreshingText.setVisibility(GONE);
        loading.setVisibility(View.VISIBLE);

        loading.clearAnimation();
        animationDrawable = (AnimationDrawable) loading.getDrawable();
        animationDrawable.start();
    }

    @Override
    public void onFinish(OnAnimEndListener animEndListener) {
        reset();
        animEndListener.onAnimEnd();
    }

    /**
     * 用于在必要情况下复位View，清除动画
     */
    @Override
    public void reset() {
        loading.clearAnimation();
        animationDrawable = (AnimationDrawable) loading.getDrawable();
        animationDrawable.stop();
        loading.setVisibility(GONE);
        refreshingText.setVisibility(VISIBLE);
        setRefreshingStr(releaseDoneStr);
    }
}
