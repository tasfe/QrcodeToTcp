package cn.com.reformer.netty.msg;

import cn.com.reformer.netty.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 *  Copyright 2017 the original author or authors hangzhou Reformer 
 * @Description: ${todo}(这里用一句话描述这个类的作用)
 * @author zhangjin
 * @create 2017-05-08
**/
public abstract class AbsMsg implements MessagePacket {


    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(AbsMsg.class);

    public MsgHeader head;

    public volatile static short seq = 0;
    // 消息长度
    ByteBuffer buffer = ByteBuffer.allocate(5 * 1024 * 1024);

    public AbsMsg() {
        this.head = new MsgHeader();
        if (seq == Integer.MAX_VALUE)
            seq = 0;
        this.head.setSeq(seq++);

    }

    /**
     * absMsg对象转换成byte[]
     *
     * @return
     */
    public byte[] toBytes() {

        byte[] b = new byte[0];
        try {
            // 消息内容
            int msg_len = Constants.HEAD_LENGTH ;
            int length = Constants.SIGN_STAR_LENGTH + msg_len + Constants.SIGN_END_LENGTH;
            this.head.setLength(length);
            byte[] head = this.head.tobytes();
            byte[] body = bodytoBytes();

            buffer.position(0);
            buffer.put(Constants.SIGN_START);
            buffer.put(head);
            buffer.put(body);
            buffer.put(Constants.SIGN_END);

            b = new byte[buffer.position()];
            buffer.position(0);
            buffer.get(b);
        } catch (Exception e) {
            logger.error("toBytes异常：", e);
        }

        return b;

    }

    /**
     * 编码转义
     *
     * @param bytes
     * @return
     */
    private byte[] encode(byte[] bytes) {

        buffer.position(0);
        for (byte b : bytes) {
            if (b == 0x5b) {
                buffer.put((byte) 0x5a);
                buffer.put((byte) 0x01);
            } else if (b == 0x5a) {
                buffer.put((byte) 0x5a);
                buffer.put((byte) 0x02);
            } else if (b == 0x5d) {
                buffer.put((byte) 0x5e);
                buffer.put((byte) 0x01);
            } else if (b == 0x5e) {
                buffer.put((byte) 0x5e);
                buffer.put((byte) 0x02);
            } else {
                buffer.put(b);
            }
        }

        byte[] result = new byte[buffer.position()];
        buffer.position(0);
        buffer.get(result);
        return result;
    }

    protected abstract short getCmd();

    protected abstract int getBodylen();

    protected abstract byte[] bodytoBytes();

    public abstract boolean bodyfrombytes(byte[] b);

    public MsgHeader getHead() {
        return head;
    }

    public void setHead(MsgHeader head) {
        this.head = head;
    }

}
