package com.sweven.company;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        RpcImporter<EchoService> importer = new RpcImporter<>();
        EchoService echo = importer.importer(EchoServiceImpl.class, new InetSocketAddress("localhost", 8098));
        System.out.println(echo.echo("mary?"));
    }
}


/* Location:              C:\Users\Administrator.SC-201911131242\Desktop\simplerpc.jar!\com\company\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.2
 */