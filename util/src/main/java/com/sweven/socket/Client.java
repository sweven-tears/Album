package com.sweven.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import static com.sweven.socket.App.clients;

public class Client implements Runnable {
    private Socket socket;
    private int id;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private boolean connected = false;

    public Client(Socket socket) {
        this.socket = socket;
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg) {
        try {
            dos.writeUTF(msg);
        } catch (SocketException e) {
            System.err.println(id + " already disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (connected) {
                String msg = dis.readUTF();
                read(msg);
                System.out.println(socket.getPort() + ":" + msg);
            }
        } catch (SocketException ignore) {
            System.err.println("this client " + socket.getPort() + "'s exit. ");
        } catch (IOException ignore) {
            System.err.println("catch io exception. ");
        } finally {
            try {
                connected = false;
                if (dis != null) {
                    dis.close();
                }
                if (dos != null) {
                    dos.close();
                }
                if (socket != null) {
                    socket.close();
                }
                new Thread(this::remove).start();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void read(String msg) {
        if (msg.startsWith("@id/")) {
            try {
                this.id = Integer.parseInt(msg.replace("@id/", ""));

            } catch (NumberFormatException e) {
                System.err.println("id isn't number.");
            }
        } else if (msg.startsWith("@msg/")) {

        }
    }

    private void remove() {
        if (clients.size() > 0) {
            for (int i = clients.size() - 1; i >= 0; i--) {
                if (clients.get(i) == this) {
                    clients.remove(i);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Client{" +
                "socket=" + socket.getPort() +
                ", connected=" + connected +
                '}';
    }
}
