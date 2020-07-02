package com.sweven.socket.service;

public interface IService {
    /**
     * connected success.
     */
    void onConnected();

    /**
     * get client send msg and then deal this.
     *
     * @param port socket port
     * @param msg  msg
     */
    void readUTF(int port, String msg);

    /**
     * @param port client socket's port.
     */
    void onAccept(int port);

    /**
     * this socket connection reset.
     *
     * @param port socket port
     */
    void onDrops(int port);
}
