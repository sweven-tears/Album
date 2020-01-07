package com.sweven.socket;

import org.json.JSONException;
import org.json.JSONObject;

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
    private String account;
    private int id;
    private DataOutputStream dos = null;
    private DataInputStream dis = null;
    private boolean connected = false;

    private Thread thread = new Thread(new ReceiveMsg());

    public SocketClient(String account) {
        this.account = account;
    }

    public void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            System.out.println("connected success!");
            connected = true;
            thread.start();
            JSONObject object = new JSONObject();
            object.put("account", account);
            sendConfig(object);
            write();
        } catch (ConnectException e) {
            System.err.println("service wasn't launched");
            reconnect();
        } catch (UnknownHostException e) {
            System.err.println("host is null");
            System.exit(0);
        } catch (IOException ignore) {
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void write() {
        Scanner scanner = new Scanner(System.in);
        String msg = scanner.nextLine();
        while (!msg.equals("exit")) {
            send(msg);
            msg = scanner.nextLine();
        }
        disconnect();
    }

    private void sendConfig(JSONObject config) {
        _send("@config/" + config.toString());
    }

    private void send(String msg) {
        JSONObject object = new JSONObject();
        try {
            object.put("account", account)
                    .put("receive_account", account)
                    .put("msg", msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        _send("@msg/" + msg);
    }

    private void _send(String msg) {
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
        connected = false;
        if (dos != null) {
            try {
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ReceiveMsg implements Runnable {

        @Override
        public void run() {
            try {
                while (connected) {
                    String msg = dis.readUTF();
                    read(msg);
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

    private void read(String msg) {
        System.out.println("receive msg:" + msg);
//        if (msg.startsWith("@config/")){
//            String config = msg.substring("@config/".length());
//            try {
//                JSONObject object = new JSONObject(config);
//                id = object.optInt("connect_id");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }else if (msg.startsWith("@msg/")){
//
//        }
    }

}
