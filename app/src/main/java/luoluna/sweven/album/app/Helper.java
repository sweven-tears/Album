package luoluna.sweven.album.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.sweven.sqlite.SQLite;
import com.sweven.sqlite.bean.Rows;
import com.sweven.util.PreferenceUtil;

import org.greenrobot.greendao.query.WhereCondition;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import luoluna.sweven.album.entity.local.Album;
import luoluna.sweven.album.entity.local.AlbumDao;
import luoluna.sweven.album.entity.local.Image;
import luoluna.sweven.album.manager.FileManager;
import luoluna.sweven.album.repository.local.DaoManager;

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
    @Deprecated
    public int getNextAlbumId(Context context) {
        int nextAlbumId = 0;

        Rows column = SQLite.with(context)
                .readTable(albumListTableName)
                .orderBy("aid desc")
                .query();
        if (column.size() > 0) {
            nextAlbumId = column.getInt(0, "aid");
        }
        return nextAlbumId + 1;
    }

    public long getNextAlbumId() {
        List<Album> albums = DaoManager
                .getSession()
                .getAlbumDao()
                .queryBuilder()
                .orderAsc(AlbumDao.Properties.Id)
                .list();
        return albums.size() > 0 ? albums.get(albums.size() - 1).getId() : 1;
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
                .readTable(table)
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
                .readTable(table)
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
    @Deprecated
    public Album getAlbumByAid(Context context, long aid) {
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
            List<Image> images = new ArrayList<>();
            for (int i = 0; i < rows1.size(); i++) {
                Image desktop = new Image();
                desktop.setId(rows1.getLong(i, "id"));
                desktop.setAid(rows1.getLong(i, "aid"));
                desktop.setUri(rows1.getString(i, "uri"));
                images.add(desktop);

            }
            if (path != null && !path.isEmpty()) {
                images.addAll(FileManager.sort(new File(path)));
            }
            album.setDesktops(images);
            album.setCount(images.size());
            return album;
        }
        return null;
    }

    public Album getAlbumByAid(long id) {
        List<Album> albums = DaoManager
                .getSession()
                .getAlbumDao()
                .queryBuilder()
                .where(new WhereCondition.PropertyCondition(AlbumDao.Properties.Id, "=" + id))
                .list();
        if (albums.size() == 0) return null;
        return albums.get(0);
    }

    /**
     * 创建新的相册
     *
     * @param context 上下文
     * @param album   新建album信息
     * @return 是否创建成功
     */
    @Deprecated
    public long addAlbum(Context context, Album album) {
        if (Helper.hasAlbum(album)) {
            return -1;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("aid", album.getId());
        map.put("name", album.getName());
        map.put("cover", album.getCover());
        map.put("path", album.getPath());
        map.put("count", album.getCount());
        return SQLite.with(context)
                .writeTable(database, albumListTableName)
                .insert(map);
    }

    public long addAlbum(Album album) {
        if (hasAlbum(album)) return -1;
        DaoManager.getSession().getAlbumDao().insert(album);
        return hasAlbum(album) ? 1 : 0;
    }

    public static boolean hasAlbum(Album album) {
        List<Album> albums = DaoManager
                .getSession()
                .getAlbumDao()
                .queryBuilder()
                .where(new WhereCondition.PropertyCondition(AlbumDao.Properties.Id, "=" + album.getId()))
                .list();
        return albums.size() > 0;
    }

    /**
     * 更新图集信息
     *
     * @param context 上下文
     * @param album   album
     */
    @Deprecated
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

    public long updateAlbum(Album album) {
        DaoManager.getSession().getAlbumDao().update(album);
        return 1;
    }

    /**
     * 删除图集
     *
     * @param context 上下文
     * @param aid     相册id
     * @return 是否删除成功
     */
    @Deprecated
    public boolean delAlbum(Context context, long aid) {
        return SQLite.with(context)
                .writeTable(database, albumListTableName)
                .where("aid=?")
                .selectionArgs(aid)
                .del() > 0;
    }


    public boolean delAlbum(Album album) {
        DaoManager.getSession().getAlbumDao().deleteByKey(album.getId());
        return !hasAlbum(album);
    }


    /**
     * @param context 上下文
     * @return 获取相册列表
     */
    @Deprecated
    public List<Album> queryAlbums(Context context) {
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

            Album album = new Album(aid, name);
            album.setCount(count);
            album.setPath(path);
            album.setRemark(remark);
            album.setCover(cover);

            list.add(album);
        }
        return list;
    }

    public List<Album> queryAlbums() {
        return DaoManager.getSession().getAlbumDao().queryBuilder().list();
    }
}
