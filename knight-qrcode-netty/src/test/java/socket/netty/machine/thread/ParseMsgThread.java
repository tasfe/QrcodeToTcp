package socket.netty.machine.thread;

import cn.com.reformer.netty.msg.AbsMsg;
import cn.com.reformer.netty.msg.MsgHeader;
import cn.com.reformer.netty.msg.ReceivePackBean;
import cn.com.reformer.netty.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import socket.netty.machine.MsgFactory;
import socket.netty.machine.handler.HandlerFactory;
import socket.netty.machine.handler.IHandler;

import java.nio.ByteBuffer;
import java.util.Date;

/**
 * 处理消息线程
 */
public class ParseMsgThread extends Thread {

    private static final Logger logger = LoggerFactory
            .getLogger(ParseMsgThread.class);

    private ReceivePackBean rpb;

    public ParseMsgThread(ReceivePackBean rpb) {
        this.rpb = rpb;
    }

    @Override
    public void run() {
        try {
            HeartBeatThread.lastTime = new Date();

            // 转码
            byte[] msgbytes = decode(rpb.getMsgbytes());

            // 消息头解析
            MsgHeader head = headFromBytes(msgbytes);
            if (head == null) {
                return;
            }

            // 生成消息后产生handler
            AbsMsg msg = MsgFactory.genMsg(head, msgbytes);
            if (msg == null) {
                logger.error(Integer.toHexString(head.getCmd()) + "消息不存在");
                return;
            }
            // 交给对应handler处理
            IHandler handler = HandlerFactory.getHandler(msg);
            if (handler != null) {
                handler.doHandle(msg, rpb.getChannel());
            }
        } catch (Exception e) {
            logger.error("接受消息队列处理数据错误", e);
            e.printStackTrace();
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
        ByteBuffer buffer = ByteBuffer.allocate(10 * 1024 * 1024);
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

}
