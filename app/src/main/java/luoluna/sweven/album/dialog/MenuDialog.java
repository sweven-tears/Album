package luoluna.sweven.album.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import luoluna.sweven.album.R;
import luoluna.sweven.album.adapter.MenuAdapter;
import luoluna.sweven.album.entity.Menu;
import luoluna.sweven.album.entity.PictureMenu;

public class MenuDialog extends Dialog {

    private Activity activity;

    private List<Menu<?>> list = new ArrayList<>();
    private RecyclerView recyclerView;

    public MenuDialog(Activity activity) {
        super(activity, com.sweven.util.R.style.NormalDialogStyle);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_menu);
        recyclerView = findViewById(R.id.menu_list);
        init();
    }

    private void init() {
        Menu<PictureMenu> menu = new Menu<PictureMenu>("重命名", "rename") {
        };
        list.add(menu);
        MenuAdapter adapter = new MenuAdapter(activity, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
