package com.sweven.sqlite.execute;

import android.content.ContentValues;

import com.sweven.sqlite.SQLite;

import java.util.Map;

/**
 * Created by Sweven on 2019/10/8--23:36.
 * Email: sweventears@foxmail.com
 */
public class Write extends Execute {

    private String nullColumnsHack;


    public Write(SQLite sqLite) {
        super(sqLite);
    }

    /**
     * 添加条件语句
     */
    public WhereArgs<Write> where(String where) {
        this.where = where;
        return new WhereArgs<>(this);
    }

    /**
     * @param nullColumnsHack default null
     */
    public Write nullColumnsHack(String nullColumnsHack) {
        this.nullColumnsHack = nullColumnsHack;
        return this;
    }

    /**
     * 插入表记录
     *
     * @param values content
     * @return 影响行数
     */
    public long insert(ContentValues values) {
        return sqLite.insert(nullColumnsHack, values);
    }

    /**
     * 更新表记录
     *
     * @param values content
     * @return 影响行数
     */
    public int update(ContentValues values) {
        return sqLite.update(values, where, whereArgs);
    }

    /**
     * 插入表记录
     *
     * @param map map
     * @return 影响行数
     */
    public long insert(Map<String, Object> map) {
        ContentValues values = new ContentValues();
        for (String key : map.keySet()) {
            Object o = map.get(key);
            if (o == null) {
                values.putNull(key);//注意值的类型要匹配
            } else {
                String className = o.getClass().getSimpleName();
                switch (className) {
                    case "Long":
                        values.put(key, (Long) map.get(key));
                        break;
                    case "Float":
                        values.put(key, (Float) map.get(key));
                        break;
                    case "Short":
                        values.put(key, (Short) map.get(key));
                        break;
                    case "Byte[]":
                        values.put(key, (byte[]) map.get(key));
                        break;
                    case "Double":
                        values.put(key, (Double) map.get(key));
                        break;
                    case "String":
                        values.put(key, (String) map.get(key));
                        break;
                    case "Boolean":
                        values.put(key, (Boolean) map.get(key));
                        break;
                    case "Integer":
                        values.put(key, (Integer) map.get(key));
                        break;
                }
            }

        }
        return sqLite.insert(nullColumnsHack, values);
    }

    /**
     * 更新表记录
     *
     * @param map map
     * @return 影响行数
     */
    public int update(Map<String, Object> map) {
        ContentValues values = new ContentValues();
        for (String key : map.keySet()) {
            Object o = map.get(key);
            if (o == null) {
                values.putNull(key);//注意值的类型要匹配
            } else {
                String className = o.getClass().getSimpleName();
                switch (className) {
                    case "Long":
                        values.put(key, (Long) map.get(key));
                        break;
                    case "Float":
                        values.put(key, (Float) map.get(key));
                        break;
                    case "Short":
                        values.put(key, (Short) map.get(key));
                        break;
                    case "Byte[]":
                        values.put(key, (byte[]) map.get(key));
                        break;
                    case "Double":
                        values.put(key, (Double) map.get(key));
                        break;
                    case "String":
                        values.put(key, (String) map.get(key));
                        break;
                    case "Boolean":
                        values.put(key, (Boolean) map.get(key));
                        break;
                    case "Integer":
                        values.put(key, (Integer) map.get(key));
                        break;
                }
            }

        }
        return sqLite.update(values, where, whereArgs);
    }

    /**
     * 删除表记录
     *
     * @return 影响行数
     */
    public int del() {
        return sqLite.delete(where, whereArgs);
    }
}
