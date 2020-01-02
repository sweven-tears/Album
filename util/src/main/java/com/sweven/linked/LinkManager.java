package com.sweven.linked;

import android.util.Log;

import java.util.List;

public class LinkManager {

    private Linked linked;
    private Linked head;

    public LinkManager(Linked head) {
        this.linked = head;
        this.head = linked;
    }


    public LinkManager append(Linked linked) {
        this.linked.next = linked;
        this.linked = this.linked.next;
        return this;
    }

    public LinkManager appendArray(List<Linked> list) {
        for (Linked linked : list) {
            this.linked.next = linked;
            this.linked = this.linked.next;
        }
        return this;
    }

    public void call() {
        if (head == null) {
            Log.e("LinkManager.class", "链表为空");
            return;
        }
        head.call(head.next);
    }
}
