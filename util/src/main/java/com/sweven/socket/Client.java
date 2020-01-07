package com.sweven.socket;

import java.net.Socket;

/**
 * Created by Sweven on 2020/1/7--11:17.
 * Email: sweventears@foxmail.com
 */
public class Client {
    private int id; // 序列
    private String sign;// 客户端标记
    private transient Socket socket;

    public Client() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", socket=" + socket +
                '}';
    }
}
