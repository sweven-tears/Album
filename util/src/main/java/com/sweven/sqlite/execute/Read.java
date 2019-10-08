package com.sweven.sqlite.execute;

import android.database.Cursor;

import com.sweven.sqlite.SQLite;

/**
 * Created by Sweven on 2019/10/8--23:36.
 * Email: sweventears@foxmail.com
 */
public class Read {
    private SQLite sqLite;
    private String[] columns;
    private String selection;
    private String[] selectionArgs;
    private String groupBy;
    private String having;
    private String orderBy;
    private String limit;

    public Read(SQLite sqLite) {
        this.sqLite = sqLite;
    }

    /**
     * 添加要查询的列
     *
     * @param columns 列
     */
    public Read columns(String... columns) {
        this.columns = columns;
        return this;
    }

    /**
     * 添加条件语句
     */
    public SelectionArgs selection(String selection) {
        this.selection = selection;
        return new SelectionArgs(this);
    }

    /**
     * 添加条件
     */
    private void setSelectionArgs(String[] selectionArgs) {
        this.selectionArgs = selectionArgs;
    }

    /**
     * 给查询出的数据分组
     * 分组
     */
    public Read groupBy(String groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    /**
     * having
     */
    public Read having(String having) {
        this.having = having;
        return this;
    }

    /**
     * 制定排序规则
     */
    public Read orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    /**
     * 添加查询行数限制
     *
     * @param limit limit
     */
    public Read limit(String limit) {
        this.limit = limit;
        return this;
    }

    /**
     * 进行查询
     *
     * @return cursor
     */
    public Cursor query() {
        return sqLite.query(columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public class SelectionArgs {

        private Read read;

        SelectionArgs(Read read) {
            this.read = read;
        }

        /**
         * 添加条件语句对应的条件
         */
        public Read selectionArgs(Object... selectionArgs) {
            String[] args = new String[selectionArgs.length];
            for (int i = 0; i < selectionArgs.length; i++) {
                Object selectionArg = selectionArgs[i];
                args[i] = String.valueOf(selectionArg);

            }
            read.setSelectionArgs(args);
            return read;
        }

    }
}
