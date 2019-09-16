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

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;
    private ImageView doneIv;
    private RelativeLayout doneRl;

    private RefreshRecyclerView recyclerView;
    private AlbumAdapter adapter;
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
        doneRl = bindID(R.id.done);
        doneIv = bindID(R.id.done_image);
        recyclerView = bindID(R.id.album_list);
    }

    @Override
    protected void initData() {
        title.setText(R.string.index_title);
        doneIv.setVisibility(View.VISIBLE);
        doneIv.setImageResource(App.album == App.BIG_ALBUM ? R.drawable.ic_big_album_list : R.drawable.ic_roll_album_list);
        doneRl.setOnClickListener(this);

        recyclerView.defaultRecyclerView();
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        manager.setOrientation(RecyclerView.VERTICAL);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(manager);
        setAdapter();
    }

    private void setAdapter() {
        list = App.queryByAlbumList(this);
        adapter = new AlbumAdapter(this, list);
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
            setAdapter();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            setAdapter();
        } catch (Exception e) {
            e.printStackTrace();
            log.e("为实例化无法加载");
        }
    }

    @Override
    protected boolean onBack() {
        moveTaskToBack(true);
        return true;
    }
}
