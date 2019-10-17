package luoluna.sweven.album.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sweven.base.BaseFragment;
import com.sweven.dialog.WaitDialog;
import com.sweven.interf.CallBack;

import luoluna.sweven.album.R;
import luoluna.sweven.album.adapter.AlbumAdapter;
import luoluna.sweven.album.app.App;
import luoluna.sweven.album.manager.Setting;
import luoluna.sweven.album.util.ScanPhotoAsync;

import static luoluna.sweven.album.MainActivity.SYSTEM_ALBUM;

public class AlbumAtlasFragment extends BaseFragment {

    private View view;

    private static final String KEY = "type";
    private int type;

    private TextView tipsTv;
    private RecyclerView recyclerView;
    private AlbumAdapter adapter;

    private String tips;
    private GridLayoutManager manager;

    public static AlbumAtlasFragment newInstance(int type) {
        AlbumAtlasFragment fragment = new AlbumAtlasFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_album, null);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt(KEY);
        }
        bindView();
        initData();
    }

    @Override
    protected void bindView() {
        recyclerView = bindId(view, R.id.album_list);
        tipsTv = bindId(view, R.id.tips);
    }

    @Override
    protected void initData() {
        tips = type == SYSTEM_ALBUM ?
                getString(R.string.no_album_tips) :
                getString(R.string.no_atlas_tips);

        manager = new GridLayoutManager(context, App.album);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

        adapter = new AlbumAdapter(activity, type);
        recyclerView.setAdapter(adapter);

        setAdapter(null);
    }

    public void addAtlas() {
        adapter.addAlbum(this::toAddPosition);
    }

    private void toAddPosition() {
        tipsTv.setVisibility(View.VISIBLE);
        manager.scrollToPositionWithOffset(0, 0);
    }

    public void setAdapter(CallBack refreshed) {
        WaitDialog dialog = new WaitDialog(activity);
        dialog.show();
        ScanPhotoAsync scanPhotoAsync = new ScanPhotoAsync(context);
        scanPhotoAsync.execute(type);
        scanPhotoAsync.setCallBack(list -> {
            if (list.size() > 0) {
                tipsTv.setVisibility(View.GONE);
                adapter.updateAll(list);
            } else {
                tipsTv.setVisibility(View.VISIBLE);
                tipsTv.setText(tips);
            }
            dialog.cancel();
            // 添加刷新完成监听
            if (refreshed != null) {
                refreshed.call();
            }
        });
    }

    public void cutShowView() {
        manager.setSpanCount(App.album);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            cutShowView();
        }
    }
}
