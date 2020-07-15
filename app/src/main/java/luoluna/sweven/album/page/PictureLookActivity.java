package luoluna.sweven.album.page;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.sweven.base.BaseActivity;
import com.sweven.console.LogUtil;
import com.sweven.dialog.NoticeDialog;
import com.sweven.util.WindowUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import luoluna.sweven.album.R;
import luoluna.sweven.album.adapter.PictureLookAdapter;
import luoluna.sweven.album.app.Helper;
import luoluna.sweven.album.entity.local.Album;
import luoluna.sweven.album.entity.local.Image;

public class PictureLookActivity extends BaseActivity {

    private RecyclerView recyclerView;

    private int index;
    private List<Image> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_look);

        WindowUtil.fullScreen(this);
        getBundle();
        bindView();
        initData();
    }

    private void getBundle() {
        Intent intent = getIntent();
        index = intent.getIntExtra("present", 0);
        long aid = intent.getLongExtra("aid", 0);
        Album album = Helper.with().getAlbumByAid(aid);
        if (album == null) {
            NoticeDialog dialog = new NoticeDialog(this);
            dialog.setTitle("错误！请退出重试！")
                    .setEnterListener(this::finish)
                    .show();
            return;
        }
        list = album.getDesktops();
        if (list == null || list.size() == 0) {
            NoticeDialog dialog = new NoticeDialog(this);
            dialog.setTitle("错误！请退出重试！")
                    .setEnterListener(this::finish)
                    .show();
        }
    }

    @Override
    protected void bindView() {
        recyclerView = bindId(R.id.picture_look_list);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initData() {
        PictureLookAdapter adapter = new PictureLookAdapter(this, list);
        // 让 RecyclerView 像 PagerView 的翻页效果
        new PagerSnapHelper().attachToRecyclerView(recyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.HORIZONTAL);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        manager.scrollToPositionWithOffset(index, 0);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LogUtil.with().i(newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                Console.log(dx,dy);
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(com.sweven.util.R.anim.alpha_open, com.sweven.util.R.anim.alpha_close);
    }
}
