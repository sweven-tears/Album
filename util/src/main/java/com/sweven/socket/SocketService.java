package com.sweven.socket;

import com.sweven.console.Console;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 这是一个同步的连接，需要通过回调执行其他的操作
 */
public class SocketService {

    private boolean started = false;
    private ServerSocket serverSocket;
    private List<ClientHelper> clients = new ArrayList<>();

    private ServiceLaunchListener listener;

    /**
     * 这是一个同步的连接，需要通过回调执行其他的操作
     */
    public SocketService() {
        listener = new ServiceLaunchAdapter() {
        };
    }

    /**
     * 默认启动80端口
     *
     * @param adapter 连接状态反馈回调
     */
    public void start(ServiceLaunchAdapter adapter) {
        this.listener = adapter;
        _start(80);
    }

    /**
     * 默认启动80端口
     * 使用默认回调
     */
    public void start() {
        _start(80);
    }

    /**
     * 自定义端口启动
     *
     * @param port 端口号
     */
    public void start(int port) {
        _start(port);
    }

    /**
     * 自定义端口启动
     *
     * @param port 端口号
     */
    public void start(int port, ServiceLaunchAdapter adapter) {
        this.listener = adapter;
        _start(port);
    }


    /**
     * 自定义启动端口
     *
     * @param port 端口
     */
    private void _start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            started = true;
            if (listener != null) {
                listener.success();
            }
            connectClient();
        } catch (BindException e) {
            if (listener != null) {
                listener.bindException(e);
            }
        } catch (IOException e) {
            if (listener != null) {
                listener.ioException(e);
            }
        }
    }

    private void connectClient() throws IOException {
        try {
            while (started) {
                Socket socket = serverSocket.accept();
                ClientHelper clientHelper = new ClientHelper(socket);
                clients.add(clientHelper);
                new Thread(clientHelper).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (serverSocket != null) {
                serverSocket.close();
            }
            started = false;
        }
    }

    public void sendAll(String msg) {
        for (ClientHelper helper : clients) {
            helper.send(msg);
        }
    }

    public boolean send(String sign, String msg) {
        for (ClientHelper helper : clients) {
            if (helper.getSign().equals(sign)) {
                helper.send(msg);
                return true;
            }
        }
        return false;
    }

    public void showClientList() {
        if (clients.size() == 0) {
            Console.log("null");
            return;
        }
        for (ClientHelper client : clients) {
            Console.log(client.getPort(), client.getId(), client.getSign());
        }
    }

    public static abstract class ServiceLaunchAdapter implements ServiceLaunchListener {
        /**
         * 成功启动
         */
        @Override
        public void success() {
            Console.log("service launching...");
        }

        /**
         * 读写异常
         *
         * @param e exception
         */
        @Override
        public void ioException(IOException e) {
            Console.err(e.getMessage());
        }

        /**
         * 端口被占用异常
         *
         * @param e exception
         */
        @Override
        public void bindException(BindException e) {
            Console.err("端口被占用中...");
            Console.err("请关闭相关程序或者切换端口");
            System.exit(0);
        }
    }

    public interface ServiceLaunchListener {
        /**
         * 成功启动
         */
        void success();


        /**
         * 读写异常
         *
         * @param e exception
         */
        void ioException(IOException e);

        /**
         * 端口被占用异常
         *
         * @param e exception
         */
        void bindException(BindException e);
    }
}
