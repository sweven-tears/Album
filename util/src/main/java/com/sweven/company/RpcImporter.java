package com.sweven.company;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RpcImporter<S> {
    public S importer(Class<?> serviceClass, InetSocketAddress addr) {
        return (S) Proxy.newProxyInstance(serviceClass
                .getClassLoader(), new Class[]{serviceClass
                .getInterfaces()[0]}, (proxy, method, args) -> {
            Socket socket = null;
            ObjectOutputStream output = null;
            ObjectInputStream input = null;
            try {
                socket = new Socket();
                socket.connect(addr);
                output = new ObjectOutputStream(socket.getOutputStream());
                output.writeUTF(serviceClass.getName());
                output.writeUTF(method.getName());
                output.writeObject(method.getParameterTypes());
                output.writeObject(args);
                input = new ObjectInputStream(socket.getInputStream());
                return input.readObject();
            } finally {
                if (socket != null) {
                    socket.close();
                }
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            }
        });
    }

    public static void main(String[] args) {
        (new Thread(() -> {
            try {
                RpcExporter.exporter("localhost", 8098);
            } catch (Exception e) {
                e.printStackTrace();
            }
        })).start();
    }
}


/* Location:              C:\Users\Administrator.SC-201911131242\Desktop\simplerpc.jar!\com\company\RpcImporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.2
 */