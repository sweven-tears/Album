package com.sweven.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.sweven.socket.service.IService;
import com.sweven.socket.service.SocketService;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Create by Sweven on 2020/7/3 -- 14:03</p>
 * Email: sweventears@163.com
 */
public class ServiceManager implements IService {
    private SocketService service;
    private List<Person> people = new ArrayList<>();

    public ServiceManager() {
        service = new SocketService("192.168.2.13", 5477);
        service.addServiceListener(this);
        service.setMaxConnections(100);
        service.start();
    }

    @Override
    public void onConnected() {
        System.out.println("service launching......");
    }

    @Override
    public void onAccept(int port) {
        people.add(new Person(port, "person" + port));
        System.out.println(people.get(people.size() - 1).name + " online.");
    }

    @Override
    public void readUTF(int port, String msg) {
        IService.super.readUTF(port, msg);
        //{url:"",data:{}}
        try {
            JSONObject object = JSON.parseObject(msg);
            String url = object.getString("url");
            JSONObject data = object.getJSONObject("data");
            read(url, data, port);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void read(String url, JSONObject data, int port) {
        switch (url) {
            case "/user/rename":
                String rename = data.getString("rename");
                rename(port, rename);
                break;
            case "/user/chat":
                String other = data.getString("chatWith");
                String msg = data.getString("msg");
                chat(port, msg, other);
                break;
        }
    }

    private void chat(int port, String msg, String other) {
        int otherPort = getPortByName(other);
        String name = getNameByPort(port);
        service.writeUTF(toJson("/user/chat", "\"from\":\"" + name + "\",\"msg\":\"" + msg + "\""), otherPort);
    }

    private String getNameByPort(int port) {
        for (Person person : people) {
            if (person.port == port) {
                return person.name;
            }
        }
        return null;
    }

    private void rename(int port, String rename) {
        for (int i = 0; i < people.size(); i++) {
            if (people.get(i).port == port) {
                people.get(i).name = rename;
                service.writeUTF(toJson("/user/rename", "\"rename\":true"), port);
                return;
            }
        }
        service.writeUTF(toJson("/user/rename", "\"rename\":false"), port);
    }

    private String toJson(String url, String data) {
        return "{\"url\":\"" + url + "\"," +
                "\"data\":{" + data + "}}";
    }

    @Override
    public void onDrops(int port, SocketException e) {
        e.printStackTrace();
        for (Person person : people) {
            if (person.port == port) {
                people.remove(person);
                return;
            }
        }
    }

    public void send(String json) {
        try {//{from="",msg="",directive=false,to=""}
            JSONObject object = JSON.parseObject(json);
            String name = object.getString("to");
            int port = getPortByName(name);
            String msg = object.getString("msg");
            if (port == -1) service.writeUTF(msg);
            service.writeUTF(msg, port);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int getPortByName(String name) {
        for (Person person : people) {
            if (person.name.equals(name)) {
                return person.port;
            }
        }
        return 0;
    }

    public static class Person {
        private int port;
        private String name;

        public Person() {
        }

        public Person(int port, String name) {
            this.port = port;
            this.name = name;
        }
    }
}
