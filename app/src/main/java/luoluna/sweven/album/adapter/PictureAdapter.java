package luoluna.sweven.album.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sweven.base.BaseRecyclerAdapter;
import com.sweven.util.ViewUtil;

import androidx.annotation.NonNull;
import luoluna.sweven.album.R;
import luoluna.sweven.album.entity.local.Album;
import luoluna.sweven.album.entity.local.Image;
import luoluna.sweven.album.page.PictureActivity;
import luoluna.sweven.album.page.PictureLookActivity;

/**
 * Created by Sweven on 2019/9/17--13:58.
 * Email: sweventears@foxmail.com
 */
public class PictureAdapter extends BaseRecyclerAdapter<Image> {

    private Album album;

    public PictureAdapter(PictureActivity activity, Album album) {
        super(activity, album.getDesktops());
        this.album = album;
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
        Image image = list.get(position);

        Glide.with(activity)
                .load(image.getUri())
                .into(holder.image);
        ViewUtil.notifyMeasure(holder.image);
    }

    public class PictureViewHolder extends ViewHolder {

        private ImageView image;

        PictureViewHolder(@NonNull View view) {
            super(view);
            image = view.findViewById(R.id.image);

            image.setOnClickListener(v -> {
                Intent intent = new Intent(activity, PictureLookActivity.class);
                intent.putExtra("present", getAdapterPosition());
                intent.putExtra("aid", album.getId());
                activity.startActivity(intent);
            });
        }
    }
}
