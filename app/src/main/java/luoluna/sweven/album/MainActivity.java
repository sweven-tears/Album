package luoluna.sweven.album;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.sweven.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import luoluna.sweven.album.adapter.AlbumAdapter;
import luoluna.sweven.album.app.App;
import luoluna.sweven.album.bean.Album;

public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;
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
        recyclerView = bindID(R.id.album_list);
    }

    @Override
    protected void initData() {
        list = App.queryByAlbumList(this);
        adapter = new AlbumAdapter(this, list);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

}
