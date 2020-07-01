package com.sweven.socket.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

public class SocketClient {
    private Socket socket;
    private InetSocketAddress address;
    private int timeout;

    private IClient iClient;
    private IODeal IODeal;

    public SocketClient() {
        timeout = 1000 * 10;
    }

    /**
     * config host,ip and connect.
     *
     * @param host host or ip or domainName
     * @param port port
     */
    public void connect(String host, int port) {
        address = new InetSocketAddress(host, port);
        System.err.println("connecting...");
        _connect(address);
    }

    /**
     * connect service by address
     *
     * @param address address
     */
    private void _connect(SocketAddress address) {
        try {
            if (isAlive()) return;
            socket = new Socket();
            socket.setKeepAlive(true);
            socket.connect(address, timeout);
            Thread thread = new Thread();
            thread.start();
            new Thread(IODeal = new IODeal()).start();
        } catch (ConnectException e) {
            System.err.println("connect fails,reconnecting...");
            try {
                Thread.sleep(2000);
                _connect(address);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * reconnect socket
     */
    private void _reconnect() {
        close();
        _connect(address);
    }

    /**
     * @return socket is alive
     */
    public boolean isAlive() {
        return socket != null && socket.isConnected();
    }

    /**
     * add listeners to deal receive msg, send sign by start
     *
     * @param iClient listener
     */
    public void addClientListener(IClient iClient) {
        this.iClient = iClient;
    }

    /**
     * send message to service
     *
     * @param msg msg text or json
     */
    public void writeUTF(String msg) {
        if (IODeal != null) {
            IODeal.send(msg);
        }
    }

    /**
     * close socket connection
     */
    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * deal socket io's read and write
     */
    private class IODeal implements Runnable {
        private DataInputStream dis;
        private DataOutputStream dos;

        private IODeal() {
            try {
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                iClient.onStart();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (isAlive()) {
                    if (dis != null) {
                        // read msg from service
                        String msg = dis.readUTF();
                        // throw listener deal receive msg
                        if (iClient != null) {
                            iClient.onReceive(msg);
                        }
                    }
                }
            } catch (SocketException e) {
                System.err.println("read error then reconnect.");
                _reconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * send msg to service
         *
         * @param msg msg
         */
        private void send(String msg) {
            try {
                if (dos != null) {
                    dos.writeUTF(msg);
                }
            } catch (IOException e) {
                System.err.println("send error then reconnect.");
                _reconnect();
            }
        }
    }
}
