package luoluna.sweven.album;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sweven.base.BaseActivity;
import com.sweven.util.AnimationUtil;

import java.util.ArrayList;
import java.util.List;

import luoluna.sweven.album.adapter.MainDrawerAdapter;
import luoluna.sweven.album.adapter.MainNavAdapter;
import luoluna.sweven.album.fragment.main.HomeFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static int SYSTEM_ALBUM = 0;
    public static int CUSTOMER_ATLAS = 1;

    private TextView title;
    private TextView selectTv;
    private TextView doneTv;
    private ImageView addIv;
    private ImageView arrow;
    private ImageView MineIv;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private List<HomeFragment> fragments = new ArrayList<>();
    private HomeFragment albumFragment = HomeFragment.newInstance(SYSTEM_ALBUM);
    private HomeFragment atlasFragment = HomeFragment.newInstance(CUSTOMER_ATLAS);
    private Fragment currentFragment = new Fragment();// 当前显示fragment
    private int currentIndex = SYSTEM_ALBUM;// 当前显示位置

    private MainNavAdapter navAdapter;
    private MainDrawerAdapter drawerAdapter;

    private boolean refreshing;
    private boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        initData();
    }

    @Override
    protected void bindView() {
        title = bindId(R.id.title);
        selectTv = bindId(R.id.select_text);
        doneTv = bindId(R.id.done_text);
        MineIv = bindId(R.id.mine_image);
        addIv = bindId(R.id.add_image);
        arrow = bindId(R.id.pucker_arrow);
    }

    @Override
    protected void initData() {
        title.setText(R.string.index_title);

        drawerAdapter = new MainDrawerAdapter(this);
        navAdapter = new MainNavAdapter(this);
        navAdapter.setAdapter(drawerAdapter);

        fragments.add(albumFragment);
        fragments.add(atlasFragment);
        showFragment(currentIndex);
        editState(false);
    }

    /**
     * 通过{@link FragmentTransaction}添加、隐藏、显示fragment
     */
    private void showFragment(int index) {
        HomeFragment fragment = fragments.get(index);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragment.isAdded()) {
            transaction.hide(currentFragment)
                    .show(fragment);
        } else {
            transaction.hide(currentFragment)
                    .add(R.id.main_panel, fragment, index + "")
                    .show(fragment);

        }
        transaction.commit();
        currentIndex = index;
        currentFragment = fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_image:
//                TODO 刷新
//                if (!refreshing) {
//                    ((HomeFragment) currentFragment).setAdapter(this::finishRefresh);
//                }
                drawerAdapter.openDrawer();
                break;
            case R.id.add_image:
                ((HomeFragment) currentFragment).addAtlas();
//                Intent i = new Intent
//                (Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivity(i);
                break;
            case R.id.title_panel:
                showChooseMenu(title);
                break;
            case R.id.done_text:
                String done = doneTv.getText().toString();
                if (done.equals(getString(R.string.finish))) {
                    editState(false);
                    ((HomeFragment) currentFragment).closeEdit();
                } else if (done.equals(getString(R.string.edit))) {
                    editState(true);
                    ((HomeFragment) currentFragment).edit();
                    // 设置监听器
                    ((HomeFragment) currentFragment).addListener(this::onSelectedChange);
                }
                break;
            case R.id.select_text:
                String select = selectTv.getText().toString();
                if (select.equals(getString(R.string.select_all))) {
                    ((HomeFragment) currentFragment).selectAll();
                } else if (select.equals(getString(R.string.select_none))) {
                    ((HomeFragment) currentFragment).selectNone();
                } else {
                    // 不存在的字符
                    toast.showShort("发生异常，请重试");
                }
                break;
        }
    }

    /**
     * 当前列表选中数变化的响应事件
     *
     * @param total         当前列表的总数
     * @param selectedCount 当前列表选中数
     */
    private void onSelectedChange(int total, int selectedCount) {
        if (selectedCount < total) {
            selectTv.setText(R.string.select_all);
        } else if (selectedCount == total) {
            selectTv.setText(R.string.select_none);
        }
    }

    /**
     * 完成刷新后的操作
     */
    private void finishRefresh() {
        AnimationUtil.with().stopRotateConstantSpeed(MineIv);
        refreshing = false;// 刷新状态
    }

    /**
     * 唤出相册图集切换菜单
     *
     * @param view 绑定菜单的组件
     */
    private void showChooseMenu(View view) {
        // 编辑状态不允许切换
        if (edit) {
            toast.showShort("当前不可切换");
            return;
        }

        // 展开时 arrow 的动画
        AnimationUtil.with().loadAnimation(this, arrow, R.anim.expand_menu);

        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.choose_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            title.setText(item.getTitle());
            switch (item.getItemId()) {
                case R.id.system_album:
                    addIv.setVisibility(View.GONE);
                    doneTv.setVisibility(View.GONE);
                    showFragment(SYSTEM_ALBUM);
                    break;
                case R.id.customer_atlas:
                    addIv.setVisibility(View.VISIBLE);
                    doneTv.setVisibility(View.VISIBLE);
                    showFragment(CUSTOMER_ATLAS);
                    break;
            }
            return false;
        });
        popupMenu.setOnDismissListener(this::closeMenuAnim);

        popupMenu.show();
    }

    /**
     * 关闭相册图集选择菜单时的动画
     */
    private void closeMenuAnim(PopupMenu popupMenu) {
        AnimationUtil.with().loadAnimation(this, arrow, R.anim.collapse_menu);
    }

    /**
     * <p>修改编辑状态</p>
     * true 为编辑状态 false为非编辑状态
     *
     * @param state 编辑状态 true为编辑状态 false为非编辑状态
     */
    private void editState(boolean state) {
        MineIv.setVisibility(state ? View.GONE : View.VISIBLE);
        addIv.setVisibility(currentIndex == SYSTEM_ALBUM || state ? View.GONE : View.VISIBLE);

        selectTv.setVisibility(state ? View.VISIBLE : View.GONE);

        doneTv.setText(state ? R.string.finish : R.string.edit);

        edit = state;
    }

    @Override
    protected boolean onBack() {
        if (drawerAdapter.opening() || drawerAdapter.isDrawerOpen()) {
            drawerAdapter.closeDrawer();
            return true;
        }
        moveTaskToBack(false);
        return true;
    }
}
