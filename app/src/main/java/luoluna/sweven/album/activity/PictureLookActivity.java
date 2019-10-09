package luoluna.sweven.album.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.widget.ImageView;

import com.sweven.base.BaseActivity;
import com.sweven.util.WindowUtil;

import luoluna.sweven.album.R;

public class PictureLookActivity extends BaseActivity {

    private ImageView imageView;

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_look);
        getWindow().setEnterTransition(new Fade().setDuration(500));
        getWindow().setExitTransition(new Fade().setDuration(5000));

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

    @Override
    protected void initData() {
        imageView.setImageURI(uri);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
