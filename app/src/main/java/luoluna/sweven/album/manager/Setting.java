package luoluna.sweven.album.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.sweven.util.PreferenceUtil;

import java.util.HashSet;
import java.util.Set;

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
        PreferenceUtil preference = App.preference(context);
        App.album = preference.getInt("albumView", App.BIG_ALBUM);
        App.supportFormat = preference.getStringSet("supportFormat", defaultSupportFormat());
        return instance;
    }

    private static Set<String> defaultSupportFormat() {
        Set<String> set = new HashSet<>();
        set.add("jpg");
        set.add("png");
        set.add("jpeg");
        set.add("gif");
        set.add("bmp");
        return set;
    }

    public void save(Context context) {
        SharedPreferences.Editor editor = App.editor(context);
        editor.putInt("albumView", App.album);
        editor.apply();
    }
}
