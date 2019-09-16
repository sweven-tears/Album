package com.sweven.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lcodecore.tkrefreshlayout.IBottomView;
import com.sweven.util.R;

/**
 * Created by Sweven on 2019/7/19--17:33.
 * Email: sweventears@foxmail.com
 */
public class RefreshRecyclerBottomView extends FrameLayout implements IBottomView {
    private final String loadMoreReleaseStr = "上拉加载更多";
    private final String loadMoreDoingStr = "加载中...";
    private final String LoadMoreDoneStr = "加载完成";

    private Context context;

    private LinearLayout layout;
    private ImageView loading;
    private TextView loadMoreText;

    private AnimationDrawable animationDrawable;

    public RefreshRecyclerBottomView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerBottomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerBottomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_refresh_recycler_bottom_view, null);
        layout = view.findViewById(R.id.refresh_recycler_bottom_view);
        loading = view.findViewById(R.id.imageView);
        loadMoreText = view.findViewById(R.id.textView);
        addView(view);
        setLoadMoreText(loadMoreReleaseStr);
    }

    @Override
    public View getView() {
        return this;
    }

    public void setRefreshDrawable(int resId) {
        loading.setImageResource(resId);
    }

    public void setLoadMoreText(String str) {
        loadMoreText.setText(str);
    }

    public void setBackground(int color) {
        layout.setBackgroundColor(color);
    }

    /**
     * 上拉准备加载更多的动作
     *
     * @param fraction        上拉高度与Bottom总高度之比
     * @param maxBottomHeight 底部部可拉伸最大高度
     * @param bottomHeight    底部高度
     */
    @Override
    public void onPullingUp(float fraction, float maxBottomHeight, float bottomHeight) {
        if (loading.getVisibility() == GONE) {
            loading.setVisibility(VISIBLE);
        }
        setLoadMoreText(loadMoreReleaseStr);
    }

    /**
     * 上拉释放过程
     *
     * @param fraction
     * @param maxBottomHeight
     * @param bottomHeight
     */
    @Override
    public void onPullReleasing(float fraction, float maxBottomHeight, float bottomHeight) {

    }

    @Override
    public void onFinish() {
        reset();
    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        loading.clearAnimation();
        setLoadMoreText(loadMoreDoingStr);
        try {
            animationDrawable = (AnimationDrawable) loading.getDrawable();
            animationDrawable.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于在必要情况下复位View，清除动画
     */
    @Override
    public void reset() {
        loading.clearAnimation();
        try {
            animationDrawable = (AnimationDrawable) loading.getDrawable();
            animationDrawable.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setLoadMoreText(LoadMoreDoneStr);
        loading.setVisibility(GONE);
    }
}
