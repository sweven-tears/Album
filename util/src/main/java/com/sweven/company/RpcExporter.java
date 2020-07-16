package com.sweven.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RpcExporter {
    static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void exporter(String hostName, int port) throws Exception {
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(hostName, port));
        try {
            while (true) {
                System.out.println("recieving...");
                executor.execute(new ExporterTask(server.accept()));
            }
        } finally {
            server.close();
        }
    }

    private static class ExporterTask
            implements Runnable {
        Socket client;

        public ExporterTask(Socket client) {
            this.client = client;
        }

        public void run() {
            ObjectOutputStream output = null;
            try {
                ObjectInputStream input = new ObjectInputStream(this.client.getInputStream());
                String interfaceName = input.readUTF();
                Class<?> service = Class.forName(interfaceName);
                String methodName = input.readUTF();
                Class<?>[] parameterTypes = (Class[]) input.readObject();
                Object[] arguments = (Object[]) input.readObject();
                Method method = service.getMethod(methodName, parameterTypes);
                Object result = method.invoke(service.newInstance(), arguments);
                output = new ObjectOutputStream(this.client.getOutputStream());
                output.writeObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (this.client != null)
                    try {
                        this.client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}


/* Location:              C:\Users\Administrator.SC-201911131242\Desktop\simplerpc.jar!\com\company\RpcExporter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.2
 */