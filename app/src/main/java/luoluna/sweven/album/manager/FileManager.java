package luoluna.sweven.album.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
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
    private static Context mContext;
    private static ContentResolver mContentResolver;

    public static FileManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (FileManager.class) {
                if (mInstance == null) {
                    mInstance = new FileManager();
                    mContext = context;
                    mContentResolver = context.getContentResolver();
                }
            }
        }
        return mInstance;
    }

    public List<Album> get() {
        List<Album> albums = new ArrayList<>();

        String selection = MediaStore.Images.Media.MIME_TYPE;
        Cursor cursor = mContentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null
                , null,
                "date_modified desc");
        if (cursor != null) {
            Set<String> set = new HashSet<>();
            while (cursor.moveToNext()) {
                String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String[] supports = App.supportFormat.toArray(new String[0]);
                if (FileUtil.isEndName(filePath, supports)) {
                    File file = new File(filePath);
                    set.add(Objects.requireNonNull(file.getParentFile()).getAbsolutePath());
                }
            }
            String[] arrays = set.toArray(new String[0]);
            for (int i = 0; i < arrays.length; i++) {
                File file = new File(arrays[i]);
                Album album = new Album(i + 1, file.getName());
                String[] desktops = getImgListByDir(arrays[i]).split(";");
                album.setDesktops(Arrays.asList(desktops));
                album.setPath(file.getAbsolutePath());
                album.setCount(desktops.length);
                album.setCover(desktops[0]);
                albums.add(album);
            }
            cursor.close();
        }
        return albums;
    }

    /**
     * 得到图片文件夹集合
     */
//    public List<Album> getAlbumList() {
//
//        List<Album> albums = new ArrayList<>();
//        // 扫描图片
//        Cursor c = null;
//        try {
//            c = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
//                    MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ?",
//                    new String[]{"image/jpeg", "image/png"}, "date_modified desc");
//            List<String> mDirs = new ArrayList<>();//用于保存已经添加过的文件夹目录
//            if (c != null) {
//                while (c.moveToNext()) {
//                    String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));// 路径
//
//                    File parentFile = new File(path).getParentFile();
//                    if (parentFile == null)
//                        continue;
//
//                    String dir = parentFile.getAbsolutePath();
//                    if (mDirs.contains(dir)) {//如果已经添加过
//                        //追加三张图片
//                        for (Album folder : albums) {
//                            if (folder.getDir().equals(dir)) {//取出当前文件夹
//                                String[] imgPaths = folder.getPreFourImgPath().split(",");
//                                if (imgPaths.length >= 4) {
//                                    break;
//                                } else {
//                                    folder.setPreFourImgPath(folder.getPreFourImgPath() + path + ",");
//                                }
//                            }
//                        }
//                        continue;
//                    }
//
//                    mDirs.add(dir);//添加到保存目录的集合中
//                    Album folderBean = new Album();
//                    folderBean.setDir(dir);
//                    folderBean.setPreFourImgPath(path + ",");
//                    if (parentFile.list() == null)
//                        continue;
//                    int count = Objects.requireNonNull(parentFile.list((dir1, filename) -> {
//                        if (filename.endsWith(".jpeg") || filename.endsWith(".jpg") || filename.endsWith(".png")) {
//                            return true;
//                        }
//                        return false;
//                    })).length;
//
//                    folderBean.setCount(count);
//                    albums.add(folderBean);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (c != null) {
//                c.close();
//            }
//        }
//        return albums;
//    }

    /**
     * 通过图片文件夹的路径获取该目录下的图片
     */
    public String getImgListByDir(String dir) {
        StringBuilder builder = new StringBuilder("");
        File directory = new File(dir);
        if (!directory.exists()) {
            return builder.toString();
        }
        File[] files = directory.listFiles();
        //对文件进行排序
        Arrays.sort(Objects.requireNonNull(files), new FileComparator());
        for (File file : files) {
            String path = file.getAbsolutePath();
            if (FileManager.isPicFile(path)) {
                builder.append(path).append(";");
            }
        }
        return builder.toString();
    }

    /**
     * 是否是图片文件
     */
    public static boolean isPicFile(String path) {
        path = path.toLowerCase();
        if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")) {
            return true;
        }
        return false;
    }

    class FileComparator implements Comparator<File> {

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
