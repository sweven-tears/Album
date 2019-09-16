package luoluna.sweven.album.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sweven.base.BaseRecyclerAdapter;
import com.sweven.dialog.InputDialog;
import com.sweven.util.ViewUtil;
import com.sweven.util.WindowUtil;

import java.util.List;

import luoluna.sweven.album.R;
import luoluna.sweven.album.app.App;
import luoluna.sweven.album.bean.Album;

/**
 * Created by Sweven on 2019/9/10--17:01.
 * Email: sweventears@foxmail.com
 */
public class AlbumAdapter extends BaseRecyclerAdapter<Album> {
    private static final int NORMAL = 1;
    private static final int ADD = 0;

    public AlbumAdapter(Activity activity, List<Album> list) {
        super(activity, list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (App.album == App.BIG_ALBUM) {
            if (viewType == NORMAL) {
                view = inflater.inflate(R.layout.item_list_album_big, parent, false);
                return new BigAlbumHolder(view);
            }
            view = inflater.inflate(R.layout.item_list_album_big_add, parent, false);
            return new BigAlbumAddHolder(view);
        } else {
            if (viewType == NORMAL) {
                view = inflater.inflate(R.layout.item_list_album_roll, parent, false);
                return new RollAlbumHolder(view);
            }
            view = inflater.inflate(R.layout.item_list_album_roll_add, parent, false);
            return new RollAlbumAddHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).isAdd() ? ADD : NORMAL;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        AlbumHolder holder = (AlbumHolder) viewHolder;
        Album album = list.get(position);
        if (!album.isAdd()) {
            Glide.with(activity)
                    .load(album.getPath())
                    .into(holder.cover);
            holder.name.setText(album.getName());
            holder.count.setText(album.getCount() + "张");
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return list.get(i).isAdd() ? ADD : NORMAL;
            }
        });
    }

    public void cutList() {
        notifyDataSetChanged();
    }

    public class AlbumHolder extends ViewHolder {
        RelativeLayout item;
        ImageView cover;
        TextView name;
        TextView count;

        public AlbumHolder(@NonNull View view) {
            super(view);
        }
    }


    public class RollAlbumHolder extends AlbumHolder {

        public RollAlbumHolder(@NonNull View view) {
            super(view);
            item = view.findViewById(R.id.item);
            cover = view.findViewById(R.id.cover);
            name = view.findViewById(R.id.name);
            count = view.findViewById(R.id.picture_count);
            item.setOnClickListener(v -> toast.showShort("roll打开"));
        }

    }

    public class RollAlbumAddHolder extends AlbumHolder {

        public RollAlbumAddHolder(@NonNull View view) {
            super(view);
            item = view.findViewById(R.id.item);
            item.setOnClickListener(v -> toast.showShort("roll添加"));
        }
    }

    public class BigAlbumAddHolder extends AlbumHolder {

        public BigAlbumAddHolder(@NonNull View view) {
            super(view);
            item = view.findViewById(R.id.item);
            int w = WindowUtil.getWindowWidth(activity);
            int h = WindowUtil.getWindowHeight(activity);
            ViewUtil.setWidthHeight(item, w / 2, h / 3);
            item.setOnClickListener(v -> {
                InputDialog dialog = new InputDialog(activity);
                dialog.setLabel("图集名")
                        .setHint("请输入")
                        .setOnConfirmListener(input -> {
                            if (input.isEmpty()) {
                                toast.showShort("输入不能为空");
                            } else {
                                Album album = new Album(1, input);
                                insert(0, album);
                                dialog.cancel();
                            }
                        });
                dialog.show();
            });
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

            item.setOnClickListener(v -> toast.showShort("Big打开"));
        }

    }
}
