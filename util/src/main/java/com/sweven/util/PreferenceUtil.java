package com.sweven.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.sweven.console.LogUtil;

import java.util.Map;
import java.util.Set;

/**
 * Created by Sweven on 2019/4/2.
 * Email:sweventears@Foxmail.com
 */
public class PreferenceUtil {

    private static final String TAG = PreferenceUtil.class.getName();
    private Context context;
    private SharedPreferences preferences;
    private String fileName;

    public PreferenceUtil(Context context, String name) {
        this.context = context;
        this.fileName = name;
        preferences = this.context.getSharedPreferences(name, Context.MODE_PRIVATE);
        new LogUtil(TAG).i("查询" + name + ".xml文件");
    }

    public Editor getEditor() {
        return preferences.edit();
    }

    /**
     * save preference
     *
     * @param key
     * @param o
     */
    public void save(String key, Object o) {
        Editor editor = getEditor();
        if (o instanceof String) {
            editor.putString(key, (String) o);
        } else if (o instanceof Integer) {
            editor.putInt(key, (int) o);
        } else if (o instanceof Float) {
            editor.putFloat(key, (float) o);
        } else if (o instanceof Boolean) {
            editor.putBoolean(key, (boolean) o);
        } else if (o instanceof Long) {
            editor.putLong(key, (long) o);
        }
        editor.apply();
    }

    /**
     * save preference
     *
     * @param key
     * @param stringSet
     */
    public void save(String key, Set<String> stringSet) {
        Editor editor = getEditor();
        editor.putStringSet(key, stringSet).apply();
    }

    /**
     * @param key
     * @return default null
     */
    public String getString(String key) {
        new LogUtil(TAG).i("获取" + fileName + ".xml文件key为" + key + "的值");
        Map<String, ?> map = preferences.getAll();
        String f = (String) map.get(key);
        return f;
    }

    /**
     * @param key
     * @return default 0
     */
    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    /**
     * @param key
     * @return default 0
     */
    public float getFloat(String key) {
        return preferences.getFloat(key, 0);
    }

    /**
     * @param key
     * @return default false
     */
    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    /**
     * @param key
     * @return default 0
     */
    public long getLong(String key) {
        return preferences.getLong(key, 0);
    }

    /**
     * @param key
     * @return default null
     */
    public Set<String> getStringSet(String key) {
        return preferences.getStringSet(key, null);
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        return preferences.getStringSet(key, defValue);
    }

    public Map<String, ?> getAll() {
        return preferences.getAll();
    }

    @SuppressLint("CommitPrefEdits")
    public void remove(String[] name) {
        Editor editor = preferences.edit();
        for (String aName : name) {
            editor.remove(aName);
        }
        editor.apply();
    }
}
