package cn.com.reformer.netty.handler;

import cn.com.reformer.netty.util.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.List;

/**
 *  Copyright 2017 the original author or authors hangzhou Reformer 
 * @Description: ${todo}(这里用一句话描述这个类的作用)
 * @author zhangjin
 * @create 2017-05-08
**/
public class TCPMessageDecoder extends ByteToMessageDecoder {
    private static final byte HeadFlag = 0x28;
    private static final byte EndFlag = 0x29;
    private static final Logger LOG = LoggerFactory.getLogger(TCPMessageDecoder.class);
    private ByteBuffer bf = ByteBuffer.allocate(1 * 1024 * 1024);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        LOG.info("开始解码：");
        try {
            while (buffer.readableBytes() > 0) {
                byte b = buffer.readByte();
                bf.put(b);
                if (b == EndFlag) {
                    byte[] bytes = new byte[bf.position() - Constants.SIGN_STAR_LENGTH - Constants.SIGN_END_LENGTH];
                    if (bf.get(0) == HeadFlag) {//找到头尾后去除
                        bf.position(1);
                        bf.get(bytes);
                        out.add(bytes);
                        bytes = null;
                    }
                    bf.clear();
                }
            }
        } catch (Exception e) {
            LOG.error("解码异常:" + e.toString());
        }
    }
}
