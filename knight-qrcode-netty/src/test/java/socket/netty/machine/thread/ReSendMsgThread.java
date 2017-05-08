package socket.netty.machine.thread;

import cn.com.reformer.netty.cache.MsgObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import socket.netty.MsgCache;
import socket.netty.machine.TcpMachineClient;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ReSendMsgThread extends AbsThread {

    static ReSendMsgThread obj;
    private static final Logger logger = LoggerFactory
            .getLogger(ReSendMsgThread.class);

    private ReSendMsgThread() {
    }

    public static ReSendMsgThread getInstance() {
        if (obj == null) {
            synchronized (ReSendMsgThread.class) {
                if (obj == null) {
                    obj = new ReSendMsgThread();
                }
            }
            obj = new ReSendMsgThread();
        }
        return obj;
    }

    private Timer timer = new Timer();

    /**
     * executeWork:(重发消息，每10秒重发一次消息，重发5次后删除，24小时).
     *
     * @author sid
     */
    public void executeWork() {
        List<MsgObj> cleanAndgetResendMsg = MsgCache.getInstance()
                .cleanAndgetResendMsg(10, Integer.MAX_VALUE, 24);
        for (MsgObj m : cleanAndgetResendMsg) {
            int count = m.getSendedCount();
            Date now = new Date();
            m.setSendedCount(++count);
            m.setSendTime(now);
            TcpMachineClient.getInstance().sendWithoutCache(m.getMsg());
        }
    }

    @Override
    public void runThread(long delay, long period) {
        timer = new Timer();
        try {
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    executeWork();

                }
            }, delay, period);
        } catch (Exception e) {
            logger.info("消息重发异常：", e);
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {
        isRun = false;
        timer.cancel();
    }
}
