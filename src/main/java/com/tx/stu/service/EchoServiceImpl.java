package com.tx.stu.service;

/**
 * 服务端接口实现
 * Created by peter.
 */
public class EchoServiceImpl implements EchoService{
    @Override
    public String echo(String ping) {
        return ping != null ? ping + " --> I am ok." : " I am ok.";
    }
}
