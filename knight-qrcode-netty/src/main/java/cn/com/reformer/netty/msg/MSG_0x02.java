package cn.com.reformer.netty.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  Copyright 2017 the original author or authors hangzhou Reformer
 * @Description: 心跳消息
 * @author zhangjin
 * @create 2017-05-08
**/
public class MSG_0x02 extends AbsMsg {

    private static final Logger logger = LoggerFactory.getLogger(MSG_0x02.class);

    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        return "MSG_0x02 [head=" + head + "]";
    }

    @Override
    protected short getCmd() {
        return MessageID.MSG_0x02;
    }

    @Override
    protected int getBodylen() {
        return 0;
    }

    @Override
    protected byte[] bodytoBytes() {
        return new byte[0];
    }

    @Override
    public boolean bodyfrombytes(byte[] b) {
        return false;
    }
}
