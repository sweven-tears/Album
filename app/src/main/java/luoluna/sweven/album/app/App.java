package luoluna.sweven.album.app;

import android.app.Application;

import com.sweven.page.Page;
import com.sweven.sqlite.DatabaseHelper;

import java.util.HashSet;
import java.util.Set;

import luoluna.sweven.album.manager.Setting;

/**
 * Created by Sweven on 2019/9/10--16:53.
 * Email: sweventears@foxmail.com
 */
public class App extends Application {

    public static App application;

    public static final String appName = "Album";
    public static final String preference = "album";
    public static final String database = "album";

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
        application = this;
        Setting.getInstance(this);
        AppManager.getInstance().setAppStatus(AppStatus._LAUNCH);
        new Page(this)
                .setPackage("luoluna.sweven.album")
                .addPage("main", "MainActivity")
                .addPage("launch", "page.LaunchActivity")
                .addPage("setting", "page.AlbumSettingActivity")
                .addPage("picture", "page.PictureActivity")
                .addPage("launch", "page.LaunchActivity")
                .addPage("look", "page.PictureLookActivity");
    }
}
