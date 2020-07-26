package luoluna.sweven.album.fragment.main;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sweven.base.BaseFragment;
import com.sweven.dialog.InputDialog;
import com.sweven.dialog.WaitDialog;
import com.sweven.interf.CallBack;

import java.util.ArrayList;
import java.util.List;

import luoluna.sweven.album.R;
import luoluna.sweven.album.adapter.AlbumAtlasAdapter;
import luoluna.sweven.album.app.App;
import luoluna.sweven.album.entity.local.Album;
import luoluna.sweven.album.entity.local.Image;
import luoluna.sweven.album.interf.AtlasOperate;
import luoluna.sweven.album.interf.OnSelectedChangeListener;
import luoluna.sweven.album.util.ScanPhotoAsync;

import static luoluna.sweven.album.MainActivity.CUSTOMER_ATLAS;
import static luoluna.sweven.album.MainActivity.SYSTEM_ALBUM;

public class HomeFragment extends BaseFragment implements AtlasOperate {

    private View view;

    private static final String KEY = "type";
    private int type;

    private TextView tipsTv;
    private GridLayoutManager manager;
    private RecyclerView recyclerView;
    private AlbumAtlasAdapter adapter;

    private TextView[] editors = new TextView[4];
    private static final int SHARE = 0, MERGE = 1, DELETE = 2, RENAME = 3;

    private static boolean showViewChange = false;
    private String tips;

    public static List<Album> albums;

    /**
     * @param type 显示类型：SYSTEM_ALBUM/CUSTOMER_ATLAS
     * @return 新的fragment
     */
    public static HomeFragment newInstance(int type) {
        HomeFragment fragment = new HomeFragment();
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
        editors[SHARE] = bindId(view, R.id.share_item);
        editors[MERGE] = bindId(view, R.id.merge_item);
        editors[DELETE] = bindId(view, R.id.delete_item);
        editors[RENAME] = bindId(view, R.id.rename_item);
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

        editors[SHARE].setOnClickListener(this::share);
        editors[MERGE].setOnClickListener(this::merge);
        editors[DELETE].setOnClickListener(this::delete);
        editors[RENAME].setOnClickListener(this::rename);

        setAdapter(null);
    }

    /**
     * 选中/取消选中的监听变化
     */
    private void selectedChange(int total, int count) {
        if (count == 0) {
            for (TextView editor : editors) {
                setTextColor(editor, R.color.gray_cc);
            }
        } else if (count > 0) {
            for (TextView editor : editors) {
                setTextColor(editor, R.color.black);
            }
            if (count == 1) {
                setTextColor(editors[MERGE], R.color.gray_cc);
            } else {
                setTextColor(editors[RENAME], R.color.gray_cc);
            }
        }
    }

    /**
     * 给TextView的字体以及图标设置颜色
     *
     * @param textView 需要设置的textView
     * @param color    资源文件的 颜色
     */
    private void setTextColor(TextView textView, int color) {
        textView.setTextColor(ContextCompat.getColor(context, color));
        Drawable drawable = textView.getCompoundDrawables()[1];
        drawable.setTint(ContextCompat.getColor(context, color));
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
                HomeFragment.albums = list;
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
    private void cutShowView() {
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
        bindId(view, R.id.line).setVisibility(View.VISIBLE);
        bindId(view, R.id.panel_2).setVisibility(View.VISIBLE);
    }

    /**
     * 添加图集
     */
    @Override
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
     * 选中所有
     */
    @Override
    public void selectAll() {
        adapter.selectAll(true);
    }

    /**
     * 全部不选
     */
    @Override
    public void selectNone() {
        adapter.selectAll(false);
    }

    /**
     * @return 获取选中数量
     */
    @Override
    public int getSelectedCount() {
        return adapter.getSelectedCount();
    }

    /**
     * 删除
     *
     * @param view
     */
    @Override
    public void delete(View view) {
        adapter.delete(count -> {
            if (count == 0) {
                tipsTv.setVisibility(View.VISIBLE);
                tipsTv.setText(tips);
            }
        });
    }

    /**
     * 分享
     *
     * @param view
     */
    @Override
    public void share(View view) {
        List<Album> list = adapter.getList();
        List<Image> share = new ArrayList<>();
        for (Album album : list) {
            if (album.isSelected() && album.getDesktops() != null) {
                share.addAll(album.getDesktops());
            }
        }
        //TODO share to other app
    }

    /**
     * 修改相册名
     *
     * @param view
     */
    @Override
    public void rename(View view) {
        // 有且只有一个选中才能修改名称
        if (getSelectedCount() != 1) return;
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
     * 退出编辑状态
     */
    @Override
    public void closeEdit() {
        adapter.editState(false);
        bindId(view, R.id.line).setVisibility(View.GONE);
        bindId(view, R.id.panel_2).setVisibility(View.GONE);
    }

    /**
     * 至少选择判断
     */
    @Override
    public boolean less() {
        int count = adapter.getSelectedCount();
        return count < 1;
    }

    /**
     * 图集合并
     *
     * @param view
     */
    @Override
    public void merge(View view) {

    }

    /**
     * 添加 list 的 item 选则/取消选择 的监听
     *
     * @param onSelectedChangeListener 选择变化监听
     */
    public void addListener(OnSelectedChangeListener onSelectedChangeListener) {
        adapter.setOnSelectedChangeListener((i, i1) -> {
            selectedChange(i, i1);
            onSelectedChangeListener.onChange(i, i1);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        setAdapter(null);
    }
}
