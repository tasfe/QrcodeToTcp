package cn.com.reformer.netty.server.thread;

import cn.com.reformer.netty.business.IMessageHandler;
import cn.com.reformer.netty.handler.HandlerFactory;
import cn.com.reformer.netty.msg.AbsMsg;
import cn.com.reformer.netty.msg.MsgFactory;
import cn.com.reformer.netty.msg.MsgHeader;
import cn.com.reformer.netty.msg.ReceivePackBean;
import cn.com.reformer.netty.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * 处理消息线程
 */
public class ParseMsgThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ParseMsgThread.class);

    private ReceivePackBean rpb;
    private HandlerFactory handlerFactory;

    public ParseMsgThread() {

    }

    public ParseMsgThread(ReceivePackBean rpb, HandlerFactory handlerFactory) {
        this.rpb = rpb;
        this.handlerFactory = handlerFactory;
    }

    @Override
    public void run() {
        try {
            // 转码
            rpb.setMsgbytes((rpb.getMsgbytes()));

            // 消息头解析
            MsgHeader head = headFromBytes(rpb.getMsgbytes());
            if (head == null) {
                return;
            }
            logger.info("消息头：" + head.toString());

            // 生成消息后产生handler
            AbsMsg msg = MsgFactory.genMsg(head, rpb.getMsgbytes());
            if (msg == null) {
                logger.error(Integer.toHexString(head.getCmd()) + "消息不存在");
                return;
            }
            logger.info("消息体：" + msg.toString());
            // 交给对应handler处理
            IMessageHandler handler = handlerFactory.getHandler(msg);
            if (handler != null) {
                handler.doHandle(msg, rpb.getChannel());
            }
        } catch (Exception e) {
            logger.error("接受消息队列处理数据错误", e);
        }
    }

    /**
     * 消息头解析
     *
     * @return
     */
    private MsgHeader headFromBytes(byte[] b) {
        ByteBuffer buffer1 = ByteBuffer.wrap(b);
        byte[] head_body = new byte[Constants.HEAD_LENGTH];
        buffer1.position(0);
        buffer1.get(head_body);

        MsgHeader head = new MsgHeader();
        if (!head.frombytes(head_body))
            return null;// 消息头属性解析有误
        return head;
    }

    /**
     * 解码转义
     *
     * @param b
     * @return
     */
    private byte[] decode(byte[] b) {
        ByteBuffer buffer = ByteBuffer.allocate(5 * 1024 * 1024);
        ByteBuffer buffer1 = ByteBuffer.wrap(b);
        buffer.position(0);
        while (buffer1.remaining() > 0) {

            byte d = buffer1.get();
            if (d == 0x5a) {
                byte e = buffer1.get();
                if (e == 0x02)
                    buffer.put((byte) 0x5a);
                else if (e == 0x01)
                    buffer.put((byte) 0x5b);
            } else if (d == 0x5e) {
                byte e = buffer1.get();
                if (e == 0x02)
                    buffer.put((byte) 0x5e);
                else if (e == 0x01)
                    buffer.put((byte) 0x5d);
            } else {
                buffer.put(d);
            }
        }

        byte[] result = new byte[buffer.position()];
        buffer.position(0);
        buffer.get(result);
        return result;
    }

    public HandlerFactory getHandlerFactory() {
        return handlerFactory;
    }

    public void setHandlerFactory(HandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }
}
