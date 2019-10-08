package luoluna.sweven.album.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.sweven.helper.SQLite;
import com.sweven.util.FileUtil;
import com.sweven.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luoluna.sweven.album.bean.Album;

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
     * 查询用户自定义图集地址集合
     *
     * @param context 上下文
     * @return 图集信息
     */
    public List<Album> getAlbumByCustomer(Context context) {
        Cursor cursor = query(context, albumListTableName, "system=?", 0);
        List<Album> paths = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int aid = cursor.getInt(cursor.getColumnIndex("aid"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String path = cursor.getString(cursor.getColumnIndex("path"));
            Album album = new Album(aid, name);
            album.setPath(path);
            paths.add(album);
        }
        cursor.close();
        return paths;
    }

    /**
     * 创建新的相册
     *
     * @param context 上下文
     * @param album   新建album信息
     * @return 是否创建成功
     */
    public long addAlbum(Context context, Album album) {
        Map<String, Object> map = new HashMap<>();
        map.put("aid", album.getId());
        map.put("name", album.getName());
        map.put("cover", album.getCover());
        map.put("path", album.getPath());
        map.put("count", album.getCount());
        map.put("system", album.isSystem() ? 1 : 0);

        if (getAlbumByAid(context, album.getId()) != null) {
            return -1;
        }

        return new SQLite(context, database, albumListTableName, SQLite.UPDATE).insert(map);
    }

    /**
     * 更新图集信息
     *
     * @param context 上下文
     * @param album   album
     */
    public long updateAlbum(Context context, Album album) {
        Map<String, Object> map = new HashMap<>();
        if (album.getName() != null) {
            map.put("name", album.getName());
        }
        if (album.getCover() != null) {
            map.put("cover", album.getCover());
        }
        if (album.getPath() != null) {
            map.put("path", album.getPath());
        }
        if (album.getCount() != -1) {
            map.put("count", album.getCount());
        }
        return new SQLite(context, database, albumListTableName, SQLite.UPDATE)
                .update(map, "aid=?", String.valueOf(album.getId()));
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
            boolean system = cursor.getInt(cursor.getColumnIndex("system")) == 1;
            Album album = new Album(aid, name);
            album.setCount(count);
            album.setPath(path);
            album.setRemark(remark);
            album.setCover(cover);
            album.setSystem(system);

            list.add(album);
        }
        cursor.close();
        return list;
    }
}
