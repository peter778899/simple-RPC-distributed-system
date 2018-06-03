package com.tx.stu;

import com.tx.stu.exporter.RpcExporter;
import com.tx.stu.proxy.RpcImporter;
import com.tx.stu.service.EchoService;
import com.tx.stu.service.EchoServiceImpl;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by peter.
 */
public class App {
    public static void main( String[] args ) {
        //启动服务端线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RpcExporter.exporter("localhost", 8088);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //客户端调用远程服务
        RpcImporter<EchoService> importer = new RpcImporter<>();
        EchoService service = importer.importer(EchoServiceImpl.class, new InetSocketAddress("localhost", 8088));

        System.out.println(service.echo("hello?"));
    }
}
