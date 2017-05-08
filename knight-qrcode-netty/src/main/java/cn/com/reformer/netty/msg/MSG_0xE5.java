package cn.com.reformer.netty.msg;

import cn.com.reformer.netty.util.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *  Copyright 2017 the original author or authors hangzhou Reformer
 * @Description: 开门指令
 * @author zhangjin
 * @create 2017-05-08
**/
public class MSG_0xE5 extends AbsMsg {

    private static final Logger logger = LoggerFactory.getLogger(MSG_0xE5.class);
    private static final long serialVersionUID = 1L;




    @Override
    protected short getCmd() {
        return MessageID.MSG_0xE5;
    }

    @Override
    protected int getBodylen() {
        return 3 ;
    }

    @Override
    protected byte[] bodytoBytes() {

//        byte[] src = Converter.shortToByteArray(getCmd());
//
//        byte[] data = new byte[getBodylen()];
//        byte[] p =Converter.string2ASCII(",");
//        System.arraycopy(p,0,data,0,1);
//        String src1 = Converter.toHexsFormat(src);
//        System.arraycopy(src1.getBytes(),0,data,1,2);
        byte[] data=new byte[0];
        return data;
    }

    @Override
    public boolean bodyfrombytes(byte[] data) {
        boolean resultState = false;
        int offset = 0;
        try {

        } catch (Exception e) {
            logger.error("登录消息fromBytes转换异常", e);
            e.printStackTrace();
        }
        return resultState;
    }

}
