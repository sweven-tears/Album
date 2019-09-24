package luoluna.sweven.album.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.sweven.base.BaseRecyclerAdapter;

import java.util.List;

import luoluna.sweven.album.R;
import luoluna.sweven.album.bean.Picture;

/**
 * Created by Sweven on 2019/9/17--13:58.
 * Email: sweventears@foxmail.com
 */
public class PictureAdapter extends BaseRecyclerAdapter<Picture> {

    public PictureAdapter(Activity activity, List<Picture> list) {
        super(activity, list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_image, parent, false);
        return new PictureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        PictureViewHolder holder = (PictureViewHolder) viewHolder;
        Picture picture = list.get(position);

        Glide.with(activity)
                .load(picture)
                .placeholder(R.drawable.ic_album_no_cover)
                .error(R.drawable.ic_broken_image)
                .into(holder.image);
    }

    public class PictureViewHolder extends ViewHolder {

        private ImageView image;

        public PictureViewHolder(@NonNull View view) {
            super(view);
            image = view.findViewById(R.id.image);
        }
    }
}
