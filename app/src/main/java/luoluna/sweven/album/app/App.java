package luoluna.sweven.album.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.sweven.helper.DatabaseHelper;
import com.sweven.helper.SQLite;
import com.sweven.util.FileUtil;
import com.sweven.util.PreferenceUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import luoluna.sweven.album.bean.Album;
import luoluna.sweven.album.manager.FileManager;
import luoluna.sweven.album.manager.Setting;

/**
 * Created by Sweven on 2019/9/10--16:53.
 * Email: sweventears@foxmail.com
 */
public class App extends Application {

    public static final String appName = "Album";
    public static final String preference = "album";
    public static final String database = "album";
    public static final String albumListTableName = "ouralbum";
    public static final String albumChildListTableName = "ourimage";
    public static final String[] databaseSql = new String[]{
            "create table " + albumChildListTableName + " (" +
                    "id int not null ," +
                    "aid int not null," +
                    "uri varchar(80) not null," +
                    "primary key (id)" +
                    ")",
            "create table " + albumListTableName + "(" +
                    "aid int not null ," +
                    "name varchar not null," +
                    "path varchar(80) default null," +
                    "remark varchar default ''," +
                    "cover varchar default ''," +
                    "primary key (aid)" +
                    ")"
    };

    public static final int BIG_ALBUM = 2;
    public static final int ROLL_ALBUM = 1;

    // 配置信息
    public static boolean isFirst;
    public static int album = BIG_ALBUM;
    public static Set<String> supportFormat = new HashSet<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Setting.getInstance(this);
        if (isFirst) {
            DatabaseHelper.createSql = databaseSql;
            new DatabaseHelper(this, database);
        }
        AppManager.getInstance().setAppStatus(AppStatus._LAUNCH);

        AndPermission.with(this)
                .runtime()
                .permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE)
                .onGranted(data -> scanImageStore())
                .onDenied(data -> scanImageStore())
                .start();
    }

    private void scanImageStore() {
        List<Album> list = FileManager.getInstance(this).get();
        for (Album album : list) {
            App.addAlbum(this, album);
        }
    }

    /**
     * 获取preference对象
     *
     * @param context 上下文
     * @return PreferenceUtil
     */
    public static PreferenceUtil preference(Context context) {
        return new PreferenceUtil(context, App.preference);
    }

    /**
     * 获取editor对象
     *
     * @param context 上下文
     * @return 获取preference的editor
     */
    public static SharedPreferences.Editor editor(Context context) {
        return preference(context).getEditor();
    }

    /**
     * @param context 上下文
     * @return 下一次创建album的id
     */
    public static int getNextAlbumId(Context context) {
        int nextAlbumId = 0;
        Cursor cursor = new SQLite(context, database, albumListTableName, SQLite.QUERY).query("aid desc");
        if (cursor.moveToFirst()) {
            nextAlbumId = cursor.getInt(cursor.getColumnIndex("aid"));
        }
        return nextAlbumId + 1;
    }

    /**
     * 查询某张表
     *
     * @param context 上下文
     * @param table   数据库表名
     * @return SQLite查询指针Cursor
     */
    public static Cursor query(Context context, String table) {
        return new SQLite(context, database, table, SQLite.QUERY).query();
    }

    /**
     * 通过id查询图集信息
     *
     * @param context 上下文
     * @param aid     图集id
     * @return 图集信息
     */
    public static Album getAlbumByAid(Context context, int aid) {
        Cursor cursor = new SQLite(context, database, albumListTableName, SQLite.QUERY).query("aid=?", String.valueOf(aid));
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

            Cursor imageCursor = query(context, albumChildListTableName);
            List<String> images = new ArrayList<>();
            while (imageCursor.moveToNext()) {
                String uri = imageCursor.getString(imageCursor.getColumnIndex("uri"));
                int _aid = imageCursor.getInt(imageCursor.getColumnIndex("aid"));
                if (aid == _aid) {
                    images.add(uri);
                }
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
     * 创建新的相册
     *
     * @param context 上下文
     * @param album   新建album信息
     * @return 是否创建成功
     */
    public static long addAlbum(Context context, Album album) {
        List<Album> list = queryByAlbumList(context);
        Set<String> set = new HashSet<>();
        for (Album a : list) {
            set.add(a.getName());
        }
        int count = set.size();
        set.add(album.getName());
        if (set.size() == count) {
            return -1;
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
    public static boolean delAlbum(Context context, int aid) {
        return new SQLite(context, database, albumListTableName, SQLite.UPDATE).delete("aid", aid) > 1;
    }

    /**
     * @param context 上下文
     * @return 获取相册列表
     */
    public static List<Album> queryByAlbumList(Context context) {
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

            Cursor imageCursor = query(context, albumChildListTableName);
            List<String> images = new ArrayList<>();
            while (imageCursor.moveToNext()) {
                String uri = imageCursor.getString(imageCursor.getColumnIndex("uri"));
                int _aid = imageCursor.getInt(imageCursor.getColumnIndex("aid"));
                if (aid == _aid) {
                    images.add(uri);
                }
            }
            if (path != null && !path.isEmpty()) {
                images.addAll(FileUtil.getFilesByEndName(path, App.supportFormat));
            }
            album.setDesktops(images);
            list.add(album);
        }
        cursor.close();
        return list;
    }
}
