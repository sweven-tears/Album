package com.sweven.linked;

public class Linked {
    Linked previous;
    Linked next;
    String value;

    public Linked() {
    }

    public Linked(String value) {
        this.value = value;
    }

    public void call(Linked next) {
        if (next != null) {
            next.call(next.next);
        }
    }
}
