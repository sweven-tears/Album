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
import luoluna.sweven.album.entity.IMenu;

public class MenuDialog extends Dialog {

    private Activity activity;

    private List<IMenu<?>> list = new ArrayList<>();
    private RecyclerView recyclerView;

    public MenuDialog(Activity activity) {
        super(activity, com.sweven.util.R.style.NormalDialogStyle);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_menu);
        setCanceledOnTouchOutside(true);
        recyclerView = findViewById(R.id.menu_list);
        findViewById(R.id.layout).setOnClickListener(v -> dismiss());
        init();
    }

    private void init() {
        list.add(move);
        list.add(copy);
        list.add(details);
        MenuAdapter adapter = new MenuAdapter(activity, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private IMenu<Void> details = new IMenu<>("menu", params -> {
    });

    private IMenu<Void> move = new IMenu<>("移动", params -> {
    });

    private IMenu<Void> copy = new IMenu<>("复制", params -> {
    });
}
