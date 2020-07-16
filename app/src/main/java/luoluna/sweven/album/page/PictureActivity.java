package luoluna.sweven.album.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sweven.base.BaseActivity;
import com.sweven.dialog.NoticeDialog;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import luoluna.sweven.album.R;
import luoluna.sweven.album.adapter.PictureAdapter;
import luoluna.sweven.album.entity.local.Album;
import luoluna.sweven.album.fragment.main.HomeFragment;
import luoluna.sweven.album.widget.RecyclerViewItemDecoration;

public class PictureActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;
    private RelativeLayout back, done;
    private ImageView backIv, doneIv;

    private RecyclerView recyclerView;

    private Album album;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        bindView();
        getBundle();
        initData();
    }

    /**
     * 获取intent传递的参数
     *
     */
    private void getBundle() {
        Intent intent = getIntent();
        long aid = intent.getLongExtra("aid", 0);
        for (Album a : HomeFragment.albums) {
            if (a.getId() == aid) {
                album = a;
                break;
            }
        }
        if (album == null) {
            error();
        }
    }

    @Override
    protected void bindView() {
        title = bindId(R.id.title);
        back = bindId(R.id.back);
        backIv = bindId(R.id.back_image);
        done = bindId(R.id.done);
        doneIv = bindId(R.id.done_image);


        recyclerView = bindId(R.id.image_list);
    }

    @Override
    protected void initData() {
        title.setText(getString(R.string.imageTitle, album.getName(), album.getDesktops().size()));
        backIv.setVisibility(View.VISIBLE);
        doneIv.setVisibility(View.VISIBLE);
        doneIv.setImageResource(R.drawable.ic_album_info);

        PictureAdapter adapter = new PictureAdapter(this, album);
        StaggeredGridLayoutManager manager =
                new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration());
        recyclerView.setAdapter(adapter);

        back.setOnClickListener(this);
        done.setOnClickListener(this);
    }

    /**
     * 打开activity有错误的弹窗
     */
    private void error() {
        NoticeDialog dialog = new NoticeDialog(this);
        dialog.setTitle("错误！请退出重试！")
                .setEnterListener(this::finish)
                .show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.done:
                startActivity(AlbumSettingActivity.class,
                        "name:" + album.getName(),
                        "path:" + album.getPath(),
                        "remark:" + album.getRemark());
                break;
        }
    }
}
