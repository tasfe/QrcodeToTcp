package cn.com.reformer.netty.handler;

import cn.com.reformer.netty.msg.ReceivePackBean;
import cn.com.reformer.netty.msg.ServerMsgQueue;
import cn.com.reformer.netty.util.msg.ClientManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 *  Copyright 2017 the original author or authors hangzhou Reformer 
 * @Description: ${todo}(这里用一句话描述这个类的作用)
 * @author zhangjin
 * @create 2017-05-08
**/
@ChannelHandler.Sharable
public class TCPMessageHandler extends SimpleChannelInboundHandler {
    private static final Logger LOG = LoggerFactory.getLogger(TCPMessageHandler.class);

    @Autowired
    private HandlerFactory handlerFactory;

    @Autowired
    private ClientManager clientManager;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            if (msg instanceof byte[]) {
                final byte[] msgBytes = (byte[]) msg;

                ReceivePackBean receivePackBean = new ReceivePackBean();
                receivePackBean.setChannel(ctx);
                receivePackBean.setMsgbytes(msgBytes);
                ServerMsgQueue.getRecqueue().put(receivePackBean);
            } else {
                LOG.error("主handler---消息解码有误，请检查！！");
            }
        } catch (InterruptedException e) {
            LOG.error("主handler---接收消息失败", e);
        }

       /* ISender sender = new TCPMessageSender(ctx.channel());
        IMessageHandler iMessageHandler= handlerFactory.getHandler(msg);
        iMessageHandler.doHandle(msg,ctx);*/


    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        try {
            super.channelActive(ctx);
            LOG.info("-------------临时客户端建立连接--------------");
            // TCPServer.setChtx(ctx);
            clientManager.addTemClient(ctx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        try {
            super.channelInactive(ctx);
            InetSocketAddress address = (InetSocketAddress) ctx.channel()
                    .remoteAddress();
            InetAddress inetAdd = address.getAddress();
            LOG.info("客户端断开连接：" + ctx.name()
                    + ",IP:" + inetAdd.getHostAddress()
                    + ",port:" + address.getPort());
            // 记录日志
            clientManager.removeClient(ctx);
            clientManager.removeTemClient(ctx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.info("-------------连接异常关闭--------------");
        try {
            cause.printStackTrace();
            ctx.close();
            clientManager.removeClient(ctx);
            clientManager.removeTemClient(ctx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HandlerFactory getHandlerFactory() {
        return handlerFactory;
    }

    public void setHandlerFactory(HandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }
}
