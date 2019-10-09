package com.sweven.sqlite.execute;

public class WhereArgs<T extends Execute> {
    private T args;

    WhereArgs(T args) {
        this.args = args;
    }

    /**
     * 添加条件语句对应的条件
     */
    public T selectionArgs(Object... selectionArgs) {
        String[] args = new String[selectionArgs.length];
        for (int i = 0; i < selectionArgs.length; i++) {
            Object selectionArg = selectionArgs[i];
            args[i] = String.valueOf(selectionArg);

        }
        this.args.setWhereArgs(args);
        return this.args;
    }
}
