package socket.netty.machine.thread;

import cn.com.reformer.netty.msg.ClientMsgQueue;
import cn.com.reformer.netty.msg.ReceivePackBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 线程池（处理消息）管理
 *
 * @author sid
 */
public class ParseMsgThreadManager extends AbsThread {

    private static final Logger logger = LoggerFactory.getLogger(ParseMsgThreadManager.class);

    static ParseMsgThreadManager obj;

    public static ParseMsgThreadManager getInstance() {
        if (obj == null)
            obj = new ParseMsgThreadManager();
        return obj;
    }

    private ThreadPoolExecutor threadPool;

    private volatile boolean isStart = true;

    public ParseMsgThreadManager() {
        int corePoolSize = 24;
        int maximunPoolSize = 100;
        int keepAliveTime = 4;
        threadPool = new ThreadPoolExecutor(corePoolSize, maximunPoolSize,
                keepAliveTime, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(corePoolSize),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    protected void runThread(long delay, long period) {
        isRun = true;
        isStart = true;
        new Thread(new ParseThreadManage()).start();
        logger.info("client消息处理线程启动完成");

    }

    class ParseThreadManage implements Runnable {

        public void run() {
            while (isStart) {
                ReceivePackBean rpb = null;
                try {
                    LinkedBlockingQueue<ReceivePackBean> recqueue = ClientMsgQueue.getRecqueue();
                    if (null != recqueue) {
                        ReceivePackBean take = recqueue.take();
                        if (null != take) {
                            rpb = take;
                            threadPool.execute(new ParseMsgThread(rpb));
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("消息解析管理线程运行异常", e);
                    e.printStackTrace();
                }
            }

        }

    }

    @Override
    public void stop() {
        isRun = false;
        isStart = false;
    }

}
