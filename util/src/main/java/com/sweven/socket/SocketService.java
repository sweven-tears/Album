package com.sweven.socket;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.sweven.socket.App.clients;

public class SocketService {

    private boolean started = false;
    private ServerSocket serverSocket;

    public void start() {
        try {
            serverSocket = new ServerSocket(5209);
            System.out.println("service launching...");
            started = true;
        } catch (BindException e) {
            System.err.println("端口被占用中...");
            System.err.println("请关闭相关程序或者切换端口");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        connectClient();
    }

    private void connectClient() {
        try {
            while (started) {
                Socket socket = serverSocket.accept();
                Client client = new Client(socket);
                System.out.println("a client " + socket.getPort() + " is connected!");
                new Thread(client).start();
                clients.add(client);
                new Thread(new Send(clients.get(0))).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
                started = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
