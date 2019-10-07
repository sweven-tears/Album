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
    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper database_helper;
    private String dataBaseName;
    private String tableName;


    /**
     * @param context      上下文
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
     * 适用于与rawQuery()一起用
     *
     * @param db      库名
     * @param context 上下文
     */
    public SQLite(SQLiteDatabase db, Context context) {
        this.db = db;
        this.context = context;
        updateDataBase();
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


    /**
     * 向表中插入数据
     *
     * @param map 键值对 table列名--值
     * @return 影响行数
     */
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

    /**
     * @param map         键值对 table列名--值
     * @param whereClause 条件语句 例如：id=?,name=?
     * @param whereArgs   条件值 例如：1,name
     * @return 影响行数
     */
    public int update(Map<String, Object> map, String whereClause, String... whereArgs) {
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
        return db.update(tableName, values, whereClause, whereArgs);
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
    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return db.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * @param columns       需要查询出的列，为null则为全部
     * @param selection     条件
     * @param selectionArgs 条件对应的值
     * @param groupBy       分组
     * @param having        having
     * @param orderBy       排序
     * @return cursor
     */
    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return db.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, null);
    }

    /**
     * @param columns       需要查询出的列，为null则为全部
     * @param selection     条件
     * @param selectionArgs 条件对应的值
     * @return cursor
     */
    public Cursor query(String[] columns, String selection, String[] selectionArgs) {
        return db.query(tableName, columns, selection, selectionArgs, null, null, null);
    }

    /**
     * @param columns       需要查询出的列，为null则为全部
     * @param selection     条件
     * @param selectionArgs 条件对应的值
     * @param orderBy       排序
     * @return cursor
     */
    public Cursor query(String[] columns, String selection, String[] selectionArgs, String orderBy) {
        return db.query(tableName, columns, selection, selectionArgs, null, null, orderBy);
    }

    /**
     * 查询一张表
     *
     * @param selection     条件
     * @param selectionArgs 条件对应的值
     * @return cursor
     */
    public Cursor query(String selection, String[] selectionArgs) {
        return db.query(tableName, null, selection, selectionArgs, null, null, null);
    }

    /**
     * 按一定顺序完整查询一张表
     *
     * @param orderBy 排序
     * @return cursor
     */
    public Cursor query(String orderBy) {
        return db.query(tableName, null, null, null, null, null, orderBy);
    }

    /**
     * 不带条件查询整张表
     *
     * @return cursor
     */
    public Cursor query() {
        return db.query(tableName, null, null, null, null, null, null);
    }

    /**
     * 多条件删除
     *
     * @param whereClause 条件
     * @param whereArgs   条件值
     * @return 影响行数
     */
    public int delete(String whereClause, String... whereArgs) {
        return db.delete(tableName, whereClause, whereArgs);
    }

    /**
     * 单一条件删除
     *
     * @return 影响行数
     */
    public int delete(String where, Object value) {
        return db.delete(tableName, where + "=?", new String[]{String.valueOf(value)});
    }


    /**
     * @param sql SQL语句
     * @param o   '?,?,?...'参数
     * @return 结果集
     */
    public Cursor rawQuery(String sql, Object... o) {
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

    public SQLiteDatabase db() {
        return this.db;
    }
}
