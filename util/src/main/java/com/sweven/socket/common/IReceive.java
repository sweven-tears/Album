package com.sweven.socket.common;

public interface IReceive {
    void onStart();

    void readUTF(int no, String msg);
}
