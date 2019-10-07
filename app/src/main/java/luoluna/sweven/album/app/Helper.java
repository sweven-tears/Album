package luoluna.sweven.album.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.sweven.helper.SQLite;
import com.sweven.util.FileUtil;
import com.sweven.util.PreferenceUtil;
import com.sweven.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import luoluna.sweven.album.bean.Album;
import luoluna.sweven.album.manager.FileManager;

import static luoluna.sweven.album.app.App.albumChildListTableName;
import static luoluna.sweven.album.app.App.albumListTableName;
import static luoluna.sweven.album.app.App.database;

/**
 * app项目的辅助类
 * 公共使用的静态方法保存类
 */
public class Helper {
    private static Helper instance;

    public static Helper with() {
        if (instance == null) {
            synchronized (Helper.class) {
                instance = new Helper();
            }
        }
        return instance;
    }


    // 获取preference对象
    public static PreferenceUtil preference(Context context) {
        return new PreferenceUtil(context, App.preference);
    }

    // 获取editor对象
    public static SharedPreferences.Editor editor(Context context) {
        return preference(context).getEditor();
    }

    /**
     * @param context 上下文
     * @return 图集数量
     */
    public int scanImageStore(Context context) {
        List<Album> list = new ArrayList<>();
        try {
            list = FileManager.getInstance(context).get();
        } catch (Exception e) {
            ToastUtil.showShort(context, "权限不足，无法扫描相册");
            e.printStackTrace();
        }
        int success = 0;
        for (Album album : list) {
            List<Album> albums = queryByAlbumNameList(context);
            Set<String> set = new HashSet<>();
            for (Album a : albums) {
                set.add(a.getName());
            }
            if (!set.add(album.getName())) {
                int same = 0;
                Set<String> path = new HashSet<>();
                for (String s : set) {
                    if (s.startsWith(album.getName())) {
                        path.add(album.getName());
                        same++;
                    }
                }
                if (same > 1) {
                    // 判断路径是否也相同
                    if (!path.add(album.getPath())) {
                        continue;
                    } else {
                        // 相同名的图集改名
//                    album.setName(album.getName() + " [" + (same + 1) + "]");
                    }
                }
            }
            success = success + ((int) addAlbum(context, album));
        }
        return success;
    }

    /**
     * @param context 上下文
     * @return 下一次创建album的id
     */
    public int getNextAlbumId(Context context) {
        int nextAlbumId = 0;
        Cursor cursor = new SQLite(context, database, albumListTableName, SQLite.QUERY).query("aid desc");
        if (cursor.moveToFirst()) {
            nextAlbumId = cursor.getInt(cursor.getColumnIndex("aid"));
        }
        cursor.close();
        return nextAlbumId + 1;
    }

    /**
     * 查询某张表
     *
     * @param context 上下文
     * @param table   数据库表名
     * @return Cursor
     */
    public Cursor query(Context context, String table) {
        return new SQLite(context, database, table, SQLite.QUERY).query();
    }

    /**
     * 条件查询查询某张表
     *
     * @param context 上下文
     * @param table   数据库表名
     * @return Cursor
     */
    public Cursor query(Context context, String table, String where, Object... whereArgs) {
        // object转字符串
        String[] args = new String[whereArgs.length];
        for (int i = 0; i < whereArgs.length; i++) {
            args[i] = String.valueOf(whereArgs[i]);
        }
        return new SQLite(context, database, table, SQLite.QUERY).query(where, args);
    }

    /**
     * 通过id查询图集信息
     *
     * @param context 上下文
     * @param aid     图集id
     * @return 图集信息
     */
    public Album getAlbumByAid(Context context, int aid) {
        Cursor cursor = query(context, albumListTableName, "aid=?", aid);
        if (cursor.moveToFirst()) {
            if (aid != cursor.getInt(cursor.getColumnIndex("aid"))) {
                return null;
            }
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String path = cursor.getString(cursor.getColumnIndex("path"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            String cover = cursor.getString(cursor.getColumnIndex("cover"));
            Album album = new Album(aid, name);
            album.setPath(path);
            album.setRemark(remark);
            album.setCover(cover);

            Cursor imageCursor = query(context, albumChildListTableName, "aid=?", aid);
            List<String> images = new ArrayList<>();
            while (imageCursor.moveToNext()) {
                String uri = imageCursor.getString(imageCursor.getColumnIndex("uri"));
                images.add(uri);
            }
            if (path != null && !path.isEmpty()) {
                images.addAll(FileUtil.getFilesByEndName(path, App.supportFormat));
            }
            album.setDesktops(images);
            album.setCount(images.size());
            return album;
        }
        cursor.close();
        return null;
    }

    /**
     * 创建新的相册
     *
     * @param context 上下文
     * @param album   新建album信息
     * @return 是否创建成功
     */
    public long addAlbum(Context context, Album album) {
        List<Album> list = queryByAlbumNameList(context);
        Set<String> set = new HashSet<>();
        for (Album a : list) {
            set.add(a.getName());
        }
        if (!set.add(album.getName())) {
            int same = 0;
            Set<String> path = new HashSet<>();
            for (String s : set) {
                if (s.startsWith(album.getName())) {
                    path.add(album.getName());
                    same++;
                }
            }
            if (same > 1) {
                // 判断路径是否也相同
                if (!path.add(album.getPath())) {
                    return -1;
                } else {
                    // 相同名的图集改名
//                    album.setName(album.getName() + " [" + (same + 1) + "]");
                }

            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("aid", album.getId());
        map.put("name", album.getName());
        map.put("cover", album.getCover());
        map.put("path", album.getPath());
        map.put("count", album.getCount());
        return new SQLite(context, database, albumListTableName, SQLite.UPDATE).insert(map);
    }

    /**
     * 删除图集
     *
     * @param context 上下文
     * @param aid     相册id
     * @return 是否删除成功
     */
    public boolean delAlbum(Context context, int aid) {
        return new SQLite(context, database, albumListTableName, SQLite.UPDATE).delete("aid", aid) > 1;
    }

    /**
     * @param context 上下文
     * @return 获取相册列表
     */
    public List<Album> queryByAlbumList(Context context) {
        List<Album> list = new ArrayList<>();
        Cursor cursor = query(context, albumListTableName);
        while (cursor.moveToNext()) {
            int aid = cursor.getInt(cursor.getColumnIndex("aid"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int count = cursor.getInt(cursor.getColumnIndex("count"));
            String path = cursor.getString(cursor.getColumnIndex("path"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            String cover = cursor.getString(cursor.getColumnIndex("cover"));
            Album album = new Album(aid, name);
            album.setCount(count);
            album.setPath(path);
            album.setRemark(remark);
            album.setCover(cover);

            list.add(album);
        }
        cursor.close();
        return list;
    }

    /**
     * @param context 上下文
     * @return 获取相册列表
     */
    public List<Album> queryByAlbumNameList(Context context) {
        List<Album> list = new ArrayList<>();
        Cursor cursor = query(context, albumListTableName);
        while (cursor.moveToNext()) {
            int aid = cursor.getInt(cursor.getColumnIndex("aid"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            Album album = new Album(aid, name);
            list.add(album);
        }
        cursor.close();
        return list;
    }
}
