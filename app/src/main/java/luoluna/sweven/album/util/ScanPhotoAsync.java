package luoluna.sweven.album.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import luoluna.sweven.album.app.Helper;
import luoluna.sweven.album.bean.Album;
import luoluna.sweven.album.manager.FileManager;

import static luoluna.sweven.album.fragment.main.AlbumFragment.SYSTEM_ALBUM;

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
//        List<Album> systemAlbums = FileManager.getInstance().get(context);
//        for (Album album : systemAlbums) {
//            if (Helper.with().addAlbum(context, album) < 1) {
//                album.setCover(null);
//                Helper.with().updateAlbum(context, album);
//            }
//        }
//        List<Album> customerAlbums = Helper.with().getAlbumByCustomer(context);
//        for (int i = 0; i < customerAlbums.size(); i++) {
//            Album album = customerAlbums.get(i);
//            String[] desktops = FileManager.getImgListByDir(album.getPath()).split(";");
//            album.setCount(desktops.length);
//            album.setDesktops(Arrays.asList(desktops));
//            Helper.with().updateAlbum(context, album);
//        }
        int type = integers[0];
        if (type == SYSTEM_ALBUM) {
            return FileManager.getInstance().get(context);
        } else {
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
