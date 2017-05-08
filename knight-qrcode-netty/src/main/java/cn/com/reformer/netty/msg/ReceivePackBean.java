package cn.com.reformer.netty.msg;

import io.netty.channel.ChannelHandlerContext;

/**
 *  Copyright 2017 the original author or authors hangzhou Reformer
 * @Description: 封装hanler类
 * @author zhangjin
 * @create 2017-05-08
**/
public class ReceivePackBean {

    private byte[] msgbytes;

    private ChannelHandlerContext channel;


    public ChannelHandlerContext getChannel() {
        return channel;
    }

    public void setChannel(ChannelHandlerContext channel) {
        this.channel = channel;
    }

    public byte[] getMsgbytes() {
        return msgbytes;
    }

    public void setMsgbytes(byte[] msgbytes) {
        this.msgbytes = msgbytes;
    }

}
