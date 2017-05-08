package cn.com.reformer.netty.msg;


import cn.com.reformer.netty.util.Constants;
import cn.com.reformer.netty.util.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;

/**
 *  Copyright 2017 the original author or authors hangzhou Reformer 
 * @Description: ${todo}(这里用一句话描述这个类的作用)
 * @author zhangjin
 * @create 2017-05-08
**/
public class MsgHeader implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(MsgHeader.class);
    private static final long serialVersionUID = 1L;
    private short seq;//包序号2+1
    private String mac;//mac   12+1
    private String sn;//接入平台标识  9+1

    private short cmd;// 2+1
    private int length;//剩余消息总长度


    public byte[] tobytes() {

        byte[] data = new byte[Constants.HEAD_LENGTH];
        try {
            int offset = 0;
           String str_seq= Converter.toHexsFormat(Converter.toByteArray16Int(this.seq));
            System.arraycopy(str_seq.getBytes(), 0, data, offset, 2);
            offset += 2;
            System.arraycopy(new byte[]{(byte) 0x2C}, 0, data, offset, 1);
            offset += 1;
            System.arraycopy(Converter.fillDataStrToByte(this.mac, 12), 0, data, offset, 12);

            offset += 12;
            System.arraycopy(new byte[]{(byte) 0x2C}, 0, data, offset, 1);

            offset += 1;
            System.arraycopy(Converter.fillDataStrToByte(this.sn, 9), 0, data, offset, 9);

            offset += 9;
            System.arraycopy(new byte[]{(byte) 0x2C}, 0, data, offset, 1);
            offset += 1;
            byte[] src = Converter.shortToByteArray(getCmd());
            String src1 = Converter.toHexsFormat(src);
            System.arraycopy(src1.getBytes(), 0, data, offset, 2);

        } catch (Exception e) {
            logger.error("convert header  toBytes error", e);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * @param data
     * @return
     */
    public boolean frombytes(byte[] data) {
        boolean resultState = false;
        int offset = 0;
        try {
            byte[] byte_seq = new byte[2];
            byte_seq = Arrays.copyOfRange(data, offset, offset + 2);

            String s = Converter.toGBKString(byte_seq);
            this.seq = Short.parseShort(s, 16);
            offset += 3;
            this.mac = Converter.toGBKString(data, offset, offset + 12);
            offset += 13;
            this.sn = Converter.toGBKString(data, offset, offset + 9);
            offset += 10;
            byte[] byte_cmd = new byte[2];
            byte_cmd = Arrays.copyOfRange(data, offset, offset + 2);
            String s1 = Converter.asciiBytesToString(byte_cmd);
            this.cmd = Short.parseShort(s1,16);
            resultState = true;
        } catch (Exception e) {
            logger.error("get header from bytes error", e);
            e.printStackTrace();
        }
        return resultState;
    }

    public void setSeq(short seq) {
        this.seq = seq;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "MsgHeader [  " + ", seq=" + seq + ", mac=" + mac
                + ", cmd=" + cmd + "]";
    }

    public short getSeq() {
        return seq;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }


    public short getCmd() {
        return cmd;
    }

    public void setCmd(short cmd) {
        this.cmd = cmd;
    }
}
