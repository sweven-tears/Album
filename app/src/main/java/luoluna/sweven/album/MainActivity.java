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

import luoluna.sweven.album.app.App;
import luoluna.sweven.album.fragment.main.AlbumAtlasFragment;
import luoluna.sweven.album.manager.Setting;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static int SYSTEM_ALBUM = 0;
    public static int CUSTOMER_ATLAS = 1;

    private TextView title;
    private TextView selectTv;
    private TextView finishTv;
    private ImageView addIv;
    private ImageView doneIv;
    private ImageView arrow;
    private ImageView refreshIv;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private List<AlbumAtlasFragment> fragments = new ArrayList<>();
    private AlbumAtlasFragment albumFragment = AlbumAtlasFragment.newInstance(SYSTEM_ALBUM);
    private AlbumAtlasFragment atlasFragment = AlbumAtlasFragment.newInstance(CUSTOMER_ATLAS);
    private Fragment currentFragment = new Fragment();// 当前显示fragment
    private int currentIndex = SYSTEM_ALBUM;// 当前显示位置

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
        finishTv = bindId(R.id.done_text);
        refreshIv = bindId(R.id.refresh_image);
        doneIv = bindId(R.id.done_image);
        addIv = bindId(R.id.add_image);
        arrow = bindId(R.id.pucker_arrow);
    }

    @Override
    protected void initData() {
        title.setText(R.string.index_title);

        // default system album no add Iv
        addIv.setVisibility(View.GONE);

        fragments.add(albumFragment);
        fragments.add(atlasFragment);
        showFragment(currentIndex);
        editState(false);
    }

    /**
     * 通过{@link FragmentTransaction}添加、隐藏、显示fragment
     */
    private void showFragment(int index) {
        AlbumAtlasFragment fragment = fragments.get(index);
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

        // 设置监听器
        if (currentIndex == CUSTOMER_ATLAS) {
            ((AlbumAtlasFragment) currentFragment).addListener(this::onSelectedChange);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.done_image:
                showMoreMenu(doneIv);
                break;
            case R.id.refresh_image:
                if (!refreshing) {
                    ((AlbumAtlasFragment) currentFragment).setAdapter(this::finishRefresh);
                }
                break;
            case R.id.add_image:
                ((AlbumAtlasFragment) currentFragment).addAtlas();
//                Intent i = new Intent
//                (Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivity(i);
                break;
            case R.id.title_panel:
                showChooseMenu(title);
                break;
            case R.id.done_text:
                editState(false);
                ((AlbumAtlasFragment) currentFragment).closeEdit();
                break;
            case R.id.select_text:
                ((AlbumAtlasFragment) currentFragment).selectAll();
                break;
        }
    }

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
        AnimationUtil.with().stopRotateConstantSpeed(refreshIv);
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
                    showFragment(SYSTEM_ALBUM);
                    break;
                case R.id.customer_atlas:
                    addIv.setVisibility(View.VISIBLE);
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
     * 唤出菜单
     *
     * @param view 绑定菜单的组件
     */
    private void showMoreMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(!edit ? R.menu.album_more_settings : R.menu.atlas_editor, popupMenu.getMenu());

        // 当前为相册时隐藏编辑按钮
        if (currentIndex == SYSTEM_ALBUM) {
            popupMenu.getMenu().removeItem(R.id.editor);
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.cut_show_view:
                    // 切换视图
                    if (App.album == App.BIG_ALBUM) {
                        App.album = App.ROLL_ALBUM;
                    } else {
                        App.album = App.BIG_ALBUM;
                    }
                    // 保存设置
                    Setting.getInstance().save(this);
                    ((AlbumAtlasFragment) currentFragment).cutShowView();
                    AlbumAtlasFragment.showViewChange = true;
                    break;
                case R.id.album_settings:
                    toast.showShort("打开设置");
                    break;
                case R.id.editor:
                    ((AlbumAtlasFragment) currentFragment).edit();
                    editState(true);
                    break;
                //------------编辑状态的菜单----------//
                case R.id.delete_item:
                    toast.showShort("delete");
                    break;
                case R.id.share_item:
                    toast.showShort("share");
                    break;
                case R.id.rename_item:
                    toast.showShort("rename");
                    break;
            }
            return false;
        });
        popupMenu.show();
    }

    /**
     * 修改编辑状态
     *
     * @param state 编辑状态
     */
    private void editState(boolean state) {
        refreshIv.setVisibility(state ? View.GONE : View.VISIBLE);
        addIv.setVisibility(
                currentIndex == SYSTEM_ALBUM ?
                        View.GONE :
                        state ? View.GONE : View.VISIBLE);

        selectTv.setVisibility(state ? View.VISIBLE : View.GONE);
        finishTv.setVisibility(state ? View.VISIBLE : View.GONE);

        edit = state;
    }
}
