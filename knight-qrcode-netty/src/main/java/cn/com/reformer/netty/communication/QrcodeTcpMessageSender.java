package cn.com.reformer.netty.communication;

import cn.com.reformer.netty.bean.Client;
import cn.com.reformer.netty.msg.MSG_0xE5;
import cn.com.reformer.netty.msg.MsgHeader;
import cn.com.reformer.netty.util.msg.ClientManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *  Copyright 2017 the original author or authors hangzhou Reformer 
 * @Description: ${todo}(这里用一句话描述这个类的作用)
 * @author zhangjin
 * @create 2017-05-08
**/
public class QrcodeTcpMessageSender extends TCPMessageSender {

    @Autowired(required = true)
    private ClientManager clientManager;

    public QrcodeTcpMessageSender(Channel channel) {
        super(channel);
    }

    public QrcodeTcpMessageSender() {
        super();
    }

    public void openDoor(String mac, String sn) {

        Client client = ClientManager.getClientBySN(sn);

        ChannelHandlerContext channel = client.getChannel();

        MSG_0xE5 msg0xE5=new MSG_0xE5();
        MsgHeader header=new MsgHeader();
        header.setMac(mac);
        header.setSn(sn);
        header.setCmd((short)0xE5);
        short seq=0xFF;
        header.setSeq(seq);
        msg0xE5.setHead(header);
        channel.writeAndFlush(msg0xE5);


    }


    public ClientManager getClientManager() {
        return clientManager;
    }

    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }
}
