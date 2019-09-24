package luoluna.sweven.album.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sweven.base.BaseRecyclerAdapter;

import java.util.List;

import luoluna.sweven.album.R;
import luoluna.sweven.album.bean.Menu;

public class MenuAdapter extends BaseRecyclerAdapter<Menu> {
    public MenuAdapter(Activity activity, List<Menu> list) {
        super(activity, list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list_menu, parent, false);
        return new MenuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        MenuHolder holder = (MenuHolder) viewHolder;
        Menu menu = list.get(position);

        holder.name.setText(menu.getName());
    }

    class MenuHolder extends ViewHolder {

        TextView name;

        public MenuHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.name);

//            name.setOnClickListener(v -> list.get(getAdapterPosition()).execute());
        }
    }
}
