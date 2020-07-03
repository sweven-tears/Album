package com.sweven.socket.service;

import java.io.IOException;
import java.net.SocketException;

public interface IService {
    /**
     * connected success.
     */
    void onConnected();

    /**
     * @param port client socket's port.
     */
    void onAccept(int port);

    /**
     * get client send msg and then deal this.
     *
     * @param port socket port
     * @param msg  msg
     */
    default void readUTF(int port, String msg) {
        System.out.println("readUTF from " + port + ":" + msg);
    }

    /**
     * this socket connection reset.
     *
     * @param port socket port
     * @param e    .
     */
    void onDrops(int port, SocketException e);

    default void throwIOException(IOException e) {
        e.printStackTrace();
    }
}
