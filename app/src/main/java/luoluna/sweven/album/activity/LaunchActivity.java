package luoluna.sweven.album.activity;

import android.os.Bundle;

import com.sweven.base.BaseActivity;
import com.sweven.util.ToastUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.List;

import luoluna.sweven.album.MainActivity;
import luoluna.sweven.album.R;
import luoluna.sweven.album.app.Helper;
import luoluna.sweven.album.bean.Album;
import luoluna.sweven.album.manager.FileManager;

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .onDenied(data -> {
                    ToastUtil.showShort(this, "部分功能将无法使用");
                    scanImageStore();
                })
                .onGranted(data -> scanImageStore())
                .start();
    }

    private void scanImageStore() {
        List<Album> list = new ArrayList<>();
        try {
            list = FileManager.getInstance(this).get();
        } catch (Exception e) {
            toast.showShort("权限不足，无法扫描相册");
            e.printStackTrace();
        }
        for (Album album : list) {
            Helper.with().addAlbum(this, album);
        }
        startActivity(MainActivity.class);
        finish();
    }
}
