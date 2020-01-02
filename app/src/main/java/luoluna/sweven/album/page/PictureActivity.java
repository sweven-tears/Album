package luoluna.sweven.album.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.sweven.base.BaseActivity;
import com.sweven.dialog.NoticeDialog;
import com.sweven.interf.CallBack;

import java.util.ArrayList;
import java.util.List;

import luoluna.sweven.album.R;
import luoluna.sweven.album.adapter.PictureAdapter;
import luoluna.sweven.album.bean.Album;
import luoluna.sweven.album.bean.Picture;
import luoluna.sweven.album.widget.RecyclerViewItemDecoration;

import static luoluna.sweven.album.MainActivity.CUSTOMER_ATLAS;
import static luoluna.sweven.album.MainActivity.SYSTEM_ALBUM;

public class PictureActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;
    private RelativeLayout back, done;
    private ImageView backIv, doneIv;

    private RecyclerView recyclerView;

    private Album album;
    private List<Picture> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        bindView();
        getBundle(this::initData);
    }

    /**
     * 获取intent传递的参数
     *
     * @param callBack 完成bundle读取后的回调
     */
    private void getBundle(CallBack callBack) {
        Intent intent = getIntent();
        int type = intent.getIntExtra("type", -1);
        String name = intent.getStringExtra("name");

        // 判断类型
        if (type == SYSTEM_ALBUM) {//album
            String uri = intent.getStringExtra("uri");
            album = Album.find(this, uri);
        } else if (type == CUSTOMER_ATLAS) {//atlas
            int aid = intent.getIntExtra("aid", 0);
            album = Album.find(this, aid);
        } else {
            error();
            return;
        }

        if (album == null) {
            error();
        } else {
            album.setName(name);
            List<String> desktops = album.getDesktops();
            if (desktops != null && desktops.size() > 0) {
                for (String image : desktops) {
                    list.add(new Picture(image));
                }
            }
            // getBundle后的操作
            if (callBack != null) {
                callBack.call();
            }
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

        PictureAdapter adapter = new PictureAdapter(this, list,album.getDesktops());
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
