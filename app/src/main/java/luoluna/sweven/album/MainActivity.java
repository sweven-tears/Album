package luoluna.sweven.album;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
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
    private ImageView addIv;
    private ImageView doneIv;
    private ImageView arrow;
    private ImageView refreshIv;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private List<AlbumAtlasFragment> fragments = new ArrayList<>();
    private int currentIndex = SYSTEM_ALBUM;
    private AlbumAtlasFragment currentFragment = new AlbumAtlasFragment();
    private AlbumAtlasFragment albumFragment = AlbumAtlasFragment.newInstance(SYSTEM_ALBUM);
    private AlbumAtlasFragment atlasFragment = AlbumAtlasFragment.newInstance(CUSTOMER_ATLAS);

    private boolean refreshing;

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.done_image:
                showMoreMenu(doneIv);
                break;
            case R.id.refresh_image:
                if (!refreshing) {
                    currentFragment.setAdapter(this::finishRefresh);
                }
                break;
            case R.id.add_image:
                currentFragment.addAtlas();
//                Intent i = new Intent
//                (Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivity(i);
                break;
            case R.id.title_panel:
                showChooseMenu(title);
                break;
        }
    }

    private void finishRefresh() {
        AnimationUtil.with().stopRotateConstantSpeed(refreshIv);
        refreshing = false;
    }

    /**
     * 唤出菜单
     *
     * @param view 绑定菜单的组件
     */
    private void showChooseMenu(View view) {
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
        popupMenu.setOnDismissListener(menu ->
                // menu 关闭时 arrow 的动画
                AnimationUtil.with().loadAnimation(this, arrow, R.anim.collapse_menu));

        popupMenu.show();
    }

    /**
     * 唤出菜单
     *
     * @param view 绑定菜单的组件
     */
    private void showMoreMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.album_more_settings, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.cut_show_view:
                    // 切换视图
                    if (App.album == App.BIG_ALBUM) {
                        App.album = App.ROLL_ALBUM;
                    } else {
                        App.album = App.BIG_ALBUM;
                    }
                    Setting.getInstance().save(this);
                    currentFragment.cutShowView();
                    break;
                case R.id.album_settings:
                    toast.showShort("打开设置");
                    break;
            }
            return false;
        });
        popupMenu.show();
    }

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
    }
}
