package cn.com.reformer.netty.handler;

import cn.com.reformer.netty.adapter.TCPMessageHandlerAdapter;
import cn.com.reformer.netty.cache.MsgCache;
import cn.com.reformer.netty.msg.AbsMsg;
import cn.com.reformer.netty.msg.MSG_0xE5;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  Copyright 2017 the original author or authors hangzhou Reformer 
 * @Description: ${todo}(这里用一句话描述这个类的作用)
 * @author zhangjin
 * @create 2017-05-08
**/
public class Handler0xE5 extends TCPMessageHandlerAdapter {

    Logger logger = LoggerFactory.getLogger(Handler0xE5.class);

    public void doHandle(AbsMsg m, ChannelHandlerContext ctx) {
        try {
            if (m instanceof MSG_0xE5) {
                MSG_0xE5 msg = (MSG_0xE5) m;
//                MsgCache.getInstance().remove(msg.getHead() + ";" + msg.getHead().getSeq());
            } else {
                logger.error("convert common reply fail:" + m.toString());
            }
        } catch (Exception e) {
            logger.error("handler common reply fail" + e);
        }
    }

}
