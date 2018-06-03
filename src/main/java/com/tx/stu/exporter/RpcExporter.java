package com.tx.stu.exporter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 服务端服务发现者
 * Created by peter.
 */
public class RpcExporter {
    static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void exporter(String hostname,int port) throws IOException {
        try(ServerSocket server = new ServerSocket();) {
            server.bind(new InetSocketAddress(hostname, port));

            while(true) {
                executor.execute(new ExporterTask(server.accept()));
            }
        }
    }

    private static class ExporterTask implements Runnable {
        Socket clint = null;
        public ExporterTask(Socket client) {
            this.clint = client;
        }

        @Override
        public void run() {
            try(ObjectInputStream input = new ObjectInputStream(clint.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(clint.getOutputStream())){
                String interfaceName = input.readUTF();
                Class<?> service = Class.forName(interfaceName);

                String methodName = input.readUTF();
                Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
                Object[] arguments = (Object[]) input.readObject();
                Method method = service.getMethod(methodName, parameterTypes);
                Object result = method.invoke(service.newInstance(), arguments);

                output.writeObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
