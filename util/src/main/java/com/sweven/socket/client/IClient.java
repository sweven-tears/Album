package com.sweven.socket.client;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;

public interface IClient {
    /**
     * After successful connection
     */
    default void onConnected() {
        System.out.println("connected.");
    }

    /**
     * read msg from service
     *
     * @param msg msg
     */
    default void readUTF(String msg) {
        System.out.println("readUTF:" + msg);
    }

    /**
     * Do you want to reconnect <p>when a connection exception is thrown during connection.
     * <p>when number <= 0 mean to don't reconnect
     *
     * @param e .
     * @return default 2<p>Number of reconnection after connection failure
     */
    default int reconnectByConnect(ConnectException e) {
        return 2;
    }

    /**
     * IOException that may occur at any time
     *
     * @param e .
     */
    default void IOException(IOException e) {
        e.printStackTrace();
    }

    /**
     * socket connect rest.
     *
     * @param e .
     */
    default void lostConnection(SocketException e) {
        System.err.println("lost connect.");
    }
}
