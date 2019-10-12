package luoluna.sweven.album.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sweven.base.BaseFragment;
import com.sweven.interf.CallBack;
import com.sweven.util.AnimationUtil;
import com.sweven.widget.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import luoluna.sweven.album.R;
import luoluna.sweven.album.adapter.AlbumAdapter;
import luoluna.sweven.album.app.App;
import luoluna.sweven.album.bean.Album;
import luoluna.sweven.album.manager.Setting;
import luoluna.sweven.album.util.ScanPhotoAsync;

public class AlbumFragment extends BaseFragment {
    public static int SYSTEM_ALBUM = 1;
    public static int CUSTOMER_ATLAS = 2;
    public static final String KEY = "type";

    private int type;

    private View view;

    private TextView tips;
    private RefreshRecyclerView recyclerView;
    private AlbumAdapter adapter;
    private GridLayoutManager layoutManager;
    private List<Album> list = new ArrayList<>();

    private String label;

    public static AlbumFragment newInstance(int type) {
        AlbumFragment fragment = new AlbumFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt(KEY);
            label = type == SYSTEM_ALBUM ? getString(R.string.no_album_tips) : getString(R.string.no_atlas_tips);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindView();
        initData();
    }

    @Override
    protected void bindView() {
        recyclerView = bindId(R.id.album_list);
        tips = bindId(R.id.tips);
    }

    @Override
    protected void initData() {
        adapter = new AlbumAdapter(activity);
        layoutManager = new GridLayoutManager(activity, App.album);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.defaultRecyclerView();
        recyclerView.setAdapter(adapter);

        setAdapter(null, true, null);
    }

    public void addAlbum() {
        adapter.addAlbum(this::addResponse);
    }

    /**
     * 添加图集后的响应
     */
    private void addResponse() {
        layoutManager.scrollToPositionWithOffset(0, 0);
    }


    /**
     * @param refreshIv         MainActivity中的refreshIv组件
     * @param refresh           是否刷新图集
     * @param refreshedListener 完成刷新后的回调
     */
    public void setAdapter(ImageView refreshIv, boolean refresh, CallBack refreshedListener) {
        if (refresh || App.isFirst) {
            if (refreshIv != null && refreshIv.getAnimation() == null) {// not null
                AnimationUtil.with().rotateConstantSpeed(activity, refreshIv);
            }
            if (App.isFirst) {// 第一次启动状态判断并修改
                Setting.getInstance().nonFirst(activity);
            }
            ScanPhotoAsync scanPhotoAsync = new ScanPhotoAsync(context);
            scanPhotoAsync.execute(type);
            scanPhotoAsync.setCallBack(list -> {
                if (refreshIv != null) {// not null
                    AnimationUtil.with().stopRotateConstantSpeed(refreshIv);
                }
                if (list.size() == 0) {// not null
                    // show tips
                    tips.setVisibility(View.VISIBLE);
                    tips.setText(label);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    tips.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                this.list = list;
                adapter.updateAll(this.list);
                //  refreshed callback
                if (refreshedListener != null) {
                    refreshedListener.call();
                }
            });
        } else {
            layoutManager.setSpanCount(App.album);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            // sync App.album setting
            setAdapter(null,false,null);
        }

    }
}
