package com.sweven.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sweven.adapter.FolderChooserAdapter;
import com.sweven.bean.Folder;
import com.sweven.interf.OnClickItemListener;
import com.sweven.util.FileUtil;
import com.sweven.util.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件夹选择器
 */
public class FolderChooser extends Dialog {

    private Activity activity;

    private static String ROOT_DIR = FileUtil.getSDCard().getAbsolutePath();

    private TextView title;

    private TextView cancel, confirm;
    private RecyclerView recyclerView;
    private FolderChooserAdapter adapter;

    private String presentDir = ROOT_DIR;
    private List<Folder> presentDirs;

    private OnClickItemListener<String> onClickItemListener;

    public FolderChooser(@NonNull Activity context, String rootDir, String presentDir) {
        this(context);
        FolderChooser.ROOT_DIR = rootDir;
//        判断是否有当前目录
        if (presentDir == null || presentDir.equals("")) {
//            设置当前目录即为根目录
            this.presentDir = rootDir;
        } else {
            this.presentDir = presentDir;
        }
    }

    public FolderChooser(@NonNull Activity context, String presentDir) {
        this(context);
        this.presentDir = presentDir;
    }

    public FolderChooser(@NonNull Activity context) {
        this(context, R.style.Theme_AppCompat);
        this.activity = context;
    }

    private FolderChooser(@NonNull Activity context, int themeResId) {
        super(context, themeResId);
        this.activity = context;
    }

    protected FolderChooser(@NonNull Activity context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.activity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_folder_chooser);

        windowDeploy(0, 0);
        setCanceledOnTouchOutside(false);

        bindView();
        initData();
    }

    private void bindView() {
        title = findViewById(R.id.title);
        cancel = findViewById(R.id.back_text);
        confirm = findViewById(R.id.done_text);
        recyclerView = findViewById(R.id.folder_list);
    }

    private void initData() {
        title.setText("选择文件夹");
        cancel.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.VISIBLE);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);

        presentDirs = getDirs(presentDir);
        adapter = new FolderChooserAdapter(activity, presentDirs);
        recyclerView.setAdapter(adapter);

        cancel.setOnClickListener(v -> dismiss());
        confirm.setOnClickListener(v -> {
            if (onClickItemListener != null) {
                onClickItemListener.onClick(presentDir);
            }
            dismiss();
        });
        adapter.setOnClickItemListener(dir -> showDirs(((Folder) dir).getPath()));
    }

    /**
     * 更新adapter，渲染新的目录
     *
     * @param dir 目录
     */
    private void showDirs(String dir) {
        this.presentDir = dir;
        presentDirs = getDirs(dir);
        adapter.replace(presentDirs);
    }

    /**
     * @param dir 目录
     * @return 当前目录下的文件夹
     */
    private List<Folder> getDirs(String dir) {

        List<Folder> list = new ArrayList<>();

        File file = new File(dir);
//        是否为根目录
        if (!dir.equals(ROOT_DIR)) {
            list.add(new Folder("返回上一级", file.getParentFile().getAbsolutePath()));
        }

//        遍历目录下的所有的文件夹
        for (File f : file.listFiles()) {
//            判断是否是文件夹且不是隐藏文件
            if (f.isDirectory() && !f.isHidden()) {
                Folder folder = new Folder(f.getName(), f.getAbsolutePath());
                list.add(folder);
            }
        }
        return list;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void cancel() {
        if (presentDir.equals(ROOT_DIR)) {
//            当前目录为根目录时直接取消
            super.cancel();
        } else {
//            当前目录非根目录时返回上一个目录
            showDirs(presentDirs.get(0).getPath());
        }
    }

    public void setOnConfirm(OnClickItemListener<String> onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    //设置窗口显示
    public void windowDeploy(int x, int y) {
        Window window = getWindow(); //得到对话框
        window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
        window.setBackgroundDrawableResource(R.color.transparent); //设置对话框背景为透明
        WindowManager.LayoutParams wl = window.getAttributes();
        //根据x，y坐标设置窗口需要显示的位置
        wl.x = x; //x小于0左移，大于0右移
        wl.y = y; //y小于0上移，大于0下移
//      wl.alpha = 0.6f; //设置透明度
//      wl.gravity = Gravity.BOTTOM; //设置重力
        window.setAttributes(wl);
    }
}
