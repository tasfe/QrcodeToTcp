package socket.netty.machine.thread;

import cn.com.reformer.netty.msg.MSG_0x02;
import cn.com.reformer.netty.msg.MsgHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import socket.netty.machine.TcpMachineClient;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class HeartBeatThread extends AbsThread {
    private static final Logger logger = LoggerFactory
            .getLogger(HeartBeatThread.class);
    static HeartBeatThread obj;
    public static Date lastTime;

    public static HeartBeatThread getInstance() {
        if (obj == null) {
            synchronized (HeartBeatThread.class) {
                if (obj == null) {
                    obj = new HeartBeatThread();
                }
            }
            obj = new HeartBeatThread();
        }
        return obj;
    }

    private Timer timer = new Timer();

    /**
     * 重新发送消息
     *
     * @param period
     */

    @Override
    protected void runThread(long delay, long period) {
        timer = new Timer();
        try {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    logger.debug("给服务发送心跳");


                    MSG_0x02 m = new MSG_0x02();
                    MsgHeader header = new MsgHeader();
                    header.setSeq((short) 12);
                    header.setMac("12");
                    header.setSn("12");
                    m.setHead(header);
                    TcpMachineClient.getInstance().sendWithoutCache(m);

				/*	MSG_0x1001 msg = new MSG_0x1001();
                    VisitInfo info=new VisitInfo();
					info.setSysNo(1);
					info.setVisitName("王二");
					info.setSex(1);
					info.setImgBase64("dfafasssssssssssssssssssssssssssssssssssssssss");
					info.setSelfMachineNum("123456789");
					msg.setContent(JSON.toJSONString(info));
					TcpMachineClient.getInstance().sendWithoutCache(msg);*/
                }
            }, delay, period);
        } catch (Exception e) {
            logger.info("发送心跳消息异常：", e);
            e.printStackTrace();
        }

    }

    public void stop() {
        isRun = false;
        timer.cancel();
    }
}
