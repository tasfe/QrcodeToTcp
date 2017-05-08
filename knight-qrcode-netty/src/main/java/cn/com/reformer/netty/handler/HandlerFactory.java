package cn.com.reformer.netty.handler;

import cn.com.reformer.netty.business.IMessageHandler;
import cn.com.reformer.netty.msg.AbsMsg;
import cn.com.reformer.netty.msg.MessageID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *  Copyright 2017 the original author or authors hangzhou Reformer 
 * @Description: ${todo}(这里用一句话描述这个类的作用)
 * @author zhangjin
 * @create 2017-05-08
**/
public class HandlerFactory {

    @Autowired
    private Handler0x01 handler0X01;


    @Autowired
    private Handler0x02 handler0X02;


    @Autowired
    private Handler0xE5 handler0XE5;


    private static final Logger logger = LoggerFactory.getLogger(HandlerFactory.class);

    public IMessageHandler getHandler(AbsMsg m) {

        int cmd = m.getHead().getCmd();

        if (logger.isDebugEnabled()) {
            logger.debug("handler  factory create  message:" + Integer.toHexString(cmd)); //$NON-NLS-1$
        }
        IMessageHandler h = null;
        switch (cmd) {
            case MessageID.MSG_0x01:
                h = handler0X01;
                break;
            case MessageID.MSG_0x02:
                h = handler0X02;
                break;
            case MessageID.MSG_0xE5:
                h = handler0XE5;
                break;

            default:
                break;
        }
        return h;
    }

    public Handler0x01 getHandler0X01() {
        return handler0X01;
    }

    public void setHandler0X01(Handler0x01 handler0X01) {
        this.handler0X01 = handler0X01;
    }

    public Handler0x02 getHandler0X02() {
        return handler0X02;
    }

    public void setHandler0X02(Handler0x02 handler0X02) {
        this.handler0X02 = handler0X02;
    }

    public Handler0xE5 getHandler0XE5() {
        return handler0XE5;
    }

    public void setHandler0XE5(Handler0xE5 handler0XE5) {
        this.handler0XE5 = handler0XE5;
    }


}
