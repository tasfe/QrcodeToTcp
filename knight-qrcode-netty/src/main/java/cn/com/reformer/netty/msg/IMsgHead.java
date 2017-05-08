package cn.com.reformer.netty.msg;


/**
 *  Copyright 2017 the original author or authors hangzhou Reformer
 * @Description: 消息头接口
 * @author zhangjin
 * @create 2017-05-08
**/
public interface IMsgHead {
    /**
     * 消息转换为字节数组
     *
     * @return
     */
    byte[] tobytes();

    /**
     * 消息长度
     *
     * @return
     */
    int getHeadLen();

    /**
     * 字节数组转换为消息
     *
     * @param b
     * @return
     */
    boolean frombytes(byte[] b);
}
