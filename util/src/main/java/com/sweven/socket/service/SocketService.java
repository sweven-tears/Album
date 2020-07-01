package com.sweven.socket.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sweven on 2020/6/30--10:20.
 * Email: sweventears@foxmail.com
 */
public class SocketService {
    private ServerSocket service;
    private SocketAddress address;
    private int backlog;// 最大挂起连接数
    private boolean alive;
    private Map<Integer, IODeal> clientMap = new HashMap<>();

    private IService iService;

    private SocketService(String hostname, int port, int backlog, IService iService) {
        address = new InetSocketAddress(hostname, port);
        this.backlog = backlog;
        this.iService = iService;
    }

    public SocketService() {
        this(80);
    }

    public SocketService(int port) {
        this("127.0.0.1", port);
    }

    public SocketService(String hostname) {
        this(hostname, 80);
    }

    public SocketService(String hostname, int port) {
        this(hostname, port, 50, null);
    }

    public void setMaxConnections(int backlog) {
        this.backlog = backlog;
    }

    public void addServiceListener(IService iService) {
        this.iService = iService;
    }

    //----------------------------------------------------------//

    public void start() {
        _start();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isAlive()) {
                    clientMap.clear();
                    _start();
                }
            }
        }, 1000 * 5, 1000 * 5);
    }

    private void _start() {
        try {
            service = new ServerSocket();
            service.bind(address, backlog);
            alive = true;
            new Thread(this::accept).start();
        } catch (IOException e) {
            e.printStackTrace();
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
                IODeal io = new IODeal(socket);
                // open thread to deal socket's read and write
                new Thread(io).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * send msg to all client socket by service
     *
     * @param msg msg
     */
    public void send(String msg) {
        for (Map.Entry<Integer, IODeal> entry : clientMap.entrySet()) {
            try {
                entry.getValue().write(msg);
            } catch (SocketException e) {
                System.err.println(entry.getKey() + " was offline then remove.");
                remove(entry.getKey());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param order socket序列号 {@link IODeal}中的 port=socket.getPort()
     * @param msg   msg
     * @return status code [<p>0:socket is null,<p>1:success,<p>-1:socket connection rest,<p>-2:other IOException]
     */
    public int specifySend(int order, String msg) {
        if (clientMap.get(order) == null) return 0;
        try {
            Objects.requireNonNull(clientMap.get(order)).write(msg);
        } catch (SocketException e) {
            System.err.println("client was offline then remove.");
            remove(order);
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -2;
        }
        return 1;
    }

    /**
     * remove it when socket is unconnected by port
     *
     * @param port socket.getPort()
     */
    private void remove(int port) {
        try {
            Objects.requireNonNull(clientMap.get(port)).close();
            clientMap.remove(port);
            iService.onDrops(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return all client socket's port set
     */
    public Set<Integer> getAllClient() {
        return clientMap.keySet();
    }

    /**
     * auto clear client socket when socket wasn't alive.
     */
    private void clearNotAlive() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Set<Map.Entry<Integer, IODeal>> sets = clientMap.entrySet();
                for (Map.Entry<Integer, IODeal> entry : sets) {
                    if (!entry.getValue().alive()) {
                        remove(entry.getKey());
                    }
                }
            }
        }, 1000 * 60, 100 * 60);
    }

    public boolean isAlive() {
        return alive;
    }

    /**
     * deal socket's write and read.
     */
    private class IODeal implements Runnable {
        private Socket socket;
        private DataOutputStream dos;
        private DataInputStream dis;
        private List<String> msgList = new ArrayList<>();
        private int port;

        public IODeal(Socket socket) {
            this.socket = socket;
            this.port = socket.getPort();
            try {
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
                onRead();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * create read
         */
        private void onRead() {
            clientMap.put(port, this);
            iService.onAccept(port);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (alive()) {
                        if (next()) {
                            iService.readUTF(port, read());
                        }
                    } else {
                        cancel();
                    }
                }
            }, 0, 500);
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
                System.err.println("client No." + port + " offline");
                remove(port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void write(String msg) throws SocketException, IOException {
            if (dos != null) {
                dos.writeUTF(msg);
            }
        }

        /**
         * @return read {@link #dis}.readUTF()
         */
        public String read() {
            String msg = msgList.get(0);
            msgList.remove(0);
            return msg;
        }

        /**
         * @return Is there a message queue
         */
        public boolean next() {
            return msgList.size() > 0;
        }

        /**
         * close client socket
         */
        public void close() {
            try {
                if (socket != null) {
                    socket.close();
                    socket = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * @return Is socket alive
         */
        public boolean alive() {
            return socket != null && !socket.isClosed();
        }
    }

    public static class Builder {
        private String hostname;
        private int port;
        private int backlog = 50;// 最大挂起连接数
        private IService listener;

        public Builder address() {
            return address("localhost", 80);
        }

        public Builder address(String hostname) {
            return address(hostname, 80);
        }

        public Builder address(int port) {
            return address("localhost", port);
        }


        public Builder address(String hostname, int port) {
            this.hostname = hostname;
            this.port = port;
            return this;
        }

        public Builder maxConnected(int backlog) {
            this.backlog = backlog;
            return this;
        }

        public Builder addServiceListener(IService iService) {
            this.listener = iService;
            return this;
        }

        public SocketService build() {
            SocketService socketService = new SocketService(hostname, port, backlog, listener);
            socketService.clearNotAlive();
            return socketService;
        }

    }
}
