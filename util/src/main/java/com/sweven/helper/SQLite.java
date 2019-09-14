package com.sweven.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sweven.util.LogUtil;

import java.sql.Date;
import java.util.Map;

/**
 * Created by Sweven on 2018/11/28.
 * Email:sweventears@Foxmail.com
 */
public class SQLite {

    public static final int QUERY = 1;
    public static final int UPDATE = 2;
    public SQLiteDatabase db;
    private Context context;
    private DatabaseHelper database_helper;
    private String dataBaseName;
    private String tableName;


    /**
     * @param context     上下文
     * @param dataBaseName 库名，默认 hospital.db
     * @param tableName    需要操作的表名
     * @param type         创建并读取库 or 更新库的数据
     */
    public SQLite(Context context, String dataBaseName, String tableName, int type) {
        this.context = context;

        if (dataBaseName == null || dataBaseName.equals("")) {
            throw new NullPointerException("dataBaseName cannot null or empty");
        }
        this.dataBaseName = dataBaseName;

        this.tableName = tableName;

        if (type == QUERY) {
            // 查询数据库
            queryDataBase();
        } else if (type == UPDATE) {
            // 更新数据库
            updateDataBase();
        } else {
            new LogUtil(context.getPackageName()).e("数据库参数错误！");
        }
    }

    /**
     * 创建数据库
     */
    private void queryDataBase() {
        database_helper = new DatabaseHelper(context, dataBaseName);
        //只有调用getReadableDatabase()或者getWriteableDatabase()函数后才能返回一个SQLiteDatabase对象
        db = database_helper.getReadableDatabase();
    }


    /**
     * 更新数据库
     */
    private void updateDataBase() {
        database_helper = new DatabaseHelper(context, dataBaseName);
        db = database_helper.getWritableDatabase();
    }


    public long insert(Map<String, Object> map) {
        // 生成contentValues对象，该对象用来存数据的
        ContentValues values = new ContentValues();
        for (String key : map.keySet()) {
            Object o = map.get(key);
            if (o == null) {
                values.putNull(key);//注意值的类型要匹配
            } else if (o instanceof Byte) {
                values.put(key, (Byte) map.get(key));
            } else if (o instanceof Long) {
                values.put(key, (Long) map.get(key));
            } else if (o instanceof Float) {
                values.put(key, (Float) map.get(key));
            } else if (o instanceof Short) {
                values.put(key, (Short) map.get(key));
            } else if (o instanceof byte[]) {
                values.put(key, (byte[]) map.get(key));
            } else if (o instanceof Double) {
                values.put(key, (Double) map.get(key));
            } else if (o instanceof String) {
                values.put(key, (String) map.get(key));
            } else if (o instanceof Boolean) {
                values.put(key, (Boolean) map.get(key));
            } else if (o instanceof Integer) {
                values.put(key, (Integer) map.get(key));
            } else if (o instanceof Date) {
                String date = map.get(key).toString();
                values.put(key, date);
            }

        }
        return db.insert(tableName, null, values);
    }

    public int update(Map<String, Object> map, String whereClause, String[] whereArgs) {
        ContentValues values = new ContentValues();
        for (String key : map.keySet()) {
            Object o = map.get(key);
            if (o == null) {
                values.putNull(key);//注意值的类型要匹配
            } else if (o instanceof Byte) {
                values.put(key, (Byte) map.get(key));
            } else if (o instanceof Long) {
                values.put(key, (Long) map.get(key));
            } else if (o instanceof Float) {
                values.put(key, (Float) map.get(key));
            } else if (o instanceof Short) {
                values.put(key, (Short) map.get(key));
            } else if (o instanceof byte[]) {
                values.put(key, (byte[]) map.get(key));
            } else if (o instanceof Double) {
                values.put(key, (Double) map.get(key));
            } else if (o instanceof String) {
                values.put(key, (String) map.get(key));
            } else if (o instanceof Boolean) {
                values.put(key, (Boolean) map.get(key));
            } else if (o instanceof Integer) {
                values.put(key, (Integer) map.get(key));
            } else if (o instanceof Date) {
                String date = map.get(key).toString();
                values.put(key, date);
            }

        }
        //参数1为表名，参数2为更新后的值，参数3表示满足条件的列名称 例:id=?，参数4为该列名下的值
        return db.update(tableName, values, whereClause, whereArgs);
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs) {
        //查询的语法，参数1为表名；参数2为表中的列名；参数3为要查询的列名；参数4为对应列的值；该函数返回的是一个游标
//        Cursor cursor = db.query(tableName, new String[]{"id", "name"}, "id=?", new String[]{"1"}, null, null, null);
        Cursor cursor = db.query(tableName, columns, selection, selectionArgs, null, null, null);
        //遍历每一个记录
//        while (cursor.moveToNext()) {
//            String value = cursor.getString(cursor.getColumnIndex(columns[0]));//返回列名为 columns[1] 的值
//            System.out.println("query---->" + value);
//        }
        return cursor;
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs,String orderBy) {
        return db.query(tableName, columns, selection, selectionArgs, null, null, orderBy);
    }

    public Cursor query() {
        return db.query(tableName, null, null, null, null, null, null);
    }

    public int delete(String whereClause, String[] whereArgs) {
        //直接删除 whereClause(例：name=?) 为 whereArgs[0] 对应的那条记录
        return db.delete(tableName, whereClause, whereArgs);
    }

    /**
     * @param sql SQL语句
     * @param o   '?,?,?...'参数
     * @return 结果集
     */
    public Cursor rawQuery(String sql, Object o[]) {
        if (o != null) {
            String[] selectionArgs = new String[o.length];
            for (int i = 0; i < o.length; i++) {
                selectionArgs[i] = String.valueOf(o[i]);
            }
            return db.rawQuery(sql, selectionArgs);
        }
        return db.rawQuery(sql, null);
    }

    /**
     * 直接执行sql语句，无返回数据
     * 适用于删除、更新、插入语句
     *
     * @param sql sql语句
     */
    public void exec(String sql) {
        db.execSQL(sql);
    }

    public SQLiteDatabase db(){
        return this.db;
    }
}
