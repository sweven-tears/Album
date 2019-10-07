package luoluna.sweven.album.util;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.sweven.interf.CallBack;
import com.sweven.util.AnimationUtil;
import com.sweven.util.ToastUtil;

import luoluna.sweven.album.app.Helper;

public class ScanCamera extends AsyncTask<Context, Integer, Integer> {

    private Context context;
    private ImageView refreshIv;

    private CallBack callBack;
    private int before;

    public ScanCamera(Context context, ImageView refreshIv, int before) {
        this.context = context;
        this.refreshIv = refreshIv;
        this.before = before;
    }

    @Override
    protected Integer doInBackground(Context... strings) {
        return Helper.with().scanImageStore(context);
    }

    @Override
    protected void onPostExecute(Integer s) {
        AnimationUtil.with().stopRotateSameSpeed(refreshIv);
        ToastUtil.showShort(context,"当前更新了" + (s-before) + "个图集");
        if (callBack!=null){
            callBack.call();
        }
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
}
