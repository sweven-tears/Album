package luoluna.sweven.album.fragment.main;

import android.annotation.SuppressLint;
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
import com.sweven.dialog.InputDialog;
import com.sweven.dialog.WaitDialog;
import com.sweven.interf.CallBack;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import luoluna.sweven.album.R;
import luoluna.sweven.album.adapter.AlbumAtlasAdapter;
import luoluna.sweven.album.app.App;
import luoluna.sweven.album.bean.Album;
import luoluna.sweven.album.interf.OnSelectedChangeListener;
import luoluna.sweven.album.util.ScanPhotoAsync;

import static luoluna.sweven.album.MainActivity.CUSTOMER_ATLAS;
import static luoluna.sweven.album.MainActivity.SYSTEM_ALBUM;

public class AlbumAtlasFragment extends BaseFragment {

    private View view;

    private static final String KEY = "type";
    private int type;

    private TextView tipsTv;
    private GridLayoutManager manager;
    private RecyclerView recyclerView;
    private AlbumAtlasAdapter adapter;

    public static boolean showViewChange = false;
    private String tips;

    /**
     * @param type 显示类型：SYSTEM_ALBUM/CUSTOMER_ATLAS
     * @return 新的fragment
     */
    public static AlbumAtlasFragment newInstance(int type) {
        AlbumAtlasFragment fragment = new AlbumAtlasFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressLint("InflateParams")
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

        adapter = new AlbumAtlasAdapter(activity, type);
        recyclerView.setAdapter(adapter);

        setAdapter(null);
    }

    /**
     * <p>添加图集</p>
     * 只有在 type==CUSTOMER_ATLAS 时才能调用，否则无效
     */
    public void addAtlas() {
        if (type == CUSTOMER_ATLAS) {
            adapter.addAlbum(this::toAddPosition);
        }
    }

    /**
     * 添加图集后的回调
     */
    private void toAddPosition() {
        tipsTv.setVisibility(View.GONE);
        manager.scrollToPositionWithOffset(0, 0);
    }

    /**
     * 加载并设置相册/图集的值
     *
     * @param refreshed 刷新后的回调
     */
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

    /**
     * 切换视图
     */
    public void cutShowView() {
        manager.setSpanCount(App.album);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && showViewChange) {// 刷新视图
            cutShowView();
            showViewChange = false;
        }
    }

    /**
     * 开启编辑模式
     */
    public void edit() {
        adapter.editState(true);
    }

    /**
     * 选中全部
     */
    public void selectAll() {
        adapter.selectAll(true);
    }

    /**
     * 全部取消选中
     */
    public void selectNone() {
        adapter.selectAll(false);
    }

    /**
     * @return item选中数量
     */
    public int getSelectedCount() {
        return adapter.getSelectedCount();

    }

    /**
     * 删除所选item
     */
    public void delete() {
        // 判断是否符合删除的前置条件
        if (!less()) {
            return;
        }

        adapter.delete(count -> {
            if (count == 0) {
                tipsTv.setVisibility(View.VISIBLE);
                tipsTv.setText(tips);
            }
        });
    }

    /**
     * 分享所选图集中的所有图片
     */
    public void share() {
        // 判断是否符合分享的前置条件
        if (!less()) {
            return;
        }

        List<Album> list = adapter.getList();
        List<String> share = new ArrayList<>();
        for (Album album : list) {
            if (album.isSelected() && album.getDesktops() != null) {
                share.addAll(album.getDesktops());
            }
        }
        //TODO share to other app
    }

    public void rename() {
        // 判断是否符合改名的前置条件
        if (!less()) {
            return;
        }
        if (adapter.getSelectedCount() > 1) {
            toast.showShort("只能选择一个");
            return;
        }

        InputDialog inputDialog = new InputDialog(context);
        inputDialog.show();
        inputDialog.setLabel("重命名");
        try {
            inputDialog.setHint(adapter.getCurrentSelected().getName());
        } catch (Exception e) {
            e.printStackTrace();
            inputDialog.setHint("请输入");
        }
        inputDialog.setOnConfirmListener(input -> {
            if (adapter.rename(input)) {
                inputDialog.cancel();
            } else {
                inputDialog.cancel();
                toast.showShort("修改失败");
            }
        });
    }

    /**
     * 关闭编辑状态
     */
    public void closeEdit() {
        adapter.editState(false);
    }

    /**
     * 至少选中item判断
     */
    private boolean less() {
        int count = adapter.getSelectedCount();
        if (count < 1) {
            toast.showShort("至少选择一个");
            return false;
        }
        return true;
    }

    /**
     * 添加 list 的 item 选则/取消选择 的监听
     *
     * @param onSelectedChangeListener 选择变化监听
     */
    public void addListener(OnSelectedChangeListener onSelectedChangeListener) {
        adapter.setOnSelectedChangeListener(onSelectedChangeListener);
    }
}
