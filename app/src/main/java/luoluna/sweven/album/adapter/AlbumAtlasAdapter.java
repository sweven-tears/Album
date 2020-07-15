package luoluna.sweven.album.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sweven.base.BaseRecyclerAdapter;
import com.sweven.dialog.FolderChooser;
import com.sweven.dialog.InputDialog;
import com.sweven.interf.CallBack;
import com.sweven.interf.CallbackForParameter;
import com.sweven.util.ViewUtil;

import java.util.List;

import androidx.annotation.NonNull;
import luoluna.sweven.album.R;
import luoluna.sweven.album.app.App;
import luoluna.sweven.album.app.Helper;
import luoluna.sweven.album.entity.local.Album;
import luoluna.sweven.album.interf.OnSelectedChangeListener;
import luoluna.sweven.album.page.PictureActivity;
import luoluna.sweven.album.util.Verifier;

/**
 * Created by Sweven on 2019/9/10--17:01.
 * Email: sweventears@foxmail.com
 */
public class AlbumAtlasAdapter extends BaseRecyclerAdapter<Album> {
    private int type;

    private boolean edit;

    private OnSelectedChangeListener onSelectedChangeListener;

    public AlbumAtlasAdapter(Activity activity, int type) {
        super(activity);
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (App.album == App.BIG_ALBUM) {
            view = inflater.inflate(R.layout.item_list_album_big, parent, false);
            return new BigAlbumHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_list_album_roll, parent, false);
            return new RollAlbumHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        AlbumHolder holder = (AlbumHolder) viewHolder;
        Album album = list.get(position);
        // 使用Glide加载避免album.getCover()为空
        Glide.with(activity)
                .load(album.getCover())
                .into(holder.cover);
        holder.name.setText(album.getName());
        holder.count.setText(album.getCount() + "张");
        if (!edit) {
            holder.editPanel.setVisibility(View.GONE);
        } else {
            holder.editPanel.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(album.isSelected());
        }
    }

    @Override
    public void insert(Album album) {
        long result = Helper.with().addAlbum(album);
        if (result > 0) {
            super.insert(0, album);
        } else {
            toast.showShort("创建失败");
        }
    }

    /**
     * 添加相册
     *
     * @param callBack 完成添加步骤的回调
     */
    public void addAlbum(CallBack callBack) {
        InputDialog dialog = new InputDialog(activity, com.sweven.util.R.style.NormalDialogStyle);
        dialog.show();
        dialog.setLabel("图集名")
                .setHint("请输入")
                .setConfirm("下一步")
                .setOnConfirmListener(input -> {
                    if (input.trim().isEmpty()) {
                        toast.showShort("输入不能为空");
                    } else {
                        if (input.trim().length() > App.ALBUM_NAME_LENGTH) {
                            toast.showShort("字符长度不能大于" + App.ALBUM_NAME_LENGTH + "个！");
                            return;
                        } else {
                            String name = input.trim();
                            if (Verifier.contains(list, name, "name")) {
                                toast.showShort("图集名已被占用");
                                return;
                            }
                        }
                        Album album = new Album(Helper.with().getNextAlbumId(), input);
                        nextStep(album, callBack);
                        dialog.cancel();
                    }
                });
    }

    /**
     * 取名后的下一步：选择文件夹
     *
     * @param callBack 确定后的回调参数
     */
    private void nextStep(Album album, CallBack callBack) {
        FolderChooser chooser = new FolderChooser(activity);
        chooser.setOnConfirm(path -> {
            insert(Album.create(album, path));
            if (callBack != null) {
                callBack.call();
            }
        });
        chooser.show();
    }

    /**
     * 更新列表
     */
    public void updateAll(List<Album> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    /**
     * @return 获取选中数量
     */
    public int getSelectedCount() {
        int count = 0;
        for (Album album : list) {
            if (album.isSelected()) {
                count++;
            }
        }
        return count;
    }

    /**
     * 设置编辑状态，true 为编辑状态
     *
     * @param state 编辑状态
     */
    public void editState(boolean state) {
        this.edit = state;
        if (!state) {// 判断对否关闭编辑状态
            selectAll(false);
            return;
        }
        notifyDataSetChanged();
    }

    /**
     * true：选择全部 false：全部取消选择
     *
     * @param all 是否选择全部
     */
    public void selectAll(boolean all) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSelected(all);
        }
        notifyDataSetChanged();
    }

    /**
     * 删除所选
     */
    public void delete(CallbackForParameter<Integer> onDeleteListener) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).isSelected()) {
                if (Helper.with().delAlbum(list.get(i))) {
                    list.remove(i);
                } else
                    toast.showShort("删除失败，请重试！");
            }
        }
        notifyDataSetChanged();
        if (onDeleteListener != null) {
            onDeleteListener.call(getItemCount());
        }
    }

    public boolean rename(String newName) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).isSelected()) {
                if (list.get(i).getName().equals(newName)) {
                    toast.showShort("未修改");
                    return true;
                }
                list.get(i).setName(newName);
                if (Helper.with().updateAlbum(list.get(i)) > 0) {
                    notifyDataSetChanged();
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public Album getCurrentSelected() {
        for (Album album : list) {
            if (album.isSelected()) {
                return album;
            }
        }
        return null;
    }

    public void setOnDeleteListener(CallBack onDeleteListener) {
    }

    public void setOnSelectedChangeListener(OnSelectedChangeListener onSelectedChangeListener) {
        this.onSelectedChangeListener = onSelectedChangeListener;
    }

    class AlbumHolder extends ViewHolder {
        RelativeLayout item;
        ImageView cover;
        TextView name;
        TextView count;
        RelativeLayout editPanel;
        CheckBox checkBox;

        AlbumHolder(@NonNull View view) {
            super(view);
        }


        void openAlbum() {
            Album album = list.get(getAdapterPosition());
            Intent intent = new Intent(activity, PictureActivity.class);
            intent.putExtra("aid", album.getId());
            activity.startActivity(intent);
        }

        void onLongClick(int position) {
            if (onClickItemListener != null) {
                onClickItemListener.onClick(list.get(position));
            }
        }

        void checkedChangeListener(CompoundButton button, boolean b) {
            list.get(getAdapterPosition()).setSelected(b);
            if (onSelectedChangeListener != null) {
                onSelectedChangeListener.onChange(getItemCount(), getSelectedCount());
            }
        }

        public void onClickEditPanel(View view) {
            checkBox.setChecked(!checkBox.isChecked());
        }
    }


    public class RollAlbumHolder extends AlbumHolder {

        RollAlbumHolder(@NonNull View view) {
            super(view);
            item = view.findViewById(R.id.item);
            cover = view.findViewById(R.id.cover);
            name = view.findViewById(R.id.name);
            count = view.findViewById(R.id.picture_count);
            editPanel = view.findViewById(R.id.edit_panel);
            checkBox = view.findViewById(R.id.check_box);

            item.setOnClickListener(v -> openAlbum());
            editPanel.setOnClickListener(this::onClickEditPanel);
            checkBox.setOnCheckedChangeListener(this::checkedChangeListener);
        }

    }

    public class BigAlbumHolder extends AlbumHolder {

        BigAlbumHolder(@NonNull View view) {
            super(view);
            item = view.findViewById(R.id.item);
            cover = view.findViewById(R.id.cover);
            name = view.findViewById(R.id.name);
            count = view.findViewById(R.id.picture_count);
            editPanel = view.findViewById(R.id.edit_panel);
            checkBox = view.findViewById(R.id.check_box);

            ViewUtil.setWidthHeight(item, 0.5, 0.8);

            item.setOnClickListener(v -> openAlbum());
            editPanel.setOnClickListener(this::onClickEditPanel);
            checkBox.setOnCheckedChangeListener(this::checkedChangeListener);
        }

    }
}
