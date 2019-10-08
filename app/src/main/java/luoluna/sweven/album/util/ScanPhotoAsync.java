package luoluna.sweven.album.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.sweven.interf.CallBack;
import com.sweven.util.ToastUtil;

import java.util.Arrays;
import java.util.List;

import luoluna.sweven.album.app.Helper;
import luoluna.sweven.album.bean.Album;
import luoluna.sweven.album.manager.FileManager;

public class ScanPhotoAsync extends AsyncTask<String, String, Integer> {

    @SuppressLint("StaticFieldLeak")
    private Context context;

    private CallBack callBack;
    private int before;

    public ScanPhotoAsync(Context context, int before) {
        this.context = context;
        this.before = before;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        List<Album> systemAlbums = FileManager.getInstance().get(context);

        try {
            for (Album album : systemAlbums) {
                if (Helper.with().addAlbum(context, album) < 1) {
                    album.setCover(null);
                    Helper.with().updateAlbum(context, album);
                }
            }
        } catch (Exception e) {

        }
        List<Album> customerAlbums = Helper.with().getAlbumByCustomer(context);
        for (int i = 0; i < customerAlbums.size(); i++) {
            Album album = customerAlbums.get(i);
            String[] desktops = FileManager.getImgListByDir(album.getPath()).split(";");
            album.setCount(desktops.length);
            album.setDesktops(Arrays.asList(desktops));
            Helper.with().updateAlbum(context, album);
        }
        return systemAlbums.size();
    }

    @Override
    protected void onPostExecute(Integer s) {
        if (before > 0 && s - before > 0) {
            ToastUtil.showShort(context, "当前更新了" + (s - before) + "个图集");
        }
        if (callBack != null) {
            callBack.call();
        }
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
}
