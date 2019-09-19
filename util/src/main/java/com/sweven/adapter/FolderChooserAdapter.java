package com.sweven.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.sweven.base.BaseRecyclerAdapter;
import com.sweven.bean.Folder;
import com.sweven.util.R;
import com.sweven.widget.TextImageView;

import java.util.List;

public class FolderChooserAdapter extends BaseRecyclerAdapter<Folder> {

    public FolderChooserAdapter(Activity activity, List<Folder> list) {
        super(activity, list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_folder_chooser, parent, false);
        return new FolderChooserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        FolderChooserHolder holder = (FolderChooserHolder) viewHolder;
        Folder folder = list.get(position);

        holder.text.setText(folder.getName());
    }

    public void replace(List<Folder> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class FolderChooserHolder extends ViewHolder {

        private TextImageView text;

        public FolderChooserHolder(@NonNull View view) {
            super(view);
            text = view.findViewById(R.id.item);

            text.setOnClickListener(v ->
            {
                if (onClickItemListener != null) {
                    onClickItemListener.onClick(list.get(getAdapterPosition()));
                }
            });
        }
    }
}
