package com.sweven.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketClient {
    private Socket socket;
    private int id;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    private boolean connected = false;

    private Thread thread = new Thread(new ReceiveMsg());

    public SocketClient(int id) {
        this.id = id;
    }

    public void connect() {
        try {
            socket = new Socket("127.0.0.1", 5209);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            System.out.println("connected success!");
            connected = true;
            thread.start();
            send("@id/" + id);
            write();
        } catch (ConnectException e) {
            System.err.println("service wasn't launched");
            reconnect();
        } catch (UnknownHostException e) {
            System.err.println("host is null");
            System.exit(0);
        } catch (IOException ignore) {
        }
    }

    private void write() {
        Scanner scanner = new Scanner(System.in);
        String msg = scanner.nextLine();
        while (!msg.equals("exit")) {
            send("@msg/"+msg);
            msg = scanner.nextLine();
        }
        disconnect();
    }

    private void send(String msg) {
        try {
            dos.writeUTF(msg);
            dos.flush();
        } catch (SocketException e) {
            reconnect();
        } catch (IOException e) {
            System.err.println("send error." + e.getMessage());
        }
    }

    private void disconnect() {
        connected = false;
        System.out.println("exit success");
        System.exit(0);
    }

    private void reconnect() {
        System.out.println("reconnecting...");
    }

    private class ReceiveMsg implements Runnable {

        @Override
        public void run() {
            try {
                while (connected) {
                    String msg = dis.readUTF();
                    System.out.println("receive msg：" + msg);
                }
            } catch (EOFException e) {
                System.err.println("see you later.");
            } catch (SocketException e) {
                reconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
