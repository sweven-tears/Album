package luoluna.sweven.album.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.sweven.base.BaseRecyclerAdapter;
import com.sweven.util.ViewUtil;

import java.util.List;

import luoluna.sweven.album.R;
import luoluna.sweven.album.page.PictureLookActivity;
import luoluna.sweven.album.bean.Picture;

/**
 * Created by Sweven on 2019/9/17--13:58.
 * Email: sweventears@foxmail.com
 */
public class PictureAdapter extends BaseRecyclerAdapter<Picture> {

    private List<String> desktops;

    public PictureAdapter(Activity activity, List<Picture> list, List<String> desktops) {
        super(activity, list);
        this.desktops = desktops;
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

        holder.image.setImageURI(Uri.parse(picture.getUri()));

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
                intent.putExtra("images", desktops.toArray(new String[0]));
                activity.startActivity(intent);
            });
        }
    }
}
