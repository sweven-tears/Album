package com.sweven.socket.service;

import com.sweven.socket.common.IReceive;

public interface IService extends IReceive {
    void readUTF(int port, String msg);

    void onAccept(int port);

    void onDrops(int no);// 掉线 即socket connection rest
}
