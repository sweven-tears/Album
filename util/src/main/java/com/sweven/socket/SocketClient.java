package com.sweven.socket;

import com.sweven.console.Console;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SocketClient {
    private Socket socket;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    private boolean connected = false;

    private String receiveMsg = "";

    private Thread thread = new Thread(new ReceiveMsg());

    private ConnectListener listener;

    public void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            Console.log("client:", "connected success!");
            connected = true;
            thread.start();
        } catch (ConnectException e) {
            Console.err("service wasn't launched");
            reconnect();
        } catch (UnknownHostException e) {
            Console.err("host is null");
            System.exit(0);
        } catch (IOException ignore) {
        }
    }

    public void connect(String host, int port, ConnectListener listener) {
        try {
            socket = new Socket(host, port);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            Console.log("client:", "connected success!");
            connected = true;
            listener.success();
            thread.start();
        } catch (ConnectException e) {
            Console.err("service wasn't launched");
            reconnect(listener);
        } catch (UnknownHostException e) {
            Console.err("host is null");
            System.exit(0);
        } catch (IOException ignore) {
        }
    }

    public void send(String msg) {
        try {
            dos.writeUTF(msg);
            dos.flush();
        } catch (SocketException e) {
            reconnect();
        } catch (IOException e) {
            Console.err("send error." + e.getMessage());
        }
    }

    private void reconnect() {
        System.out.println("reconnecting...");
        connected = false;
        if (dos != null) {
            try {
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void reconnect(ConnectListener listener) {
        System.out.println("reconnecting...");
        connected = false;
        listener.fail();
        if (dos != null) {
            try {
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return connected;
    }

    private class ReceiveMsg implements Runnable {

        @Override
        public void run() {
            try {
                while (connected) {
                    receiveMsg = dis.readUTF();
                }
            } catch (EOFException e) {
                Console.log("see you later.");
            } catch (SocketException e) {
                reconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String read() {
        String msg = receiveMsg;
        receiveMsg = "";
        return msg;
    }

    interface ConnectListener {
        void success();

        void fail();
    }
}
