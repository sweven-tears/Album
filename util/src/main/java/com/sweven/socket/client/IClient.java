package com.sweven.socket.client;

import com.sweven.socket.common.IReceive;

public interface IClient extends IReceive {
    void onStart();

    @Deprecated
    void readUTF(int no, String msg);

    void onReceive(String msg);
}
