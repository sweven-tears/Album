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
        service.addReceiverListener((id, msg) -> {
            if (msg.startsWith("user@")) {
                //配置用户标识
                for (int i = 0; i < service.getClients().size(); i++) {
                    ClientHelper client = service.getClients().get(i);
                    if (client.getId() == id) {
                        service.getClients().get(i).setSign(msg.substring(msg.indexOf("user@")));
                        Console.log(service.getClients().get(i).getSign() + " online.");
                        break;
                    }

                }
            } else if (msg.startsWith("sys/")) {
                //给系统发消息
                Console.log(id + " send sys:" + msg.substring(msg.indexOf("sys/")));
            }
        });
        Scanner scanner = new Scanner(System.in);
        String msg = scanner.nextLine();
        while (!msg.equals("exit")) {
            if (msg.startsWith("/list")) {
                showList();
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
