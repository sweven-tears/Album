package luoluna.sweven.album.activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.sweven.base.BaseActivity;
import com.sweven.util.AnimationUtil;
import com.sweven.util.WindowUtil;

import java.util.Timer;
import java.util.TimerTask;

import luoluna.sweven.album.R;

public class PictureLookActivity_copy extends BaseActivity implements View.OnTouchListener {

    private ImageView imageView;

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_picture_look);
        getWindow().setEnterTransition(new Fade().setDuration(500));
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

        imageView.setOnTouchListener(this);
    }

    @Override
    public void finish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityOptions.makeClipRevealAnimation(imageView, 0, 0, 0, 0);
        }
        super.finish();
    }

    float dins = 0;
    float y = 0;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y = motionEvent.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                dins = motionEvent.getY() - y;
                System.out.println(y + "\t" + motionEvent.getY() + "\t" + dins);
                if (dins >= 300 && dins <= 400) {
                    AnimationUtil.with().moveToViewBottom(imageView, 500);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 500);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (dins == 0) {
                    toast.showShort("点击");
                }
                break;
        }
        return true;
    }

}
