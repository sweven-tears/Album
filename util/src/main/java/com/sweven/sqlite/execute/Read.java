package com.sweven.sqlite.execute;

import com.sweven.sqlite.SQLite;
import com.sweven.sqlite.bean.Rows;

/**
 * Created by Sweven on 2019/10/8--23:36.
 * Email: sweventears@foxmail.com
 */
public class Read extends Execute {
    private String groupBy;
    private String having;
    private String orderBy;
    private String limit;

    public Read(SQLite sqLite) {
        super(sqLite);
    }

    /**
     * 添加条件语句
     */
    public WhereArgs<Read> where(String where) {
        this.where = where;
        return new WhereArgs<>(this);
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

    public Rows query() {
        return new Rows(sqLite.query(columns, where, whereArgs, groupBy, having, orderBy, limit));
    }
}
