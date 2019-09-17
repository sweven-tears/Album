package luoluna.sweven.album;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sweven.base.BaseActivity;
import com.sweven.widget.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import luoluna.sweven.album.adapter.AlbumAdapter;
import luoluna.sweven.album.app.App;
import luoluna.sweven.album.bean.Album;
import luoluna.sweven.album.manager.Setting;
import luoluna.sweven.album.wigdet.RecyclerViewItemDecoration;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static int cutIv = App.album == App.BIG_ALBUM ? R.drawable.ic_big_album_list : R.drawable.ic_roll_album_list;

    private TextView title;
    private ImageView doneIv;
    private RelativeLayout doneRl;

    private RefreshRecyclerView recyclerView;
    private AlbumAdapter adapter;
    private RecyclerViewItemDecoration itemDecoration;
    private GridLayoutManager layoutManager;
    private List<Album> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        bindView();
        initData();
    }

    @Override
    protected void bindView() {
        title = bindID(R.id.title);
        doneRl = bindID(R.id.done);
        doneIv = bindID(R.id.done_image);
        recyclerView = bindID(R.id.album_list);
    }

    @Override
    protected void initData() {
        title.setText(R.string.index_title);
        doneIv.setVisibility(View.VISIBLE);
        doneIv.setImageResource(cutIv);
        doneRl.setOnClickListener(this);

        itemDecoration = new RecyclerViewItemDecoration(10, 2);
        layoutManager = new GridLayoutManager(this, App.album);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.defaultRecyclerView();
        setAdapter(false);
    }

    private void setAdapter(boolean cut) {
        list = App.queryByAlbumList(this);
        adapter = new AlbumAdapter(this, list);
        if (App.album == App.BIG_ALBUM) {
            recyclerView.addItemDecoration(itemDecoration);
        } else {
            recyclerView.removeItemDecoration(itemDecoration);
        }
        layoutManager.setSpanCount(App.album);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.done) {
            if (App.album == App.BIG_ALBUM) {
                doneIv.setImageResource(R.drawable.ic_roll_album_list);
                App.album = App.ROLL_ALBUM;
            } else {
                doneIv.setImageResource(R.drawable.ic_big_album_list);
                App.album = App.BIG_ALBUM;
            }
            Setting.getInstance().save(this);
            setAdapter(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
//            setAdapter();
        } catch (Exception e) {
            e.printStackTrace();
            log.e("为实例化无法加载");
        }
    }
}
