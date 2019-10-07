package luoluna.sweven.album.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sweven.base.BaseActivity;
import com.sweven.dialog.NoticeDialog;
import com.sweven.widget.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import luoluna.sweven.album.R;
import luoluna.sweven.album.adapter.PictureAdapter;
import luoluna.sweven.album.app.Helper;
import luoluna.sweven.album.bean.Album;
import luoluna.sweven.album.bean.Picture;

public class AlbumActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;
    private RelativeLayout back, done;
    private ImageView backIv, doneIv;

    private RefreshRecyclerView recyclerView;
    private PictureAdapter adapter;

    private int aid;
    private Album album;
    private String name;
    private List<String> desktops = new ArrayList<>();
    private List<Picture> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        getBundle();
        bindView();
        initData();
    }

    private void getBundle() {
        Intent intent = getIntent();
        aid = intent.getIntExtra("aid", 0);
        album = Album.config(this, aid);
        if (album == null) {
            NoticeDialog dialog = new NoticeDialog(this);
            dialog.setTitle("错误！请退出重试！")
                    .setCallBack(this::finish)
                    .show();
        } else {
            name = album.getName();
            desktops = album.getDesktops();
            if (desktops != null && desktops.size() > 0) {
                for (String image : desktops) {
                    list.add(new Picture(image));
                }
            }
        }

    }

    @Override
    protected void bindView() {
        title = bindID(R.id.title);
        back = bindID(R.id.back);
        backIv = bindID(R.id.back_image);
        done = bindID(R.id.done);
        doneIv = bindID(R.id.done_image);

        recyclerView = bindID(R.id.image_list);
    }

    @Override
    protected void initData() {
        title.setText(getString(R.string.imageTitle, name, desktops.size()));
        backIv.setVisibility(View.VISIBLE);
        doneIv.setVisibility(View.VISIBLE);
        doneIv.setImageResource(R.drawable.ic_settings);

        adapter = new PictureAdapter(this, list);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.defaultRecyclerView();

        back.setOnClickListener(this);
        done.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.done:
                Album album = Helper.with().getAlbumByAid(this, aid);
                startActivity(AlbumSettingActivity.class,
                        "name:" + name,
                        "path:" + album.getPath(),
                        "remark:" + album.getRemark());
                break;
        }
    }
}
