package com.sweven.socket;

import com.sweven.console.Console;
import com.sweven.console.LogUtil;

import java.util.Scanner;

/**
 * Created by Sweven on 2020/1/7--12:56.
 * Email: sweventears@foxmail.com
 */
public class App {
    public static LogUtil log = new LogUtil("Socket");

    public static SocketService service;

    public static void main(String[] arg) {
        log.debug(false);
        service = new SocketService();
        service.start(new SocketService.ServiceLaunchAdapter() {
            /**
             * 成功启动
             */
            @Override
            public void success() {
                super.success();
                launch();
            }
        });
    }

    private static void launch() {
        new Thread(() -> {
        }).start();
        Scanner scanner = new Scanner(System.in);
        String msg = scanner.nextLine();
        while (!msg.equals("exit")) {
            if (msg.startsWith("/list")) {
                showList();
            } else if (msg.startsWith("123:")) {
                send2Service(msg.substring("123:".length()));
            } else if (msg.startsWith("service:@all/")) {

                send2Clients(msg.substring("service:@all/".length()));
            } else if (msg.startsWith("service:")) {
                send2Client(msg.substring("service:".length()));
            }
            msg = scanner.nextLine();
        }
    }

    private static void showList() {
        service.showClientList();
    }

    private static void send2Clients(String msg) {
        Console.log("all:", msg);
        service.sendAll(msg);
    }

    private static void send2Client(String msg) {
        Console.err("service->123:", msg);
    }

    private static void send2Service(String msg) {
        Console.log("123->service:", msg);
    }
}
