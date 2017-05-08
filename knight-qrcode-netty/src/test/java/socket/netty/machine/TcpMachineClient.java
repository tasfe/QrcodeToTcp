package socket.netty.machine;

import cn.com.reformer.netty.handler.TCPMessageDecoder;
import cn.com.reformer.netty.handler.TCPMessageEncoder;
import cn.com.reformer.netty.msg.AbsMsg;
import cn.com.reformer.netty.util.Converter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import socket.netty.machine.thread.HeartBeatThread;
import socket.netty.machine.thread.ParseMsgThreadManager;
import socket.netty.machine.thread.ReSendMsgThread;

public class TcpMachineClient extends Thread {

    private volatile static TcpMachineClient obj;

    public static TcpMachineClient getInstance() {
        if (obj == null) {
            synchronized (TcpMachineClient.class) {
                if (obj == null) {
                    obj = new TcpMachineClient();
                }
            }
            obj = new TcpMachineClient();
        }
        return obj;
    }

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(TcpMachineClient.class);

    Channel ch;
    ChannelInboundHandlerAdapter handler;
    Bootstrap b;
    Channel channel;

    private boolean isLogined = true; // 是否登陆成功
    public ChannelHandlerContext chtx;
    public ChannelFuture cf;

    private int heartbeatdelay = 2;// 心跳间隔
    private int reconnectdealy = 5;
    private int resendmsgdealy = 5;
    private int connstate = 0;

    public void reconnect() {
        connstate = 0;
        logger.info("数据端：断线重连线程，当前状态：" + (this.connstate == 1 ? "连接中" : "已断线"));
        if (this.connstate != 1) {
            try {
                init();
            } catch (Exception e) {
                logger.error("client断线重连初始化失败：", e);
                e.printStackTrace();
            }
        }

    }

    @Override
    public void run() {
        init();
        //HeartBeatThread.getInstance().run(0,this.heartbeatdelay * 1000);
    }

    private void init() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
//			final LogLevel loglevel = LogLevel.valueOf(p.getProperty("_loglevel").toUpperCase());
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(new TCPMessageDecoder(),
                                    new TCPMessageEncoder(),
                                    new LoggingHandler(LogLevel.INFO),
                                    new TcpMachineClientHandler());
                        }
                    });

            b.option(ChannelOption.SO_KEEPALIVE, true);
            cf = b.connect("127.0.0.1",
                    Integer.parseInt("9991")).sync();
            // cf.channel().closeFuture().sync();
            ParseMsgThreadManager.getInstance().run(0, 0);
            Thread.sleep(2000);
            HeartBeatThread.getInstance().run(1, this.heartbeatdelay * 1000);

            logger.info("client线程启动成功");
        } catch (InterruptedException e) {
            logger.error("client初始化失败：", e); //$NON-NLS-1$
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("client初始化失败：", e); //$NON-NLS-1$
            e.printStackTrace();
        } finally {


            // group.shutdownGracefully();
        }
    }


    /**
     * 设置登陆状态
     *
     * @param b
     */
    public void loginOK(boolean b) {

        this.isLogined = b;
        //如果是第一次登录成功
        if (this.isLogined) {
            this.connstate = 1;
            ReSendMsgThread.getInstance().run(0, this.resendmsgdealy * 1000);
            HeartBeatThread.getInstance().run(0, this.heartbeatdelay * 1000);
            logger.info("client线程启动成功");

        }
    }

    /**
     * 断开连接，关闭服务
     */
    public void stopClient() {
        ReSendMsgThread.getInstance().stop();
        HeartBeatThread.getInstance().stop();
        ParseMsgThreadManager.getInstance().stop();
        chtx.close();
        this.isLogined = false;
        this.connstate = 0;
    }

    /**
     * 发送消息
     *
     * @param m
     */
    public void send(AbsMsg m) {
        if (this.isLogined) {
            logger.debug("CLINET发送：" + m.toString());
            if (chtx != null && chtx.channel().isOpen()) {
                chtx.write(m);
                chtx.flush();
            }
        }
    }

    /**
     * 只发消息不缓存，用于心跳类消息
     *
     * @param m
     */
    public void sendWithoutCache(AbsMsg m) {
        if (isLogined) {
            logger.debug("CLINET发送WithoutCache：" + Converter.bytes2HexsSpace(m.toBytes()));
            if (chtx != null && chtx.channel().isOpen()) {
                chtx.write(m);
                chtx.flush();
            }
        }
    }

    /**
     * login:(发送登陆消息).
     *
     * @author sid
     */
    public void login() {

        // 打开连接时发送登录消息
    /*	try {
			MSG_0x01 m = new MSG_0x01();

			m.setData(DateUtils.formatDate(new Date(), "YYYYMMDDHHss"));
			JSONObject object=new JSONObject();
			object.put("userName","12345667");
			object.put("password","123456");
			object.put("userType",1);

			if (chtx != null && chtx.channel().isOpen()) {
				chtx.writeAndFlush(m);
				logger.info("-------------发送登录消息--------------"+m.toString());
			}



		} catch (Exception e) {
			logger.error("Client :login() - Exception",e); //$NON-NLS-1$
			e.printStackTrace();
		}*/
    }

    public ChannelHandlerContext getChtx() {
        return chtx;
    }

    public void setChtx(ChannelHandlerContext chtx) {
        this.chtx = chtx;
    }

    public ChannelFuture getCf() {
        return cf;
    }

    public void setCf(ChannelFuture cf) {
        this.cf = cf;
    }

    public int getConnstate() {
        return connstate;
    }

    public void setConnstate(int connstate) {
        this.connstate = connstate;
    }

    public boolean isLogined() {
        return isLogined;
    }

    public void setLogined(boolean isLogined) {
        this.isLogined = isLogined;
    }
}
