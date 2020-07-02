package com.sweven.socket.launch;

import com.sweven.socket.client.IClient;
import com.sweven.socket.client.SocketClient;

import java.util.Scanner;

public class ClientRun implements IClient {
    private SocketClient client;

    public static void main(String[] args) {
        new ClientRun();
    }

    public ClientRun() {
        client = new SocketClient();
        client.addClientListener(this);
        client.connect("192.168.2.13", 8080);
        Scanner scanner = new Scanner(System.in);
        String arg = scanner.nextLine();
        while (!arg.equals("exit")) {
            if (arg.equals("/help")) {
                System.out.println("rename@ 改名");
                System.out.println("nearby@ 查找网络上的人");
                System.out.println("forward@name@ 给昵称为name的人发送消息");
            } else if (arg.equals("/connect")) {
                client.connect("192.168.2.13", 8080);
            } else {
                client.writeUTF(arg);
            }
            arg = scanner.nextLine();
        }
        client.close();
        scanner.close();
    }

    @Override
    public String onSign() {
        return "rename@007";
    }

    @Override
    public void readUTF(String msg) {
        if (msg.startsWith("rename^")) {
            System.out.println(msg.substring("rename^".length()));
        } else if (msg.startsWith("nearby^")) {
            String array = msg.substring("nearby^".length()).trim();
            System.out.println(array);
        } else if (msg.startsWith("forward^")) {
            String temp = msg.substring("forward^".length());
            int index = temp.indexOf("^");
            String name = temp.substring(0, index);
            String message = temp.substring(index + 1);
            System.out.println("receive msg from " + name + ": " + message);
        }
    }
}
