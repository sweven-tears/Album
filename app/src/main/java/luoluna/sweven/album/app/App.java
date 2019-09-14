package luoluna.sweven.album.app;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;

import com.sweven.helper.DatabaseHelper;
import com.sweven.helper.SQLite;
import com.sweven.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

import luoluna.sweven.album.bean.Album;

/**
 * Created by Sweven on 2019/9/10--16:53.
 * Email: sweventears@foxmail.com
 */
public class App extends Application {

    public static final String preference = "album";
    public static final String database = "album";
    public static final String appName = "Album";
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
                    "count int default 0," +
                    "path varchar(80) default null," +
                    "remark varchar default ''," +
                    "primary key (aid)" +
                    ")"
    };

    public static final int BIG_ALBUM = 0x01;
    public static final int ROLL_ALBUM = 0x02;
    public static int album = BIG_ALBUM;


    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper.createSql = databaseSql;
        new DatabaseHelper(this, database);
    }

    public static Cursor query(Context context, String table) {
        return new SQLite(context, database, table, SQLite.QUERY).query();
    }

    public static List<Album> queryByAlbumList(Context context) {
        List<Album> list = new ArrayList<>();
        Cursor cursor = query(context, albumListTableName);
        while (cursor.moveToNext()) {
            int aid = cursor.getInt(cursor.getColumnIndex("aid"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int count = cursor.getInt(cursor.getColumnIndex("count"));
            String path = cursor.getString(cursor.getColumnIndex("path"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            Album album = new Album(aid, name);
            album.setCount(count);
            album.setPath(path);
            album.setRemark(remark);
            Cursor imageCursor = query(context, albumChildListTableName);
            List<String> images = new ArrayList<>();
            while (imageCursor.moveToNext()) {
                String uri = imageCursor.getString(imageCursor.getColumnIndex("uri"));
                int _aid = imageCursor.getInt(imageCursor.getColumnIndex("aid"));
                if (aid == _aid) {
                    images.add(uri);
                }
            }
            if (!path.isEmpty()) {
                images.addAll(FileUtil.getFilesByEndName(path, "jpg", "jpeg", "bmp", "png"));
            }
            album.setDesktops(images);
            list.add(album);
        }
        cursor.close();

        return list;
    }
}
