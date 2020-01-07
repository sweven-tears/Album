package com.sweven.socket;

import com.sweven.console.Console;
import com.sweven.console.LogUtil;

import java.io.IOException;

/**
 * Created by Sweven on 2020/1/7--12:56.
 * Email: sweventears@foxmail.com
 */
public class App {
    public static LogUtil log = new LogUtil("Socket");

    public static void main(String[] arg){
        log.debug(false);
        try {
            new SocketService().start();
        } catch (IOException e) {
            Console.log("连接异常");
            e.printStackTrace();
        }
    }
}
