package cn.com.reformer.netty.msg;

import cn.com.reformer.netty.util.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *  Copyright 2017 the original author or authors hangzhou Reformer
 * @Description:  MSG_0x01  上传二维码协议
 * @author zhangjin
 * @create 2017-05-08
**/
public class MSG_0x01 extends AbsMsg {

    private static final Logger logger = LoggerFactory.getLogger(MSG_0x01.class);
    private static final long serialVersionUID = 1L;
    private String data;


    @Override
    public String toString() {
        return "MSG_0x01 [data=" + data
                + ", head=" + head + "]";
    }


    @Override
    protected short getCmd() {
        return MessageID.MSG_0x01;
    }


    @Override
    protected int getBodylen() {
        return 7;
    }

    @Override
    public byte[] bodytoBytes() {
        byte[] data = new byte[getBodylen()];

        return data;
    }

    @Override
    public boolean bodyfrombytes(byte[] b) {
        boolean resultState = false;
        try {
            this.data = Converter.toGBKString(b);

            resultState = true;
        } catch (Exception e) {
            logger.error("登录消息fromBytes转换异常", e);
            e.printStackTrace();
        }
        return resultState;
    }

    /**
     * 获取二维码信息
     * @return
     */
    public String getQrCode() {
        if(null !=data && data.length()>0){
            return data.substring(1);
        }
        else{
            return  data;
        }
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
