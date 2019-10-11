package luoluna.sweven.album.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.sweven.base.BaseRecyclerAdapter;

import java.util.List;

import luoluna.sweven.album.R;
import luoluna.sweven.album.bean.Picture;

public class PictureLookAdapter extends BaseRecyclerAdapter<Picture> {

    public PictureLookAdapter(Activity activity, List<Picture> list) {
        super(activity, list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_picture_look, parent, false);
        return new PictureLookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        PictureLookViewHolder holder = (PictureLookViewHolder) viewHolder;
        Picture picture = list.get(position);

        holder.picture.setImageURI(Uri.parse(picture.getUri()));
    }

    public class PictureLookViewHolder extends ViewHolder {

        private ImageView picture;

        public PictureLookViewHolder(@NonNull View view) {
            super(view);
            picture = view.findViewById(R.id.picture);

            picture.setOnClickListener(v -> activity.finish());
        }
    }
}
