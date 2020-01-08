package com.sweven.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ClientHelper implements Runnable {
    private Client client = new Client();
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private boolean connected = false;

    public ClientHelper(Socket socket) {
        this.client.setSocket(socket);
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
            System.err.println(client.getId() + " already disconnected.");
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
            }
        } catch (SocketException ignore) {
            App.log.e("this client " + client.getId() + "'s exit. ");
        } catch (IOException e) {
            App.log.e(e.getMessage());
        } finally {
            try {
                connected = false;
                if (dis != null) {
                    dis.close();
                }
                if (dos != null) {
                    dos.close();
                }
                if (client.getSocket() != null) {
                    client.getSocket().close();
                }
                new Thread(this::remove).start();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }



    public String read(String msg) {
        return msg;
    }

    private void remove() {

    }

    public Client getClient(){
        return client;
    }

    public int getId(){
        return client.getId();
    }

    public String getSign(){
        return client.getSign();
    }

    public Socket getSocket(){
        return client.getSocket();
    }

    public int getPort(){
        return getSocket().getPort();
    }
}
