package com.sweven.socket;

import com.sweven.console.Console;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketService {

    private boolean started = false;
    private ServerSocket serverSocket;

    /**
     * 默认启动80端口
     */
    public void start() throws IOException {
        start(80);
    }

    /**
     * 自定义启动端口
     *
     * @param port 端口
     */
    public void start(int port) throws IOException {
        try {
            serverSocket = new ServerSocket(port);
            started = true;
        } catch (BindException e) {
            Console.err("端口被占用中...");
            Console.err("请关闭相关程序或者切换端口");
            System.exit(0);
        }

        connectClient();
    }

    private void connectClient() throws IOException {
        try {
            while (started) {
                Socket socket = serverSocket.accept();
                ClientHelper clientHelper = new ClientHelper(socket);
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
}
