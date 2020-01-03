package com.sweven.linked;

public class Linked implements LinkedCallBack {
    int index;
    String value;
    Linked next;


    public Linked() {
    }

    public Linked(String value) {
        this.value = value;
    }

    @Override
    public void execute() {
        if (next != null) {
            next.execute();
        }
    }
}
