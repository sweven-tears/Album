package com.sweven.socket.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SocketClient {
    private Socket socket;
    private InetSocketAddress address;
    private int timeout;

    private List<String> msgList = new ArrayList<>();
    private IClient iClient;
    private IODeal IODeal;

    private int time = 0;
    private boolean reconnectByLostConnection = true;


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
        time = 0;
        _connect(address);
    }

    /**
     * connect service by address
     *
     * @param address address
     */
    private void _connect(SocketAddress address) {
        try {
            if (isAlive()) {
                System.err.println("already connected.");
                return;
            }
            socket = new Socket();
            socket.connect(address, timeout);
            new Thread(IODeal = new IODeal()).start();
        } catch (ConnectException e) {
            System.err.println("connect fail.");
            _reconnect(iClient.reconnectByConnect(e) - time > 0);
        } catch (IOException e) {
            throwException(e);
        }
    }

    /**
     * reconnect socket
     *
     * @param flag if need reconnect.
     */
    private void _reconnect(boolean flag) {
        try {
            if (flag) {
                Thread.sleep(2000);
                close();
                time++;
                _connect(address);
            } else {
                // reset time.
                time = 0;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * throw IOException let customer operate.
     *
     * @param e .
     */
    private void throwException(IOException e) {
        iClient.IOException(e);
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
        msgList.add(msg);
        _write();
    }

    /**
     * 突然断连，不确定当前是否还有未发消息，
     * <p>重连后优先发送标识Sign
     *
     * @param msg msg text or json
     */
    private void writeUTFInsert(String msg) {
        msgList.add(0, msg);
        _write();
    }

    /**
     * 按消息队列来，将所有消息全部发送
     */
    private void _write() {
        if (IODeal != null && msgList.size() > 0) {
            int result = IODeal.send(msgList.get(0));
            if (result == 1) {
                msgList.remove(0);
            }
            if (result < 0) return;//only result >= 0 to _write()
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    _write();
                }
            }, 500);
        } else if (IODeal == null) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    _write();
                }
            }, 500);
        }
    }

    /**
     * close socket connection
     */
    public void close() {
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
            if (IODeal != null) {
                IODeal.close();
                IODeal = null;
            }
        } catch (IOException e) {
            throwException(e);
        }
    }

    public void setReconnectByLostConnection(boolean reconnectByLostConnection) {
        this.reconnectByLostConnection = reconnectByLostConnection;
    }

    /**
     * deal socket io's read and write
     */
    private class IODeal implements Runnable {
        private DataInputStream dis;
        private DataOutputStream dos;
        private boolean run;

        private IODeal() {
            try {
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                run = true;
                iClient.onConnected();
                writeUTFInsert(iClient.onSign());
                time = 0;
            } catch (IOException e) {
                throwException(e);
            }
        }

        @Override
        public void run() {
            try {
                while (run) {
                    if (dis != null) {
                        // read msg from service
                        String msg = dis.readUTF();
                        // throw listener deal receive msg
                        if (iClient != null) {
                            iClient.readUTF(msg);
                        }
                    }
                }
            } catch (SocketException e) {
                lostConnection(e);
            } catch (IOException e) {
                throwException(e);
            }
        }

        private void close() {
            try {
                run = false;
                if (dis != null) {
                    dis.close();
                }
                if (dos != null) {
                    dos.close();
                }
            } catch (IOException e) {
                throwException(e);
            }
        }

        /**
         * send msg to service
         *
         * @param msg msg
         */
        private int send(String msg) {
            try {
                if (dos != null && run) {
                    dos.writeUTF(msg);
                    return 1;
                }
            } catch (SocketException e) {
                lostConnection(e);
                return -1;
            } catch (IOException e) {
                throwException(e);
                return -2;
            }
            return 0;
        }

        /**
         * 处理失去连接（{@link #dis}和{@link #dos}产生）的异常
         *
         * @param e .
         */
        private void lostConnection(SocketException e) {
            iClient.lostConnection(e);
            if (reconnectByLostConnection) {
                new Thread(() -> _reconnect(true)).start();
            }
        }

    }
}
