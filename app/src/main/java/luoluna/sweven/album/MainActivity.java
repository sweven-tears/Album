package luoluna.sweven.album;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sweven.base.BaseActivity;
import com.sweven.util.AnimationUtil;
import com.sweven.widget.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import luoluna.sweven.album.adapter.AlbumAdapter;
import luoluna.sweven.album.app.App;
import luoluna.sweven.album.app.Helper;
import luoluna.sweven.album.bean.Album;
import luoluna.sweven.album.manager.Setting;
import luoluna.sweven.album.util.ScanPhotoAsync;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static int cutIv = App.album == App.BIG_ALBUM ? R.drawable.ic_big_album_list : R.drawable.ic_roll_album_list;

    private TextView title;
    private ImageView doneIv, addIv, refreshIv;

    private RefreshRecyclerView recyclerView;
    private AlbumAdapter adapter;
    private GridLayoutManager layoutManager;
    private List<Album> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        initData();
    }

    @Override
    protected void bindView() {
        title = bindID(R.id.title);
        refreshIv = bindID(R.id.refresh_image);
        doneIv = bindID(R.id.done_image);
        addIv = bindID(R.id.add_image);
        recyclerView = bindID(R.id.album_list);
    }

    @Override
    protected void initData() {
        title.setText(R.string.index_title);
        doneIv.setVisibility(View.VISIBLE);
        doneIv.setImageResource(cutIv);
        refreshIv.setOnClickListener(this);
        doneIv.setOnClickListener(this);
        addIv.setOnClickListener(this);

        layoutManager = new GridLayoutManager(this, App.album);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        adapter = new AlbumAdapter(this, list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.defaultRecyclerView();
        recyclerView.setAdapter(adapter);
        setAdapter(true);
    }

    /**
     * @param refresh 是否刷新图集
     */
    private void setAdapter(boolean refresh) {
        if (refresh) {
            ScanPhotoAsync scanPhotoAsync = new ScanPhotoAsync(this, list.size());
            scanPhotoAsync.execute();
            scanPhotoAsync.setCallBack(() -> {
                AnimationUtil.with().stopRotateSameSpeed(refreshIv);
                updateAlbums = false;
                list = Helper.with().queryByAlbumList(this);
                adapter.updateAll(list);
            });
        } else {
            layoutManager.setSpanCount(App.album);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.done_image) {
            if (App.album == App.BIG_ALBUM) {
                doneIv.setImageResource(R.drawable.ic_roll_album_list);
                App.album = App.ROLL_ALBUM;
            } else {
                doneIv.setImageResource(R.drawable.ic_big_album_list);
                App.album = App.BIG_ALBUM;
            }
            Setting.getInstance().save(this);
            setAdapter(false);
        } else if (view.getId() == R.id.add_image) {
            adapter.addAlbum();
        } else if (view.getId() == R.id.refresh_image) {
            if (updateAlbums = !updateAlbums) {
                AnimationUtil.with().rotateSameSpeed(this, refreshIv);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        setAdapter(true);
                    }
                }, 500);
            }
        }
    }

    private boolean updateAlbums = false;
}
