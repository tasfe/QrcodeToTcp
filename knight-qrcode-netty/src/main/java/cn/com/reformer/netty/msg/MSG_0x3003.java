package cn.com.reformer.netty.msg;

import cn.com.reformer.netty.util.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *  Copyright 2017 the original author or authors hangzhou Reformer
 * @Description: 服务器端通用应答
 * @author zhangjin
 * @create 2017-05-08
**/
public class MSG_0x3003 extends AbsMsg {

    private static final Logger logger = LoggerFactory.getLogger(MSG_0x3003.class);
    private static final long serialVersionUID = 1L;


    private String content;


    @Override
    protected short getCmd() {
        return MessageID.MSG_0x3003;
    }

    @Override
    protected int getBodylen() {

        return Converter.getBytes(content).length;
    }

    /**
     * absMsg对象转换成byte[]
     *
     * @return
     */
    @Override
    public byte[] toBytes() {

        byte[] byte_content = Converter.string2ASCII(content);
        return byte_content;
    }

    @Override
    protected byte[] bodytoBytes() {
        byte[] data = new byte[getBodylen()];
        int offset = 0;
        try {
            System.arraycopy(Converter.getBytes(content), 0, data, offset, Converter.getBytes(content).length);
        } catch (Exception e) {
            logger.error("服务器通用应答toBytes转换异常", e);
            e.printStackTrace();
        }
        return data;
    }


    @Override
    public boolean bodyfrombytes(byte[] data) {
        boolean resultState = false;
        int offset = 0;
        try {
            this.content = Converter.toGBKString(data, offset, data.length);
            resultState = true;
        } catch (Exception e) {
            logger.error("登录消息fromBytes转换异常", e);
            e.printStackTrace();
        }
        return resultState;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
