package luoluna.sweven.album.page;

import android.os.Bundle;
import android.widget.ImageView;

import com.sweven.base.BaseActivity;
import com.sweven.dialog.NoticeDialog;
import com.sweven.ioc.Inject;
import com.sweven.ioc.annotation.BindView;
import com.sweven.ioc.annotation.ContentView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import luoluna.sweven.album.MainActivity;
import luoluna.sweven.album.R;

@ContentView(R.layout.activity_launch)
public class LaunchActivity extends BaseActivity {

    @BindView(R.id.image)
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Inject.init(this);
        if (AndPermission.hasPermissions(this,
                Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)) {
            init();
        } else {
            showTips();
        }
        imageView.setImageResource(R.drawable.ic_album_info);
    }

    private void showTips() {
        NoticeDialog noticeDialog = new NoticeDialog(this);
        noticeDialog.setTitle("需获取以下权限\n\r才能使用本应用：\n\r读写权限");
        noticeDialog.setEnterListener(() -> {
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
