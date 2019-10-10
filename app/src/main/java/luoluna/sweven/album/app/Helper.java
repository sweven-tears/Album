package luoluna.sweven.album.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.sweven.sqlite.SQLite;
import com.sweven.sqlite.bean.Rows;
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

        Rows column = SQLite.with(context)
                .readTable(database, albumListTableName)
                .orderBy("aid desc")
                .query();
        if (column.size() > 0) {
            nextAlbumId = column.getInt(0, "aid");
        }
        return nextAlbumId + 1;
    }

    /**
     * 查询某张表
     *
     * @param context 上下文
     * @param table   数据库表名
     * @return Cursor
     */
    public Rows query(Context context, String table) {
        return SQLite.with(context)
                .readTable(database, table)
                .query();
    }

    /**
     * 条件查询查询某张表
     *
     * @param context 上下文
     * @param table   数据库表名
     * @return Cursor
     */
    public Rows query(Context context, String table, String where, Object... whereArgs) {
        return SQLite.with(context)
                .readTable(database, table)
                .where(where)
                .selectionArgs(whereArgs)
                .query();
    }

    /**
     * 通过id查询图集信息
     *
     * @param context 上下文
     * @param aid     图集id
     * @return 图集信息
     */
    public Album getAlbumByAid(Context context, int aid) {
        Rows rows = query(context, albumListTableName, "aid=?", aid);
        if (rows.size() > 0) {
            String name = rows.getString(0, "name");
            String path = rows.getString(0, "path");
            String remark = rows.getString(0, "remark");
            String cover = rows.getString(0, "cover");
            Album album = new Album(aid, name);
            album.setPath(path);
            album.setRemark(remark);
            album.setCover(cover);

            Rows rows1 = query(context, albumChildListTableName, "aid=?", aid);
            List<String> images = new ArrayList<>();
            for (int i = 0; i < rows1.size(); i++) {
                String uri = rows.getString(i, "uri");
                images.add(uri);

            }
            if (path != null && !path.isEmpty()) {
                images.addAll(FileUtil.getFilesByEndName(path, App.supportFormat));
            }
            album.setDesktops(images);
            album.setCount(images.size());
            return album;
        }
        return null;
    }

    /**
     * 查询用户自定义图集地址集合
     *
     * @param context 上下文
     * @return 图集信息
     */
    public List<Album> getAlbumByCustomer(Context context) {
        Rows rows = query(context, albumListTableName, "system=?", 0);
        List<Album> paths = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            int aid = rows.getInt(i, "aid");
            String name = rows.getString(i, "name");
            String path = rows.getString(i, "path");
            Album album = new Album(aid, name);
            album.setPath(path);
            paths.add(album);
        }
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

        return SQLite.with(context)
                .writeTable(database, albumListTableName)
                .insert(map);
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
        return SQLite.with(context)
                .writeTable(database, albumListTableName)
                .where("aid=?")
                .selectionArgs(album.getId())
                .update(map);
    }

    /**
     * 删除图集
     *
     * @param context 上下文
     * @param aid     相册id
     * @return 是否删除成功
     */
    public boolean delAlbum(Context context, int aid) {
        return SQLite.with(context)
                .writeTable(database, albumListTableName)
                .where("aid=?")
                .selectionArgs(aid)
                .del() > 0;
    }

    /**
     * @param context 上下文
     * @return 获取相册列表
     */
    public List<Album> queryByAlbumList(Context context) {
        List<Album> list = new ArrayList<>();
        Rows rows = SQLite.with(context)
                .readTable(database, albumListTableName)
                .orderBy("aid desc")
                .query();
        for (int i = 0; i < rows.size(); i++) {
            int aid = rows.getInt(i, "aid");
            String name = rows.getString(i, "name");
            int count = rows.getInt(i, "count");
            String path = rows.getString(i, "path");
            String remark = rows.getString(i, "remark");
            String cover = rows.getString(i, "cover");
            boolean system = rows.getInt(i, "system") == 1;

            Album album = new Album(aid, name);
            album.setCount(count);
            album.setPath(path);
            album.setRemark(remark);
            album.setCover(cover);
            album.setSystem(system);

            list.add(album);
        }
        return list;
    }
}
