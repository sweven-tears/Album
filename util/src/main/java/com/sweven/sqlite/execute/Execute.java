package com.sweven.sqlite.execute;

import com.sweven.sqlite.SQLite;

class Execute {
    SQLite sqLite;
    String[] columns;
    String where;
    String[] whereArgs;

    Execute(SQLite sqLite) {
        this.sqLite = sqLite;
    }

    /**
     * 添加要查询的列
     *
     * @param columns 列
     */
    public Execute columns(String... columns) {
        this.columns = columns;
        return this;
    }

    /**
     * 添加条件
     */
    void setWhereArgs(String[] whereArgs) {
        this.whereArgs = whereArgs;
    }
}
