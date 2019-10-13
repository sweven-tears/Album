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

import luoluna.sweven.album.app.App;
import luoluna.sweven.album.fragment.main.AlbumFragment;

import static luoluna.sweven.album.fragment.main.AlbumFragment.CUSTOMER_ATLAS;
import static luoluna.sweven.album.fragment.main.AlbumFragment.SYSTEM_ALBUM;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static int cutIv =
            App.album == App.BIG_ALBUM ?
                    R.drawable.ic_big_album_list :
                    R.drawable.ic_roll_album_list;

    private TextView title;
    private ImageView addIv, doneIv, refreshIv, arrow;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private AlbumFragment systemAlbum = AlbumFragment.newInstance(SYSTEM_ALBUM);
    private AlbumFragment customerAtlas = AlbumFragment.newInstance(CUSTOMER_ATLAS);
    private AlbumFragment currentFragment = new AlbumFragment();

    private boolean refreshing = false;

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

        // default system album no add
        addIv.setVisibility(View.GONE);

        setFragment(SYSTEM_ALBUM);

        refreshShowViewType();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.done_image:
                if (currentFragment.getShowViewType() == App.BIG_ALBUM) {
                    doneIv.setImageResource(R.drawable.ic_roll_album_list);
//                    App.album = App.ROLL_ALBUM;
                    currentFragment.setShowViewType(App.ROLL_ALBUM);
                } else {
                    doneIv.setImageResource(R.drawable.ic_big_album_list);
//                    App.album = App.BIG_ALBUM;
                    currentFragment.setShowViewType(App.BIG_ALBUM);
                }
//                Setting.getInstance().save(this);
//                currentFragment.setAdapter(null, false, null);
                break;
            case R.id.add_image:
                currentFragment.addAlbum();
                break;
            case R.id.refresh_image:
                if (refreshing = !refreshing) {
                    AnimationUtil.with().rotateConstantSpeed(this, refreshIv);
//                    currentFragment.setAdapter(refreshIv, true, () -> refreshing = false);
                }
                break;
            case R.id.title_panel:
                showPopupMenu(title);
//                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivity(i);
                break;
        }
    }

    /**
     * 唤出菜单
     *
     * @param view 绑定菜单的组件
     */
    private void showPopupMenu(View view) {
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
                    setFragment(SYSTEM_ALBUM);
                    break;
                case R.id.customer_atlas:
                    addIv.setVisibility(View.VISIBLE);
                    setFragment(CUSTOMER_ATLAS);
                    break;
            }
            return false;
        });
        popupMenu.setOnDismissListener(menu ->
                // menu 关闭时 arrow 的动画
                AnimationUtil.with().loadAnimation(this, arrow, R.anim.collapse_menu));

        popupMenu.show();
    }

    public void setFragment(int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        AlbumFragment nextFragment = index == SYSTEM_ALBUM ? systemAlbum : customerAtlas;
        if (!nextFragment.isAdded()) {
            transaction.hide(currentFragment)
                    .add(R.id.main_panel, nextFragment, index + "")
                    .show(nextFragment);
        } else {
            transaction.hide(currentFragment)
                    .show(nextFragment);
        }
        transaction.commit();
        currentFragment = nextFragment;
        refreshShowViewType();
    }

    private void refreshShowViewType() {
        if (currentFragment.getShowViewType() == App.BIG_ALBUM) {
            doneIv.setImageResource(R.drawable.ic_big_album_list);
        } else {
            doneIv.setImageResource(R.drawable.ic_roll_album_list);
        }
    }
}
