package com.sweven.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Set;

/**
 * Created by Sweven on 2019/10/8--23:05.
 * Email: sweventears@foxmail.com
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    public static Set<String> sqlSet;

    public DatabaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    public DatabaseHelper(Context context, String name) {
        this(context, name, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase arg) {
        for (String sql : sqlSet) {
            arg.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg, int i, int i1) {

    }
}
