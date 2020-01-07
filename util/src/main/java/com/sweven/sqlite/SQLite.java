package com.sweven.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sweven.sqlite.execute.Read;
import com.sweven.sqlite.execute.Write;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sweven on 2019/10/8--23:10.
 * Email: sweventears@foxmail.com
 */
public class SQLite {
    @SuppressLint("StaticFieldLeak")
    private static SQLite sqLite;

    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper database_helper;
    private String databaseName;
    private String tableName;

    /**
     * start execute
     *
     * @param context 上下文
     * @return this
     */
    public static SQLite with(Context context) {
        if (sqLite == null) {
            synchronized (SQLite.class) {
                if (sqLite == null) {
                    sqLite = new SQLite();
                }
            }
        }
        sqLite.context = context;
        return sqLite;
    }

    public Read readTable(String databaseName, String tableName) {
        this.databaseName = databaseName;
        this.tableName = tableName;
        readDataBase();
        return new Read(this);
    }

    public Write writeTable(String databaseName, String tableName) {
        this.databaseName = databaseName;
        this.tableName = tableName;
        writeDataBase();
        return new Write(this);
    }


    /**
     * 创建数据库
     */
    private void readDataBase() {
        database_helper = new DatabaseHelper(context, databaseName);
        db = database_helper.getReadableDatabase();
    }


    /**
     * 更新数据库
     */
    private void writeDataBase() {
        database_helper = new DatabaseHelper(context, databaseName);
        db = database_helper.getWritableDatabase();
    }

    /**
     * @param columns       需要查询出的列，为null则为全部
     * @param selection     条件
     * @param selectionArgs 条件对应的值
     * @param groupBy       分组
     * @param having        having
     * @param orderBy       排序
     * @param limit         列数限制
     * @return cursor
     */
    public List<Map<String, String>> query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        Cursor cursor = db.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        List<Map<String, String>> maps = Helper.toMap(cursor, columns);
        close();
        return maps;
    }

    /**
     * @param nullColumnsHack default null
     * @param values          not no empty
     * @return affect columns
     */
    public long insert(String nullColumnsHack, ContentValues values) {
        long result = db.insert(tableName, nullColumnsHack, values);
        close();
        return result;
    }

    /**
     * @param values        update values
     * @param selection     where
     * @param selectionArgs whereArgs
     * @return affect columns
     */
    public int update(ContentValues values, String selection, String[] selectionArgs) {
        int result = db.update(tableName, values, selection, selectionArgs);
        close();
        return result;
    }

    /**
     * 条件删除表记录
     *
     * @param whereClause 条件
     * @param whereArgs   条件值
     * @return 影响行数
     */
    public int delete(String whereClause, String[] whereArgs) {
        int result = db.delete(tableName, whereClause, whereArgs);
        close();
        return result;
    }

    private void close() {
        try {
            if (sqLite != null) {
                sqLite.db.close();
                sqLite.database_helper.close();
                sqLite = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 内部辅助类
     */
    private static class Helper {
        private static List<Map<String, String>> toMap(Cursor cursor, String[] columns) {
            return list(cursor, columns == null ? cursor.getColumnNames() : columns);
        }

        /**
         * @param cursor  cursor
         * @param columns columns
         * @return 由cursor遍历出来的数据的集合
         */
        private static List<Map<String, String>> list(Cursor cursor, String[] columns) {
            List<Map<String, String>> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                Map<String, String> map = new HashMap<>();
                for (String column : columns) {
                    map.put(column, cursor.getString(cursor.getColumnIndex(column)));
                }
                list.add(map);
            }
            return list;
        }
    }
}
