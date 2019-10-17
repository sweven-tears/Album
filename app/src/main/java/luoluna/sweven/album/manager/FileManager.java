package luoluna.sweven.album.manager;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.sweven.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import luoluna.sweven.album.app.App;
import luoluna.sweven.album.bean.Album;

/**
 * Created by Sweven on 2019/9/17.
 * Email:sweventears@Foxmail.com
 */
public class FileManager {

    private static FileManager mInstance;

    public static FileManager getInstance() {
        if (mInstance == null) {
            synchronized (FileManager.class) {
                if (mInstance == null) {
                    mInstance = new FileManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * @param context 上下文
     * @return 获取系统相册列表
     */
    public List<Album> getSystemAlbums(Context context) {
        List<Album> albums = new ArrayList<>();

        String selection = MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?";
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                selection,
                new String[]{"image/jpeg", "image/png", "image/gif"},
                "date_modified desc");
        Set<String> set = new HashSet<>();
        if (cursor == null) {
            return null;
        }
        while (cursor.moveToNext()) {
            String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            File file = new File(filePath);
            set.add(Objects.requireNonNull(file.getParentFile()).getAbsolutePath());
        }
        cursor.close();
        String[] arrays = set.toArray(new String[0]);
        for (int i = 0; i < arrays.length; i++) {
            File file = new File(arrays[i]);
            Album album = new Album(i + 1, file.getName());
            List<String> desktops = FileUtil.getFilesByEndName(file.getPath(), App.supportFormat);
            album.setDesktops(desktops);
            album.setPath(file.getAbsolutePath());
            album.setCount(desktops.size());
            album.setCover(desktops.get(0));
            albums.add(album);
        }
        return albums;
    }

    /**
     * 通过路径获取album信息
     *
     * @param context 上下文
     * @param folder  文件夹路径
     * @return album
     */
    public Album getAlbumByFolder(Context context, String folder) {
        Album album = new Album(0, null);
        List<String> desktops = FileUtil.getFilesByEndName(folder, App.supportFormat);
        album.setDesktops(desktops);
        album.setCount(desktops.size());
        album.setPath(folder);
        album.setCover(desktops.get(0));
        return album;
    }

    static class FileComparator implements Comparator<File> {

        @Override
        public int compare(File lhs, File rhs) {
            if (lhs.lastModified() < rhs.lastModified()) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
