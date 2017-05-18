package cn.com.reformer.netty.handler;

import cn.com.reformer.netty.adapter.TCPMessageHandlerAdapter;
import cn.com.reformer.netty.bean.TcpUser;
import cn.com.reformer.netty.bean.UserType;
import cn.com.reformer.netty.communication.QrcodeTcpMessageSender;
import cn.com.reformer.netty.msg.AbsMsg;
import cn.com.reformer.netty.msg.MSG_0x01;
import cn.com.reformer.netty.msg.MSG_0x3003;
import cn.com.reformer.netty.msg.json.CommonResponse;
import cn.com.reformer.netty.observer.EventWatcher;
import cn.com.reformer.netty.observer.UploadQrCodeEvent;
import cn.com.reformer.netty.util.Constants;
import cn.com.reformer.netty.util.msg.ClientManager;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Observable;

/**
 *  Copyright 2017 the original author or authors hangzhou Reformer 
 * @Description: ${todo}(这里用一句话描述这个类的作用)
 * @author zhangjin
 * @create 2017-05-08
**/
public class Handler0x01 extends TCPMessageHandlerAdapter {

    Logger logger = LoggerFactory.getLogger(Handler0x01.class);

    @Autowired(required = true)
    private ClientManager clientManager;
    @Autowired(required = true)
    private QrcodeTcpMessageSender qrcodeTcpMessageSender;


    public void doHandle(AbsMsg m, ChannelHandlerContext ctx) {
        MSG_0x3003 response = new MSG_0x3003();
        CommonResponse commonResponse = new CommonResponse();
        try {
            if (m instanceof MSG_0x01) {
                MSG_0x01 msg = (MSG_0x01) m;

                String qrcode =msg.getQrCode();
                UploadQrCodeEvent uploadQrCodeEvent=new UploadQrCodeEvent();
                EventWatcher eventWatcher=new EventWatcher();
                uploadQrCodeEvent.addObserver(eventWatcher);
                uploadQrCodeEvent.setQrCode(qrcode);
                uploadQrCodeEvent.notifyObservers();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TcpUser getTcpUserByMac(String mac) {
        return new TcpUser();
    }


    public ClientManager getClientManager() {
        return clientManager;
    }

    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }


}
