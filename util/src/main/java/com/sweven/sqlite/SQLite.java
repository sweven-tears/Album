package com.sweven.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sweven.helper.DatabaseHelper;
import com.sweven.sqlite.execute.Read;
import com.sweven.sqlite.execute.Write;

/**
 * Created by Sweven on 2019/10/8--23:10.
 * Email: sweventears@foxmail.com
 */
public class SQLite {
    private static SQLite sqLite;

    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper database_helper;
    private String databaseName;
    private String tableName;

    private String[] columns;
    private String selection;
    private String[] selectionArgs;
    private String groupBy;
    private String having;
    private String orderBy;
    private String limit;


    public static SQLite with(Context context) {
        sqLite.context = context;
        return sqLite;
    }

    public Read readTable(String databaseName, String tableName) {
        sqLite.databaseName = databaseName;
        sqLite.tableName = tableName;
        readDataBase();
        return new Read(sqLite);
    }

    public Write writeTable(String databaseName, String tableName) {
        sqLite.databaseName = databaseName;
        sqLite.tableName = tableName;
        writeDataBase();
        return new Write(sqLite);
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
    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return db.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

}
