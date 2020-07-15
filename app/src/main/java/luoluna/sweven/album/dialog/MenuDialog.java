package luoluna.sweven.album.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import java.util.List;

import luoluna.sweven.album.entity.BaseMenu;

public class MenuDialog extends Dialog {

    private List<BaseMenu<?>> list;

    public MenuDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
