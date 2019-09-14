package luoluna.sweven.album.manager;

import android.content.Context;

import com.sweven.util.PreferenceUtil;

import luoluna.sweven.album.app.App;

/**
 * Created by Sweven on 2019/9/10--17:11.
 * Email: sweventears@foxmail.com
 */
public class Setting {
    private static Setting instance;

    public static Setting getInstance() {
        if (instance == null) {
            synchronized (Setting.class) {
                instance = new Setting();
            }
        }
        return instance;
    }

    public static Setting getInstance(Context context) {
        if (instance == null) {
            synchronized (Setting.class) {
                instance = new Setting();
            }
        }
        PreferenceUtil preference = new PreferenceUtil(context, App.preference);
        App.album = preference.getInt("albumView", App.BIG_ALBUM);
        return instance;
    }
}
