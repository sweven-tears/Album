package luoluna.sweven.album.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.sweven.base.BaseActivity;
import com.sweven.util.ViewUtil;
import com.sweven.util.WindowUtil;

import luoluna.sweven.album.R;

public class PictureLookActivity extends BaseActivity {

    private ImageView imageView;

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_look);

        WindowUtil.fullScreen(this);
        getBundle();
        bindView();
        initData();
    }

    private void getBundle() {
        Intent intent = getIntent();
        uri = Uri.parse(intent.getStringExtra("picture_uri"));
    }

    @Override
    protected void bindView() {
        imageView = bindID(R.id.picture);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initData() {
        imageView.setImageURI(uri);

        imageView.setOnClickListener(v -> finish());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(com.sweven.util.R.anim.alpha_open,com.sweven.util.R.anim.alpha_close);
    }


}
