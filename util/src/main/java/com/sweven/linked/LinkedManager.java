package com.sweven.linked;

import android.util.Log;

import java.util.List;

public class LinkedManager {

    protected Linked linked;
    protected Linked head;

    public LinkedManager() {
        this.linked = new Linked();
        this.head = this.linked;
    }

    public LinkedManager(Linked head) {
        this.linked = head;
        this.head = linked;
    }


    public LinkedManager append(Linked linked) {
        this.linked.next = linked;
        this.linked = this.linked.next;
        return this;
    }

    public LinkedManager appendArray(List<Linked> list) {
        for (Linked linked : list) {
            this.linked.next = linked;
            this.linked = this.linked.next;
        }
        return this;
    }

    public void call() {
        if (head == null) {
            Log.e("LinkedManager.class", "链表为空");
            return;
        }
        head.execute();
    }
}
