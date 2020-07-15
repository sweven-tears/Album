package luoluna.sweven.album.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.sweven.util.PreferenceUtil;

import java.util.HashSet;
import java.util.Set;

import luoluna.sweven.album.app.App;
import luoluna.sweven.album.app.Helper;

/**
 * Created by Sweven on 2019/9/10--17:11.
 * Email: sweventears@foxmail.com
 */
public class Setting {
    private static Setting instance;

    public static Setting getInstance() {
        if (instance == null) {
            synchronized (Setting.class) {
                if (instance == null) {
                    instance = new Setting();
                }
            }
        }
        return instance;
    }

    public static Setting getInstance(Context context) {
        if (instance == null) {
            synchronized (Setting.class) {
                if (instance == null) {
                    instance = new Setting();
                }
            }
        }
        PreferenceUtil preference = Helper.preference(context);
        App.album = preference.getInt("albumView", App.BIG_ALBUM);
        App.supportFormat = preference.getStringSet("supportFormat", defaultSupportFormat());
        App.isFirst = preference.getBoolean("isFirst", true);
        return instance;
    }

    private static Set<String> defaultSupportFormat() {
        Set<String> set = new HashSet<>();
        set.add("jpg");
        set.add("png");
        set.add("jpeg");
        set.add("gif");
        return set;
    }

    public void nonFirst(Context context) {
        App.isFirst = false;
        save(context);
    }

    public void save(Context context) {
        SharedPreferences.Editor editor = Helper.editor(context);
        editor.putInt("albumView", App.album);
        editor.putStringSet("supportFormat", App.supportFormat);
        editor.putBoolean("isFirst", App.isFirst);
        editor.apply();
    }
}
