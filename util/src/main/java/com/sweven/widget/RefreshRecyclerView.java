package com.sweven.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.sweven.util.R;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by Sweven on 2019/7/19--14:39.
 * Email: sweventears@foxmail.com
 * 需要配置
 */
public class RefreshRecyclerView extends FrameLayout {
    private static final int REFRESH = 0x101;
    private static final int LOAD_MORE = 0x201;
    private static final int FLOAT_ALPHA = 0x88;
    private static final int NO_ALPHA = 0xFF;

    private Context context;

    private TwinklingRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ImageView actionButton;

    private RecyclerViewNoBugLinearLayoutManager layoutManager;
    private RefreshListenerAdapter refreshListener;

    private RefreshRecyclerHeadView headerView;
    private RefreshRecyclerBottomView bottomView;

    private int TYPE;
    private int showPosition = 5;
    private boolean showTopButton = false;//默认不显示
    private boolean moveTopButton = false;//默认不可移动

    public RefreshRecyclerView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @SuppressLint("WrongConstant")
    private void init() {
        LayoutInflater.from(context).inflate(R.layout.layout_refresh_recycler_view, this);
        refreshLayout = findViewById(R.id.refresh);
        recyclerView = findViewById(R.id.recycler);
        actionButton = findViewById(R.id.action_button);

        actionButton.getDrawable().setAlpha(FLOAT_ALPHA);
        layoutManager = new RecyclerViewNoBugLinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        initRefreshLayout();
        initRecyclerView();
        initAction();
    }


    private int left, top, right, bottom, lastX, lastY;
    private int screenWidth;
    private int screenHeight;
    private boolean pressButton;

    @SuppressLint("ClickableViewAccessibility")
    private void initAction() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels - 50;

        actionButton.setVisibility(GONE);
        actionButton.setOnClickListener(v -> stickTop());
        actionButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case ACTION_DOWN:
                    actionButton.getDrawable().setAlpha(NO_ALPHA);
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    pressButton = true;
                    break;
                case ACTION_UP:
                    actionButton.getDrawable().setAlpha(FLOAT_ALPHA);
                    if (pressButton) {
                        stickTop();
                    }
                    break;
                case ACTION_MOVE:
                    if (moveTopButton) {
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        left = v.getLeft() + dx;
                        top = v.getTop() + dy;
                        right = v.getRight() + dx;
                        bottom = v.getBottom() + dy;
                        if (left < 0) {
                            left = 0;
                            right = left + v.getWidth();
                        }
                        if (right > screenWidth) {
                            right = screenWidth;
                            left = right - v.getWidth();
                        }
                        if (top < 0) {
                            top = 0;
                            bottom = top + v.getHeight();
                        }
                        if (bottom > screenHeight) {
                            bottom = screenHeight;
                            top = bottom - v.getHeight();
                        }
                        v.layout(left, top, right, bottom);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                    }
                    pressButton = false;
                    break;
            }
            return moveTopButton;
        });
    }

    private void initRecyclerView() {
        recyclerView.setNestedScrollingEnabled(false);
        try {
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (showTopButton) {
                        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        int position = manager.findFirstVisibleItemPosition();
                        if (position > showPosition) {
                            actionButton.setVisibility(VISIBLE);
                            if (left != 0 && top != 0 && right != 0 && bottom != 0 && lastX != 0 && lastY != 0) {
                                actionButton.layout(left, top, right, bottom);
                            }
                        } else {
                            actionButton.setVisibility(GONE);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(getClass().getName(), "RecyclerView的LayoutManager必须是LinearLayoutManager");
        }
    }

    private void initRefreshLayout() {
        refreshLayout.setNestedScrollingEnabled(false);
        headerView = new RefreshRecyclerHeadView(context);
        refreshLayout.setHeaderView(headerView);

        bottomView = new RefreshRecyclerBottomView(context);
        refreshLayout.setBottomView(bottomView);

        refreshLayout.setOnRefreshListener(new com.lcodecore.tkrefreshlayout.RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                TYPE = REFRESH;
                if (refreshListener != null) {
                    refreshListener.onRefresh();
                }
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                TYPE = LOAD_MORE;
                if (refreshListener != null) {
                    refreshListener.onLoadMore();
                }
            }

            @Override
            public void onFinishRefresh() {
                if (refreshListener != null) {
                    refreshListener.onFinishRefresh();
                }
            }

            @Override
            public void onFinishLoadMore() {
                if (refreshListener != null) {
                    refreshListener.onFinishLoadMore();
                }
            }

            @Override
            public void onRefreshCanceled() {
                if (refreshListener != null) {
                    refreshListener.onRefreshCanceled();
                }
            }

            @Override
            public void onLoadmoreCanceled() {
                if (refreshListener != null) {
                    refreshListener.onLoadMoreCanceled();
                }
            }
        });
    }

    /**
     * @param color 背景色
     */
    public void setBackground(int color) {
        headerView.setBackground(color);
        bottomView.setBackground(color);
    }

    /**
     * @param showTopButton 是否使用置顶按钮
     * @param position      显示置顶按钮时RecyclerView的position值,默认为5
     *                      当position<=0时，默认position=5;
     */
    public void showTopButton(boolean showTopButton, int position) {
        this.showTopButton = showTopButton;
        if (position > 0) {
            this.showPosition = position;
        }
    }

    /**
     * @param moveTopButton 设置置顶按钮可否自由移动
     */
    public void setTopButtonMoveEnable(boolean moveTopButton) {
        this.moveTopButton = moveTopButton;
    }

    /**
     * 设置是否能下拉刷新
     *
     * @param enable 可行性
     */
    public void enableRefresh(boolean enable) {
        refreshLayout.setEnableRefresh(enable);
    }

    /**
     * 设置是否能上拉加载
     *
     * @param enable 可行性
     */
    public void enableLoadMore(boolean enable) {
        refreshLayout.setEnableLoadmore(enable);
    }

    /**
     * 设置普通 RecyclerView
     */
    public void defaultRecyclerView() {
        enableRefresh(false);
        enableLoadMore(false);
    }

    public void enableOverScroll(boolean enable) {
        refreshLayout.setEnableOverScroll(enable);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    public void refreshComplete() {
        if (TYPE == REFRESH) {
            refreshLayout.finishRefreshing();
        } else if (TYPE == LOAD_MORE) {
            refreshLayout.finishLoadmore();
        }
    }

    public void autoRefresh() {
        refreshLayout.startRefresh();
    }

    public void stickTop() {
        recyclerView.getLayoutManager().scrollToPosition(2);
        smoothMoveToPosition(recyclerView, 0);
    }

    /**
     * 平滑的滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position);
        }
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        recyclerView.setLayoutManager(manager);
    }

    public void setRefreshListener(RefreshListenerAdapter refreshListener) {
        if (refreshListener != null) {
            this.refreshListener = refreshListener;
        }
    }

    public TwinklingRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        recyclerView.addItemDecoration(itemDecoration);
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        recyclerView.removeItemDecoration(itemDecoration);
    }

    public interface OnRefreshListener {
        /**
         * 下拉中
         */
        void onPullingDown();

        /**
         * 上拉
         */
        void onPullingUp();

        /**
         * 下拉松开
         */
        void onPullDownReleasing();

        /**
         * 上拉松开
         */
        void onPullUpReleasing();

        /**
         * 刷新中。。。
         */
        void onRefresh();

        /**
         * 加载更多中
         */
        void onLoadMore();

        /**
         * 手动调用finishRefresh或者finishLoadmore之后的回调
         */
        void onFinishRefresh();

        void onFinishLoadMore();

        /**
         * 正在刷新时向上滑动屏幕，刷新被取消
         */
        void onRefreshCanceled();

        /**
         * 正在加载更多时向下滑动屏幕，加载更多被取消
         */
        void onLoadMoreCanceled();
    }

    public abstract static class RefreshListenerAdapter implements OnRefreshListener {
        @Override
        public void onRefresh() {

        }

        @Override
        public void onLoadMore() {

        }

        /**
         * 下拉中
         */
        @Override
        public void onPullingDown() {

        }

        /**
         * 上拉
         */
        @Override
        public void onPullingUp() {

        }

        /**
         * 下拉松开
         */
        @Override
        public void onPullDownReleasing() {

        }

        /**
         * 上拉松开
         */
        @Override
        public void onPullUpReleasing() {

        }

        /**
         * 手动调用finishRefresh或者finishLoadmore之后的回调
         */
        @Override
        public void onFinishRefresh() {

        }

        @Override
        public void onFinishLoadMore() {

        }

        /**
         * 正在刷新时向上滑动屏幕，刷新被取消
         */
        @Override
        public void onRefreshCanceled() {

        }

        /**
         * 正在加载更多时向下滑动屏幕，加载更多被取消
         */
        @Override
        public void onLoadMoreCanceled() {

        }
    }
}
