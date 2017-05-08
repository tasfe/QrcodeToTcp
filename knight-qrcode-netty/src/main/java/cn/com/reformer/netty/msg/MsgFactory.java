package cn.com.reformer.netty.msg;

import cn.com.reformer.netty.util.Constants;
import cn.com.reformer.netty.util.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 *  Copyright 2017 the original author or authors hangzhou Reformer
 * @Description: 消息工厂
 * @author zhangjin
 * @create 2017-05-08
**/
public class MsgFactory {

    private static final Logger logger = LoggerFactory
            .getLogger(MsgFactory.class);

    /**
     * 根据二进制文件生成消息对象
     *
     * @param head
     * @return
     */
    public static AbsMsg genMsg(MsgHeader head, byte[] msgbytes) {
        byte xor = 0;


        ByteBuffer bf = ByteBuffer.wrap(msgbytes);
        logger.info("处理消息：" + Converter.bytes2HexsSpace(msgbytes));
        int bodylen = msgbytes.length - Constants.HEAD_LENGTH;//去掉消息头和校验码长度
        byte[] body = new byte[bodylen];
        bf.position(Constants.HEAD_LENGTH + Constants.SIGN_STAR_LENGTH - 1);//下标从0开始
        bf.get(body);
        logger.info(bodylen + "处理消息体：" + Converter.bytes2HexsSpace(body));
        logger.info(bodylen + "处理消息体：" + Converter.toGBKString(body));
        AbsMsg m = null;
        int cmd = head.getCmd();
        logger.info("处理消息id：" + cmd);
        switch (cmd) {
            case MessageID.MSG_0x01:
                m = new MSG_0x01();
                break;
            case MessageID.MSG_0x02:
                m = new MSG_0x02();
                break;
            case MessageID.MSG_0xE5:
                m = new MSG_0xE5();
                break;

            case MessageID.MSG_0x3003:
                m = new MSG_0x3003();
                break;
            default:
                m = new MSG_0x3003();
                break;
        }
        if (m != null) {
            m.head = head;
        }
        m.bodyfrombytes(body);
        logger.debug("接收到数据解码后：" + m.toString());

        return m;
    }
}
