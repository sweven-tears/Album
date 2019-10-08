package com.sweven.sqlite;

/**
 * Created by Sweven on 2019/10/8--23:12.
 * Email: sweventears@foxmail.com
 */
public enum ClassType {
    Int(0, "Integer"),
    String(1, "String"),
    Float(2, "Float"),
    Double(3, "Double"),
    Short(4, "Short"),
    Long(5, "Long"),
    Boolean(6, "Boolean"),
    Byte(7, "Byte[]"),

    ;
    private int index;
    private String name;

    ClassType(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public String getName(int index) {
        for (ClassType value : ClassType.values()) {
            if (value.getIndex() == index) {
                return name;
            }
        }
        return null;
    }


    public String getName() {
        return name;
    }

}