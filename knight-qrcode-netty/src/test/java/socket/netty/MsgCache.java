package socket.netty;

/**
 * Created by feixiang on 2016-09-23.
 */

import cn.com.reformer.netty.cache.MsgObj;
import cn.com.reformer.netty.msg.AbsMsg;
import cn.com.reformer.netty.msg.MessageID;
import cn.com.reformer.netty.msg.MsgHeader;
import cn.com.reformer.netty.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ��Ϣ����
 *
 * @author sid
 */
public class MsgCache {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(MsgCache.class);

    static MsgCache obj;

    private ConcurrentHashMap cache = new ConcurrentHashMap();

    public static MsgCache getInstance() {
        if (obj == null)
            obj = new MsgCache();
        return obj;
    }


    public MsgCache() {
    }

    /**
     * �����Ϣ
     *
     * @param msg
     */
    public void put(AbsMsg msg) {

        //ָ�����ֲ���Ҫ�������Ϣ
        if (msg.getHead().getCmd() == MessageID.MSG_0x01
                || msg.getHead().getCmd() == MessageID.MSG_0x02) {
            return;
        }
        String key = getMsgKey(msg);

        MsgObj m = this.get(key);
        if (m == null)
            m = new MsgObj(msg);
        else {
            m.setSendTime(new Date());
            m.setSendedCount(m.getSendedCount() + 1);
        }
        AbsMsg am = m.getMsg();
        this.cache.put(getMsgKey(am), msg);

        if (logger.isDebugEnabled()) {
            logger.debug("��Ϣ���뻺�棡��Ϣkey:" + key);
        }
    }

    /**
     * remove:�Ƴ���Ϣ
     *
     * @param key
     * @author sid
     */
    public void remove(String key) {
        this.cache.remove(key);
    }

    /**
     * ���ݷ�����Ϣ ��ˮ��
     *
     * @param key
     * @return
     */
    public AbsMsg getMsg(String key) {
        Object e = this.cache.get(key);
        MsgObj m = e == null ? null : (MsgObj) e;
        return m == null ? null : m.getMsg();

    }

    /**
     * ���ݷ�����Ϣ ��ˮ��
     *
     * @param key
     * @return
     */
    public MsgObj get(String key) {
        Object e = this.cache.get(key);
        return e == null ? null : (MsgObj) e;

    }

    /**
     * cleanAndgetResendMsg:(�������е���Ϣ��ͬʱ����Ҫ���·��͵���Ϣ����).
     *
     * @param minInterval ��С���ʱ�䣬����Ϊ��λ
     * @param maxCount    ����ط�����
     * @param maxTime     �����ʱ��(Сʱ)
     * @return
     * @author sid
     */
    @SuppressWarnings("unchecked")
    public List<MsgObj> cleanAndgetResendMsg(int minInterval, int maxCount,
                                             int maxTime) {
        List<MsgObj> list = new ArrayList<MsgObj>();

        Enumeration<String> keys = cache.keys();
        Date date = new Date();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            MsgObj obj = MsgCache.getInstance().get(key);
            Date endtime = DateUtils.addDateHour(obj.getCreateTime(),
                    maxTime);

            if (obj.getSendedCount() < maxCount
                    && endtime.getTime() > date.getTime()) {
                long lasttime = obj.getSendTime().getTime();
                long now = System.currentTimeMillis();
                if ((now - lasttime) > (minInterval * 1000)) {
                    list.add(obj);
                }
            } else
                MsgCache.getInstance().remove(key);
        }
        return list;

    }


    /**
     * ������Ϣ����������Ϣid
     *
     * @param m
     * @return
     */
    public static String getMsgKey(AbsMsg m) {
        return m.getHead().getCmd() + ";" + m.getHead().getSeq();
    }

    /**
     * ������Ϣͷ������Ϣid
     *
     * @param header
     * @return
     */
    public static String getMsgKey(MsgHeader header) {
        return header.getCmd() + ";" + header.getSeq();
    }
}
