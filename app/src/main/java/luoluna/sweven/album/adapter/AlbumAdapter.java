package luoluna.sweven.album.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.sweven.base.BaseRecyclerAdapter;

import java.util.List;

import luoluna.sweven.album.R;
import luoluna.sweven.album.app.App;
import luoluna.sweven.album.bean.Album;

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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
    }

    public class BigAlbumHolder extends ViewHolder {

        public BigAlbumHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class RollAlbumHolder extends ViewHolder {

        public RollAlbumHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
