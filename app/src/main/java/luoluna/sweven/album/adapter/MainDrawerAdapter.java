package luoluna.sweven.album.adapter;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.sweven.console.LogUtil;

import luoluna.sweven.album.R;

public class MainDrawerAdapter implements DrawerLayout.DrawerListener {

    private final static int CLOSED = 0;
    private final static int OPENED = 1;

    private int current = CLOSED;
    private Activity context;
    private DrawerLayout drawerLayout;
    private boolean opening;
    private boolean closing;

    public MainDrawerAdapter(Activity context) {
        this.context = context;
        init();
    }

    /**
     * 展开 drawer
     */
    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    /**
     * 关闭 drawer
     */
    public void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    /**
     * @return drawer 是否处于打开状态
     */
    public boolean isDrawerOpen() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    private void init() {
        drawerLayout = context.findViewById(R.id.main_drawer);
        drawerLayout.addDrawerListener(this);

        opening = false;
        closing = false;
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        // drawer 滑动时事件
        switch (current) {
            case CLOSED:
                opening = true;
                break;
            case OPENED:
                closing = true;
                break;
            default:
                break;
        }
        if (slideOffset == 0) {// 关闭
            opening = false;
            closing = false;
            current = CLOSED;
        } else if (slideOffset == 1) {// 展开
            opening = false;
            closing = false;
            current = OPENED;
        }
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        new LogUtil("change").i(newState + "");
    }

    /**
     * @return 是否正在运行中
     */
    public boolean opening() {
        return opening || closing;
    }
}
