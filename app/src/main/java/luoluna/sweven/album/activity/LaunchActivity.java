package luoluna.sweven.album.activity;

import android.os.Bundle;

import com.sweven.base.BaseActivity;
import com.sweven.dialog.NoticeDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import luoluna.sweven.album.MainActivity;
import luoluna.sweven.album.R;

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        if (AndPermission.hasPermissions(this,
                Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)) {
            init();
        } else {
            showTips();
        }
    }

    private void showTips() {
        NoticeDialog noticeDialog = new NoticeDialog(this);
        noticeDialog.setTitle("需获取以下权限\n\r才能使用本应用：\n\r读写权限");
        noticeDialog.setCallBack(() -> {
            AndPermission.with(this)
                    .runtime()
                    .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                    .onDenied(data -> showTips())
                    .onGranted(data -> init())
                    .start();
        });
        noticeDialog.show();
    }

    private void init() {
        startActivity(MainActivity.class);
        finish();
    }
}
