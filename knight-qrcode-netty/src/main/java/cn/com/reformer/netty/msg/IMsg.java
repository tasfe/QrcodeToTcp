package cn.com.reformer.netty.msg;

/**
 *  Copyright 2017 the original author or authors hangzhou Reformer
 * @Description: 消息体接口
 * @author zhangjin
 * @create 2017-05-08
**/
public interface IMsg {
    /**
     * 消息转换为字节数组
     *
     * @return
     */
    byte[] toBytes();

    /**
     * 字节数组转换为消息
     *
     * @param b
     * @return
     */
    boolean fromBytes(byte[] b);
}
