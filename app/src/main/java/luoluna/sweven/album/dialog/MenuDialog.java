package luoluna.sweven.album.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sweven.console.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import luoluna.sweven.album.R;
import luoluna.sweven.album.adapter.MenuAdapter;
import luoluna.sweven.album.entity.AbsMenu;
import luoluna.sweven.album.entity.IMenu;
import luoluna.sweven.album.entity.PictureMenu;

public class MenuDialog extends Dialog {

    private Activity activity;

    private List<AbsMenu> list = new ArrayList<>();
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
        PictureMenu menu = new PictureMenu("重命名", "rename");
        list.add(move);
        list.add(copy);
        list.add(menu);
        list.add(details);
        MenuAdapter adapter = new MenuAdapter(activity, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        clear.execute();
    }

    private IMenu<Void, Void> clear = new IMenu<>(
            "menu",
            params -> {
                System.out.println("param");
                return params;
            });

    private AbsMenu details = new AbsMenu("详情", "details") {
        public void details() {

        }
    };

    private AbsMenu move = new AbsMenu("移动", "move") {
        public void move() {
            ToastUtil.showShort(activity, "this file need move to other directory.");
        }
    };

    private AbsMenu copy = new AbsMenu("复制", "copy") {
        public void copy() {

        }
    };
}
