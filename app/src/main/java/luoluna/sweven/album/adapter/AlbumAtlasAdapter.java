package luoluna.sweven.album.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sweven.base.BaseRecyclerAdapter;
import com.sweven.dialog.FolderChooser;
import com.sweven.dialog.InputDialog;
import com.sweven.interf.CallBack;
import com.sweven.util.ViewUtil;

import java.util.List;

import luoluna.sweven.album.R;
import luoluna.sweven.album.activity.PictureActivity;
import luoluna.sweven.album.app.App;
import luoluna.sweven.album.app.Helper;
import luoluna.sweven.album.bean.Album;
import luoluna.sweven.album.interf.OnSelectedChangeListener;
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
        holder.cover.setImageURI(Uri.parse(album.getCover()));
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
        long result = Helper.with().addAlbum(activity, album);
        if (result > 0) {
            super.insert(0, album);
        } else {
            toast.showShort("创建失败");
        }
    }

    public void addAlbum(CallBack callBack) {
        InputDialog dialog = new InputDialog(activity);
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
                            if (Verifier.contains(list, name, "getName")) {
                                toast.showShort("图集名已被占用");
                                return;
                            }
                        }
                        Album album = new Album(Helper.with().getNextAlbumId(activity), input);
                        nextStep(album, callBack);
                        dialog.cancel();
                    }
                });
    }

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

    public void updateAll(List<Album> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private int getSelectedCount() {
        int count = 0;
        for (Album album : list) {
            if (album.isSelected()) {
                count++;
            }
        }
        return count;
    }

    public void editState(boolean state) {
        this.edit = state;
        if (!state) {
            unSelectAll();
        }
        notifyDataSetChanged();
    }

    public void selectAll() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSelected(true);
        }
        notifyDataSetChanged();
    }

    private void unSelectAll() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSelected(false);
        }
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
            intent.putExtra("type", type);
            intent.putExtra("name", album.getName());
            intent.putExtra("aid", album.getId());
            intent.putExtra("uri", album.getPath());
            List<String> desktops = album.getDesktops();
            String[] images = new String[desktops == null ? 0 : desktops.size()];
            if (images.length > 0) {
                for (int i = desktops.size() - 1; i >= 0; i--) {
                    images[i] = desktops.get(i);
                }
            }
            intent.putExtra("images", images);
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
