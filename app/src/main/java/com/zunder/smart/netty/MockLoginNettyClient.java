package com.zunder.smart.netty;

import android.util.Log;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.json.MasterUtils;
import com.zunder.smart.json.ServiceUtils;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.Master;
import com.zunder.smart.model.ServiceInfo;
import com.zunder.smart.netty.message.MessageDecoder;
import com.zunder.smart.netty.message.MessageEncoder;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.socket.ICoallBack;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.socket.rak.RAKParams;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.SystemInfo;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Vector;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 模拟智能设备登录模块netty客户端
 */

public class MockLoginNettyClient {
    Channel channel=null;
    private int readerIdleSeconds = 0;
    private Thread timmer = null;
    private Thread send = null;
    private int writerIdleSeconds = 0;
    private int allIdleSeconds= 300;
    public  int  isLinkFlag=0;
    public  int plusNumber=0;
    public  int CloudTime=0;
    private ICoallBack CoallBack = null;

    private Vector<String> datas = new Vector<String>();
    private final Object sendLock = new Object();
    public void setConnectorHost(String connectorHost) {
        this.connectorHost = connectorHost;
    }
    private static  String connectorHost="112.74.64.82:2017";
    public static MockLoginNettyClient instans;

    public MockLoginNettyClient(String  connectorHost){
        this.connectorHost=connectorHost;
    }
    public static synchronized MockLoginNettyClient getInstans(){
        if(instans==null){
            ServiceInfo info = ServiceUtils.getServiceInfo();
            String loginHost = info.getIP()+":"+info.getPort();
            if(SystemInfo.getSSID(MyApplication.getInstance()).startsWith("RAK")) {
                loginHost="192.168.7.1:25000";
            }
            instans=new MockLoginNettyClient(loginHost);
        }
        return instans;
    }
    public boolean  isConnect(){
        if(channel!=null&&channel.isActive()) {
            return  false;
        }
        return true;
    }
    public void disConnect(){
        if(channel!=null) {
            datas.clear();
            try {
                channel.disconnect();
                channel=null;
            }catch (Exception e){
            }
        }
        if(SystemInfo.getSSID(MyApplication.getInstance()).startsWith("RAK")) {
            connectorHost="192.168.7.1:25000";
        }else{
            ServiceInfo info = ServiceUtils.getServiceInfo();
            connectorHost = info.getIP()+":"+info.getPort();
        }
    }
    public void connect() {
        disConnect();
        if (StringUtils.isNotBlank(connectorHost) && connectorHost.contains(":")) {
            String[] host = connectorHost.split(":");
            if (host.length == 2) {
                channel= connect(host[0], Integer.valueOf(host[1]));
                if(channel!=null){
                    isLinkFlag=10;
                    plusNumber=1;
                    if (CoallBack != null) {
                        CoallBack.OnSuccess(null);
                        if(timmer==null) {
                            timmer = new Thread(new Timer());
                            timmer.start();
                        }
                        if(send==null) {
                            send = new Thread(new Send());
                            send.start();
                        }
                    }
                }else{
                    isLinkFlag=5;
                    plusNumber=0;
                }
            }
        }
    }
    public Channel connect(String host, int port) {
        try {
            EventLoopGroup group = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.CONNECT_TIMEOUT_MILLIS,3000).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    // receive
                                    .addLast(new LineBasedFrameDecoder(Integer.MAX_VALUE))
                                    .addLast(new MessageDecoder())
                                    // send
                                    .addLast(new MessageEncoder())
                                    .addLast("timeout", new IdleStateHandler(readerIdleSeconds, writerIdleSeconds, allIdleSeconds))
                                    // handler
                                    .addLast(new DeviceChannelInboundHandler());
                        }
                    });
            // 发起异步连接操作
            Channel channel = b.connect(host, port).sync().channel();
            return channel;
        } catch (Exception e) {

        }
        return null;
    }
    public void SenddData(String msg) {
        if(channel!=null) {
            synchronized (sendLock) {
                datas.add(msg);
                sendLock.notify();
            }
        }else{
            isLinkFlag=5;
            plusNumber=0;
        }
    }

    private class Send implements Runnable {
        public void run() {
            Log.e("socket", "S_run"+ AppTools.getCurrentTime());
            try {
                while (true) {
                    while (datas.size() > 0) {
                        try {
                            String result = datas.remove(0);
                            channel.writeAndFlush(result).sync();
                        } catch (Exception e) {

                        }
                    }
                    synchronized (sendLock) {
                        sendLock.wait();
                    }
                }
            } catch (Exception e1) {
            isLinkFlag=5;
             plusNumber=0;
            }
        }
    }
    public void Login(){
        Master masterL = MasterUtils.getMaster(MyApplication.getInstance());
        SenddData(ISocketCode.setLogin(masterL.getMn(), masterL.getPw(),
                masterL.getOr(), masterL.getOp(), 1));
    }
    public void Line(){
        Master msaterName = MasterUtils
                .getMaster(MyApplication.getInstance());
        String result = ISocketCode.setLine(msaterName.getMn(), msaterName.getPw(),
                msaterName.getOr(), msaterName.getOp(), 1);
        SenddData(result);
    }
    public void quite(){
        Log.e("NetCmd",AppTools.getCurrentTime()+"________quit");
        String result = ISocketCode.setForwardOut();
        SenddData(result);
        if(channel!=null) {
            datas.clear();
            try {
                channel.disconnect();
                channel=null;
            }catch (Exception e){
            }
        }
    }

    public void connectGateway(int IsSendCmd)	{
        List<GateWay> gateWayList = GateWayService.list;
        if (gateWayList != null && gateWayList.size() > 0) {
            for (int i = 0; i < gateWayList.size(); i++) {
                final GateWay gateWay = gateWayList.get(i);
                if (gateWay.getTypeId() <3) {
                    Boolean SendCmdFlag=true;
                    if(IsSendCmd==1){
                        if (gateWay.getState().equals(MyApplication.getInstance()
                                .getString(R.string.gateWayLine))) {
                            SendCmdFlag=false;
                        }
                    }
                    if(SendCmdFlag) {
                        if(SystemInfo.getSSID(MyApplication.getInstance()).startsWith("RAK")) {
                            if(gateWay.getTypeId()==1) {
                                String resultStr = RAKParams.getParams("Connect", gateWay.getGatewayID(), 100);
                                MyApplication.getInstance().sendBroadCast(resultStr);
                            }else{
                                String resultStr= RAKParams.getParams("Connect",gateWay.getGatewayID(),9);
                                MyApplication.getInstance().sendBroadCast(resultStr);
                            }
                        }else {
                            String resultCode = ISocketCode.setConnect(
                                    gateWay.getGatewayID(),
                                    gateWay.getUserName(),
                                    gateWay.getUserPassWord(),
                                    gateWay.getIsCurrent());
                            SenddData(resultCode);
                        }
                    }
                }
            }
        }
    }

    public void setCoallBack(ICoallBack coallBack) {
        CoallBack = coallBack;
    }


    private class Timer implements Runnable {
        public void run() {
            while (true) {
                try {
                    if(isLinkFlag>0){
                        isLinkFlag--;
                        if(plusNumber>0){
                            plusNumber--;
                            if(plusNumber==0){
                                if(isLinkFlag<10)
                                {
                                    Log.e("NetCmd",AppTools.getCurrentTime()+"________login");
                                    if(SystemInfo.getSSID(MyApplication.getInstance()).startsWith("RAK")) {
                                        String resultStr= RAKParams.getParams("Login","00000000",1);
                                        MyApplication.getInstance().sendBroadCast(resultStr);
                                    }else{
                                        Login();
                                    }
                                }
                                else
                                {
                                    Log.e("NetCmd",AppTools.getCurrentTime()+"________line");
                                    if(SystemInfo.getSSID(MyApplication.getInstance()).startsWith("RAK")) {
                                        Line();
                                        String resultStr= RAKParams.getParams("Line","00000000",1);
                                        MyApplication.getInstance().sendBroadCast(resultStr);
                                }else{
                                    Line();
                                    }
                                }
                            }
                        }
                        if(isLinkFlag>10){
                            CloudTime++;
                            if(CloudTime>=120){
                                Log.e("NetCmd",AppTools.getCurrentTime()+"________connecteAway");
                                CloudTime=0;
                                connectGateway(0);
                            }else{
                                if(CloudTime==60){
                                    Log.e("NetCmd",AppTools.getCurrentTime()+"________connecteAway");
                                    connectGateway(1);
                                }
                            }
                        }
                    }else{
                        connect();
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
