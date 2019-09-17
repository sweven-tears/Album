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
import luoluna.sweven.album.bean.Image;

/**
 * Created by Sweven on 2019/9/17--13:58.
 * Email: sweventears@foxmail.com
 */
public class ImageAdapter extends BaseRecyclerAdapter<Image> {

    public ImageAdapter(Activity activity, List<Image> list) {
        super(activity, list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ImageViewHolder holder = (ImageViewHolder) viewHolder;
        Image image = list.get(position);

        Glide.with(activity)
                .load(image)
//                .placeholder(image)
                .into(holder.image);
    }

    public class ImageViewHolder extends ViewHolder {

        private ImageView image;

        public ImageViewHolder(@NonNull View view) {
            super(view);
            image = view.findViewById(R.id.image);
        }
    }
}
