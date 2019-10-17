package luoluna.sweven.album.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.sweven.util.LogUtil;

import java.util.List;

import luoluna.sweven.album.app.Helper;
import luoluna.sweven.album.bean.Album;
import luoluna.sweven.album.manager.FileManager;

import static luoluna.sweven.album.MainActivity.SYSTEM_ALBUM;

public class ScanPhotoAsync extends AsyncTask<Integer, String, List<Album>> {

    @SuppressLint("StaticFieldLeak")
    private Context context;

    private PostListener<List<Album>> callBack;

    public ScanPhotoAsync(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Album> doInBackground(Integer... integers) {
        int type = integers[0];
        if (type == SYSTEM_ALBUM) {
            new LogUtil(this.getClass().getSimpleName()).i("同步相册");
            return FileManager.getInstance().getSystemAlbums(context);
        } else {
            new LogUtil(this.getClass().getSimpleName()).i("同步图集");
            return Helper.with().queryByAlbumList(context);
        }
    }

    @Override
    protected void onPostExecute(List<Album> s) {
        if (callBack != null) {
            callBack.call(s);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public void setCallBack(PostListener<List<Album>> callBack) {
        this.callBack = callBack;
    }

    public interface PostListener<T> {
        void call(T t);
    }
}
