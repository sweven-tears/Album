package luoluna.sweven.album.page;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sweven.base.BaseActivity;

import luoluna.sweven.album.R;

public class AlbumSettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;
    private RelativeLayout back;
    private ImageView backIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_setting);
        bindView();
        initData();
    }

    @Override
    protected void bindView() {
        title = bindId(R.id.title);
        back = bindId(R.id.back);
        backIv = bindId(R.id.back_image);
    }

    @Override
    protected void initData() {
        title.setText(R.string.image_settingTitle);
        backIv.setVisibility(View.VISIBLE);


        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
