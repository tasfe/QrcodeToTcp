package cn.com.reformer.netty.handler;

import cn.com.reformer.netty.msg.AbsMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  Copyright 2017 the original author or authors hangzhou Reformer 
 * @Description: ${todo}(这里用一句话描述这个类的作用)
 * @author zhangjin
 * @create 2017-05-08
**/
@ChannelHandler.Sharable
public class TCPMessageEncoder extends MessageToByteEncoder<AbsMsg> {
    private static final Logger LOG = LoggerFactory.getLogger(TCPMessageEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, AbsMsg msg, ByteBuf out) throws Exception {
        LOG.info("Send tcp packet '{}' to '{}' ", msg.toString(), ctx.channel().remoteAddress());
        byte[] packetByte = msg.toBytes();
        out.writeBytes(packetByte);
        // ctx.flush();
    }

}
