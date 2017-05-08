package socket.netty.machine.handler;

import cn.com.reformer.netty.msg.AbsMsg;
import cn.com.reformer.netty.msg.MSG_0x3003;
import cn.com.reformer.netty.msg.MsgHeader;
import cn.com.reformer.netty.msg.json.CommonResponse;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 链路登陆handler
 *
 * @author sid
 */
public class Handler0x3003 implements IHandler {

    Logger logger = LoggerFactory.getLogger(Handler0x3003.class);

    public void doHandle(AbsMsg m, ChannelHandlerContext ctx) {
        try {
            if (m instanceof MSG_0x3003) {
                MSG_0x3003 msg = (MSG_0x3003) m;
                //去除消息缓存
                MsgHeader head = msg.getHead();
                String str = msg.getContent();
            } else {
                logger.error("通用应答消息强转失败:" + m.toString());
            }
        } catch (Exception e) {
            logger.error("通用应答处理失败" + e);
        }
    }


}
