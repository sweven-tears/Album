package com.sweven.socket;

import com.sweven.socket.client.IClient;
import com.sweven.socket.client.SocketClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;

/**
 * <p>Create by Sweven on 2020/7/3 -- 16:44</p>
 * Email: sweventears@163.com
 */
public class ClientManager implements IClient {
    private SocketClient client;

    public ClientManager() {
        client = new SocketClient();
        client.addClientListener(this);
        client.connect("192.168.2.13", 5477);
    }

    public ClientManager(int max) {
        client = new SocketClient();
        client.addClientListener(this);
        client.connect("192.168.2.13", 5477 + max);
    }

    @Override
    public void onConnected() {
        System.out.println("connected.");
    }

    @Override
    public void readUTF(String msg) {
        try {
            JSONObject object = new JSONObject(msg);
            String url = object.getString("url");
            JSONObject data = object.optJSONObject("data");
            dispatch(url, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void dispatch(String url, JSONObject data) {
        switch (url) {
            case "/user/rename":
                boolean rename = data.optBoolean("rename");
                System.out.println("rename " + (rename ? "success" : "fail") + ".");
                break;
            case "/user/chat":
                String from = data.optString("from");
                String msg = data.optString("msg");
                System.out.println(from + " send message:" + msg);
                break;
        }
    }

    public void send(String data) {
        String[] arr = data.split(" ");
        if (arr.length == 0 || arr[0].trim().equals("")) System.err.println("print null");
        switch (arr[0]) {
            case "rename":
                if (arr[1].equals("")) {
                    System.err.println("name cannot be empty!");
                    System.err.println("example:rename 'name'");
                    break;
                }
                client.writeUTF(toJson("/user/rename", "\"rename\":\"" + arr[1]+"\""));
                break;
            case "chat":
                if (arr.length != 3) {
                    System.err.println("format is error");
                    System.err.println("example:chat -name message");
                    break;
                }
                String other = arr[1].substring(1);
                client.writeUTF(toJson("/user/chat", "\"chatWith\":\"" + other + "\",\"msg\":" + arr[2]));
        }
    }

    private String toJson(String url, String data) {
        return "{\"url\":\"" + url + "\"," +
                "\"data\":{" + data + "}}";
    }

    @Override
    public int reconnectByConnect(ConnectException e) {
        return 5;
    }
}
