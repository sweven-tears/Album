package luoluna.sweven.album.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import java.util.List;

import luoluna.sweven.album.bean.Menu;

public class MenuDialog extends Dialog {

    private List<Menu> list;

    public MenuDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
