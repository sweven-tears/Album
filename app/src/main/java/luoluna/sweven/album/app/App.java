package luoluna.sweven.album.app;

import android.app.Application;

import com.sweven.helper.DatabaseHelper;

import java.util.HashSet;
import java.util.Set;

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
                    "count int default 0," +
                    "system int not null default 1," +
                    "cover varchar default ''," +
                    "primary key (aid)" +
                    ")"
    };

    public static final int BIG_ALBUM = 2;
    public static final int ROLL_ALBUM = 1;

    // 图集名称长度
    public static final int ALBUM_NAME_LENGTH = 10;

    // 配置信息
    public static boolean isFirst;
    public static int album = BIG_ALBUM;
    public static Set<String> supportFormat = new HashSet<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Setting.getInstance(this);
        DatabaseHelper.createSql = databaseSql;
        new DatabaseHelper(this, database);
        AppManager.getInstance().setAppStatus(AppStatus._LAUNCH);
    }
}
