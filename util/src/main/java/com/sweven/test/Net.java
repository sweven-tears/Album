package com.sweven.test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sweven.bean.Folder;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sweven
 * @date 2020/11/26 -- 10:28
 * Email: sweventears@163.com
 */
public class Net {
    private String net;

    public Net(String net) {
        this.net = net;
    }

    public static void main(String[] args) {
        try {
            String json = "{\"message\":\"操作成功\",\"timestamp\":1607651586998,\"exception\":null,\"eCode\":0,\"httpStatus\":200,\"path\":null,\"data\":0}";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("data",1);
            JsonElement element = jsonObject.get("data");
            String data = null;
            Folder album = new Gson().fromJson(data, Folder.class);
            System.out.println(album);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (true) {
            return;
        }
        new Abs<List<Folder>>() {
            @Override
            protected List<Folder> print(Type name) {
                System.out.println("say " + name);
                List<Folder> albums = new ArrayList<>();
                albums.add(new Folder());
                return albums;
            }
        };


        String url = "http://192.168.2.31:81/index.html#/schoolManage/schoolMien";
        int index = url.lastIndexOf("/");
        String result = url.substring(index);
        File file = new File("sdcard", result);
        System.out.println(result + file.getAbsolutePath());
        String a = null;
        Net a1 = new Net("null");
        boolean b = a1.equals0(a);
        System.out.println(b);
        a1.hello();
        a1.hello(1, 2, 3, 4);
    }

    public void hello(int... h) {
        int length = h.length;
        System.out.println(length);
    }

    public boolean equals0(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof String) {
            String anotherString = (String) anObject;
            int n = length();
            if (n == anotherString.length()) {
                int i = 0;
                while (n-- != 0) {
                    if (charAt(i) != anotherString.charAt(i)) {
                        return false;
                    }
                    i++;
                }
                return true;
            }
        }
        return false;
    }

    private char charAt(int i) {
        return net.charAt(i);
    }

    private int length() {
        return net != null ? net.length() : 0;
    }


}
