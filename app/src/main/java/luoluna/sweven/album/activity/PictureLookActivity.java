package luoluna.sweven.album.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.sweven.base.BaseActivity;
import com.sweven.dialog.NoticeDialog;
import com.sweven.util.WindowUtil;

import java.util.ArrayList;
import java.util.List;

import luoluna.sweven.album.R;
import luoluna.sweven.album.adapter.PictureLookAdapter;
import luoluna.sweven.album.bean.Album;
import luoluna.sweven.album.bean.Picture;

public class PictureLookActivity extends BaseActivity {

    private ImageView picture;
    private RecyclerView recyclerView;

    private Uri uri;
    private int index;
    private List<Picture> list = new ArrayList<>();

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
        uri = Uri.parse(intent.getStringExtra("picture_uri"));
        index = intent.getIntExtra("present", 0);
        int aid = intent.getIntExtra("aid", 0);
        if (aid == 0) {
            NoticeDialog dialog = new NoticeDialog(this);
            dialog.setTitle("错误！请退出重试！")
                    .setCallBack(this::finish)
                    .show();
        } else {
            Album album = Album.find(this, aid);
            for (String desktop : album.getDesktops()) {
                list.add(new Picture(desktop));
            }
        }
    }

    @Override
    protected void bindView() {
        picture = bindID(R.id.picture);
        recyclerView = bindID(R.id.picture_look_list);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initData() {
        picture.setImageURI(uri);


        PictureLookAdapter adapter = new PictureLookAdapter(this, list);
        // 让 RecyclerView 像 PagerView 的翻页效果
        new PagerSnapHelper().attachToRecyclerView(recyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.HORIZONTAL);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        manager.scrollToPositionWithOffset(index, 0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(com.sweven.util.R.anim.alpha_open, com.sweven.util.R.anim.alpha_close);
    }
}
