package luoluna.sweven.album.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.sweven.base.BaseRecyclerAdapter;
import com.sweven.dialog.InputDialog;
import com.sweven.util.ViewUtil;
import com.sweven.util.WindowUtil;

import java.util.List;

import luoluna.sweven.album.R;
import luoluna.sweven.album.activity.AlbumActivity;
import luoluna.sweven.album.app.App;
import luoluna.sweven.album.bean.Album;
import luoluna.sweven.album.util.Verifier;

/**
 * Created by Sweven on 2019/9/10--17:01.
 * Email: sweventears@foxmail.com
 */
public class AlbumAdapter extends BaseRecyclerAdapter<Album> {
    public AlbumAdapter(Activity activity, List<Album> list) {
        super(activity, list);
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
        if (!album.isAdd()) {
            Glide.with(activity)
                    .load(album.getCover())
                    .into(holder.cover);
            holder.name.setText(album.getName());
            holder.count.setText(album.getCount() + "张");
        }
    }

    @Override
    public void insert(Album album) {
        long result = App.addAlbum(activity, album);
        if (result > 0) {
            super.insert(getItemCount(), album);
        } else if (result < 0) {
            toast.showShort("图集名不能重复");
        } else {
            toast.showShort("创建失败");
        }
    }

    public void addAlbum() {
        InputDialog dialog = new InputDialog(activity);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setLabel("图集名")
                .setHint("请输入")
                .setOnConfirmListener(input -> {
                    if (input.trim().isEmpty()) {
                        toast.showShort("输入不能为空");
                    } else {
                        if (input.trim().length() > 6) {
                            toast.showShort("字符长度不能大于6个！");
                            return;
                        } else {
                            String name = input.trim();
                            if (Verifier.contains(list, name, "getName")) {
                                toast.showShort("图集名已被占用");
                                return;
                            }
                        }
                        Album album = new Album(App.getNextAlbumId(activity), input);
                        insert(album);
                        dialog.cancel();
                    }
                });
        dialog.show();
    }

    public class AlbumHolder extends ViewHolder {
        RelativeLayout item;
        ImageView cover;
        TextView name;
        TextView count;

        public AlbumHolder(@NonNull View view) {
            super(view);
        }


        void openAlbum() {
            Intent intent = new Intent(activity, AlbumActivity.class);
            intent.putExtra("name", list.get(getAdapterPosition()).getName());
            intent.putExtra("aid", list.get(getAdapterPosition()).getId());
            List<String> desktops = list.get(getAdapterPosition()).getDesktops();
            String[] images = new String[desktops == null ? 0 : desktops.size()];
            if (images.length > 0) {
                for (int i = desktops.size() - 1; i >= 0; i--) {
                    images[i] = desktops.get(i);
                }
            }
            intent.putExtra("images", images);
            activity.startActivity(intent);
        }
    }


    public class RollAlbumHolder extends AlbumHolder {

        public RollAlbumHolder(@NonNull View view) {
            super(view);
            item = view.findViewById(R.id.item);
            cover = view.findViewById(R.id.cover);
            name = view.findViewById(R.id.name);
            count = view.findViewById(R.id.picture_count);
            item.setOnClickListener(v -> openAlbum());
        }

    }

    public class BigAlbumHolder extends AlbumHolder {


        public BigAlbumHolder(@NonNull View view) {
            super(view);
            item = view.findViewById(R.id.item);
            cover = view.findViewById(R.id.cover);
            name = view.findViewById(R.id.name);
            count = view.findViewById(R.id.picture_count);

            int w = WindowUtil.getWindowWidth(activity);
            int h = WindowUtil.getWindowHeight(activity);
            ViewUtil.setWidthHeight(item, w / 2, h / 3);

            item.setOnClickListener(v -> openAlbum());
        }

    }
}
