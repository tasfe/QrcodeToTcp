package socket.netty.machine.handler;

import cn.com.reformer.netty.msg.AbsMsg;
import io.netty.channel.ChannelHandlerContext;

public interface IHandler {

    void doHandle(AbsMsg m, ChannelHandlerContext ctx);

}
