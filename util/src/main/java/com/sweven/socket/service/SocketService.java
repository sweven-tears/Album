package com.sweven.socket.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import static java.util.Objects.requireNonNull;

/**
 * {@link #SocketService(String, int)} 通过此构造方法确定 host 和 port <p>
 * {@link #start()} 启动service<p>
 * {@link #writeUTF(String msg)} 向所有已连接上的 socket 发送消息<p>
 * {@link #writeUTF(String msg, int port)} 向指定 socket 发送消息<p>
 * {@link #getAllClient()} 获取当前连接 service 的所有 socket<p>
 * {@link #isAlive()} 获取 service 是否 alive<p>
 * {@link #close()} 关闭 service
 * <p>Created by Sweven on 2020/6/30--10:20.</p>
 * Email: sweventears@foxmail.com
 *
 * @version 1.0
 */
public class SocketService {
    private ServerSocket service;
    private SocketAddress address;
    private int backlog;// 最大挂起连接数
    private boolean alive;
    private Map<Integer, IODeal> clientMap = new HashMap<>();

    private IService iService;

    public SocketService(String hostname, int port) {
        this(hostname, port, 50, null);
    }

    public void setMaxConnections(int backlog) {
        this.backlog = backlog;
    }

    public void addServiceListener(IService iService) {
        this.iService = iService;
    }

    private SocketService(String hostname, int port, int backlog, IService iService) {
        address = new InetSocketAddress(hostname, port);
        this.backlog = backlog;
        this.iService = iService;
    }

    //---------------------------up code is configure.-------------------------------//

    public void start() {
        _start();
    }

    private void _start() {
        try {
            service = new ServerSocket();
            service.bind(address, backlog);
            alive = true;
            new Thread(this::accept).start();
            iService.onConnected();
        } catch (IOException e) {
            iService.throwIOException(e);
            if (service != null) {
                try {
                    service.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * accept client's socket
     */
    private void accept() {
        try {
            while (alive) {
                // Judging whether the number of connections has reached the maximum
                if (clientMap.size() == backlog) {
                    System.err.println("At present, " +
                            "the maximum number of connections has been reached, " +
                            "and it is impossible to continue connecting to the client.  .  .");
                    break;
                }
                Socket socket = service.accept();
                // open thread to deal socket's read and write
                new Thread(new IODeal(socket)).start();
            }
        } catch (IOException e) {
            iService.throwIOException(e);
        }
    }

    /**
     * 把消息发给所有 socket
     *
     * @param msg 消息
     * @return 发送成功数量
     */
    public int writeUTF(String msg) {
        int count = 0;
        for (Map.Entry<Integer, IODeal> entry : clientMap.entrySet()) {
            try {
                entry.getValue().write(msg);
                count++;
            } catch (SocketException e) {
                _remove(entry.getKey(), e);
            } catch (IOException e) {
                iService.throwIOException(e);
            }
        }
        return count;
    }

    /**
     * @param msg  消息
     * @param port socket.getPort()
     * @return 0 失败 1 成功 -1 connect rest -2 IOException。
     */
    public int writeUTF(String msg, int port) {
        IODeal deal = clientMap.get(port);
        if (deal == null) return 0;
        try {
            deal.write(msg);
        } catch (SocketException e) {
            _remove(port, e);
            return -1;
        } catch (NullPointerException e) {
            return -1;
        } catch (IOException e) {
            iService.throwIOException(e);
            return -2;
        }
        return 1;
    }

    /**
     * @return all client socket's port set
     */
    public Set<Integer> getAllClient() {
        return clientMap.keySet();
    }

    /**
     * Is service launching?
     *
     * @return alive status
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * stop this service.
     */
    public void close() {
        try {
            alive = false;
            for (Map.Entry<Integer, IODeal> entry : clientMap.entrySet()) {
                entry.getValue().close();
            }
            clientMap.clear();
            if (service != null) {
                service.close();
            }
        } catch (IOException e) {
            iService.throwIOException(e);
        }
    }

    /**
     * remove it when socket is unconnected by port
     *
     * @param port socket.getPort()
     * @param e    socket 断连抛出的异常
     */
    private void _remove(int port, SocketException e) {
        requireNonNull(clientMap.get(port)).close();
        clientMap.remove(port);
        iService.onDrops(port, e);
    }

    /**
     * auto clear client socket when socket wasn't alive.
     */
    @Deprecated
    private void clearNotAlive() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Set<Map.Entry<Integer, IODeal>> sets = clientMap.entrySet();
                for (Map.Entry<Integer, IODeal> entry : sets) {
                    if (!entry.getValue().alive()) {
                        _remove(entry.getKey(), new SocketException("socket is not alive."));
                    }
                }
            }
        }, 1000 * 60, 100 * 60);
    }

    /**
     * deal socket's write and read.
     */
    private class IODeal implements Runnable {
        private Socket socket;
        private DataOutputStream dos;
        private DataInputStream dis;
        private BlockingDeque<String> msgList = new LinkedBlockingDeque<>();
        private int port;

        private IODeal(Socket socket) {
            this.socket = socket;
            this.port = socket.getPort();
            try {
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
                add2Map();
            } catch (IOException e) {
                iService.throwIOException(e);
            }
        }

        /**
         * create read msg and add listener:onAccept(),readUTF()
         */
        private void add2Map() {
            clientMap.put(port, this);
            iService.onAccept(port);
//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    if (alive()) {
//                        if (next()) {
//                            iService.readUTF(port, read());
//                        }
//                    } else {
//                        cancel();
//                    }
//                }
//            }, 0, 500);
            new Thread(() -> {
                while (alive()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (next()) {
                        iService.readUTF(port, read());
                    }
                }
            }).start();
        }

        @Override
        public void run() {
            try {
                while (alive()) {
                    if (dis != null) {
                        msgList.add(dis.readUTF());
                    }
                }
            } catch (SocketException e) {
                _remove(port, e);
            } catch (IOException e) {
                iService.throwIOException(e);
            }
        }

        /**
         * write msg to client.
         *
         * @param msg msg
         * @throws SocketException e
         * @throws IOException     e
         */
        private void write(String msg) throws SocketException, IOException {
            if (dos != null) {
                dos.writeUTF(msg);
            }
        }

        /**
         * @return read {@link #dis}.readUTF()
         */
        private String read() {
            String msg = null;
            try {
                msg = msgList.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return msg;
        }

        /**
         * @return Is there a message queue
         */
        private boolean next() {
            return msgList.size() > 0;
        }

        /**
         * close client socket
         */
        private void close() {
            try {
                if (dis != null) {
                    dis.close();
                }
                if (dos != null) {
                    dos.close();
                }
                if (socket != null) {
                    socket.close();
                    socket = null;
                }
            } catch (IOException e) {
                iService.throwIOException(e);
            }
        }

        /**
         * @return Is socket alive
         */
        private boolean alive() {
            return socket != null && !socket.isClosed();
        }
    }
}
