package cn.com.reformer.netty.handler;

import cn.com.reformer.netty.adapter.TCPMessageHandlerAdapter;
import cn.com.reformer.netty.bean.Client;
import cn.com.reformer.netty.bean.TcpUser;
import cn.com.reformer.netty.bean.UserType;
import cn.com.reformer.netty.msg.AbsMsg;
import cn.com.reformer.netty.msg.MSG_0x02;
import cn.com.reformer.netty.msg.MSG_0x3003;
import cn.com.reformer.netty.util.Converter;
import cn.com.reformer.netty.util.msg.ClientManager;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  Copyright 2017 the original author or authors hangzhou Reformer 
 * @Description: ${todo}(这里用一句话描述这个类的作用)
 * @author zhangjin
 * @create 2017-05-08
**/
public class Handler0x02 extends TCPMessageHandlerAdapter {

    Logger logger = LoggerFactory.getLogger(Handler0x02.class);

    @Autowired
    private ClientManager clientManager;

    public void doHandle(AbsMsg m, ChannelHandlerContext ctx) {
        try {
            if (m instanceof MSG_0x02) {
                logger.info("heart beat message from mac is :" + m.getHead().getMac());


                Client client = ClientManager.getClient(ctx);
                if (null != client) {
                    clientManager.setClientLastTime(ctx, client);

                    MSG_0x3003 msg_0x3003 = new MSG_0x3003();
                    short seq = m.getHead().getSeq();    //序号最大为255
                    byte[] src = Converter.shortToByteArray(seq);
                    msg_0x3003.setContent("(" + Converter.toHexsFormat(src) + ",OK)");
                    ctx.writeAndFlush(msg_0x3003);
                } else {
                    TcpUser user = new TcpUser();
                    user.setSn(m.getHead().getSn());
                    user.setType(UserType.QRCODEMACHINE);

                    clientManager.addClient(ctx, user);
                    logger.error("convert heartbeat message  add   TCP User :" + m.toString());
                }
            }
        } catch (Exception e) {
            logger.error("heartbeat message  fail" + e);
        }
    }

    public ClientManager getClientManager() {
        return clientManager;
    }

    public void setClientManager(ClientManager clientManager) {
        this.clientManager = clientManager;
    }
}
