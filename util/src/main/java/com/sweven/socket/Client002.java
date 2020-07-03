package com.sweven.socket;

import java.util.Scanner;

/**
 * <p>Create by Sweven on 2020/7/3 -- 17:28</p>
 * Email: sweventears@163.com
 */
public class Client002 {
    public static void main(String[] args) {
        ClientManager manager = new ClientManager();
        Scanner scanner = new Scanner(System.in);
        String next = scanner.nextLine();
        while (!next.equals("close")) {
            manager.send(next);
            next = scanner.nextLine();
        }
        scanner.close();
    }
}
