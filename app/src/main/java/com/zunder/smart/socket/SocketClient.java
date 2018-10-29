package com.zunder.smart.socket;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.json.MasterUtils;
import com.zunder.smart.json.ServiceUtils;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.Master;
import com.zunder.smart.model.ServiceInfo;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.socket.rak.RAKParams;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.SystemInfo;

/**
 * Created by Administrator on 2017/6/27.
 */

public class SocketClient {
	public static int  isLinkFlag=0;
	public static int plusNumber=0;
	public static int CloudTime=0;
	private Vector<String> datas = new Vector<String>();
	private final Object sendLock = new Object();
	private Socket socket;
	private OutputStream outStream = null;
	private InputStream inStream = null;
	private Thread conn = null;
	private Thread send = null;
	private Thread timmer = null;
	private Thread rec = null;
	private String host;
	private int port = -1;
	private int timeout = 3;
	private ICoallBack CoallBack = null;
	private ISocketPacket _packet = null;

	/**
	 * @param host
	 *            ip地址
	 * @paramport端口号
	 * @param timeOut
	 *            链接超市时间 秒
	 */
	public SocketClient(int timeOut) {
		if(SystemInfo.getSSID(MyApplication.getInstance()).startsWith("RAK")) {
			this.host="192.168.7.1";
			this.port=25000;
		}else{
			ServiceInfo info = ServiceUtils.getServiceInfo();
			this.host=info.getIP();
			this.port=info.getPort();
		}
		this.timeout = timeOut;
		timmer = new Thread(new Timer());
		timmer.start();

	}
	public boolean isConcet(){
		return socket.isConnected();
	}
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @param设置连接服务器监听
	 */
	public void setOnConnectListener(ICoallBack CoallBack) {
		this.CoallBack = CoallBack;
	}

	private String RequestHeader = "$$";// 2424
	private String RAKRequestHeader = "@@";// 2424
	// / <summary>
	// / 服务器请求包尾
	// / </summary>
	private String RequestEnding = "\r\n";// 0D0A

	public final int CONNECTSTART = 0;

	/** 连接成功 **/
	public final int CONNECTSUCCESS = 1;
	/** 连接失败 **/
	public final int CONNECTFAILURE = -1;
	/** 接收消息 **/
	public final int RECEIVEMESSAGE = 2;
	/** 发送消息 **/
	public final int SENDMESSAGE = 3;
	/** 发送成功 **/
	public final int SENDSUCCESS = 4;
	/** 发送失败 **/
	public final int SENDFAILURE = -2;
	/** 断开连接 **/
	public final int DISCONNECT = -5;
	private final int STATE_CONNECT_WAIT = 5;// 等待连接
	private int state = DISCONNECT;

	/**
	 * @param_packet设置接受消息监听
	 */
	public void setOnReceiveListener(ISocketPacket _packet) {
		this._packet = _packet;
	}

	/**
	 * 打开连接
	 * @return boolean
	 */
	public synchronized void Connection() {

		closeConnection();
		state = CONNECTSTART;
		conn = new Thread(new Conn());
		conn.start();
		isLinkFlag=5;
		plusNumber=0;
		CloudTime=0;
	}
	private class Conn implements Runnable {
		public void run() {
			try {
				try {
					socket = new Socket(host, port);
					socket.setSoTimeout(timeout * 1500);
					state = CONNECTSUCCESS;
				} catch (Exception e) {
					// TODO: handle exception
					state = CONNECTFAILURE;
				}
				if (state == CONNECTSUCCESS) {
					try {
						outStream = socket.getOutputStream();
						inStream = socket.getInputStream();

					} catch (Exception e) {
						e.printStackTrace();
					}
					isLinkFlag=10;
					plusNumber=1;
					send = new Thread(new Send());
					rec = new Thread(new Rec());
					rec.start();
					send.start();
					if (CoallBack != null) {
						CoallBack.OnSuccess(socket);
					}
				}
			} catch (Exception e1) {
				state = CONNECTFAILURE;
				isLinkFlag=5;
				plusNumber=0;
			}
		}
	}
	/**
	 * 关闭连接的输入输出流
	 *
	 * @author mick.ge
	 */
	public synchronized void closeConnection() {
		try {
			datas.clear();
			sendLock.notify();
			try {
				if (null != socket) {
					socket.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}

			try {
				if (null != outStream) {
					outStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
			try {
				if (null != inStream) {
					inStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}

			try {
				if (null != conn && conn.isAlive()) {
					conn.interrupt();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}

			try {
				if (null != send && send.isAlive()) {
					send.interrupt();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
			try {
				if (null != rec && rec.isAlive()) {
					rec.interrupt();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
			try {
				if(timmer!=null&&timmer.isAlive()){
					timmer.interrupt();
				}
			}catch (Exception e){

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(SystemInfo.getSSID(MyApplication.getInstance()).startsWith("RAK")) {
			this.host="192.168.7.1";
			this.port=25000;
		}else{
			ServiceInfo info = ServiceUtils.getServiceInfo();
			this.host=info.getIP();
			this.port=info.getPort();
		}
	}

	public boolean isNet(){
		try{
			socket.sendUrgentData(0xFF);
			return true;
		}
		catch (Exception e){
			return false;
		}
	}
	public void SenddData(String msg) {
		if(state==CONNECTSUCCESS) {
			synchronized (sendLock) {
				if (SystemInfo.getSSID(MyApplication.getInstance()).startsWith("RAK")) {
					datas.add(RAKRequestHeader + msg + RequestEnding);
				}else{
					datas.add(RequestHeader + msg + RequestEnding);
				}
				sendLock.notify();
			}
		}else{
			isLinkFlag=5;
			plusNumber=0;
			datas.clear();
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
							outStream.write(result.getBytes("UTF-8"));
							outStream.flush();
						} catch (Exception e) {
							sendLock.notify();
						}
					}
					synchronized (sendLock) {
						sendLock.wait();
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				if (state == CONNECTSUCCESS) {
					state = CONNECTFAILURE;
				}
				Log.e("socket", "send_error"+AppTools.getCurrentTime());
				isLinkFlag=5;
				plusNumber=0;
			}
		}
	}
	String SocketBuff = "";

	private class Rec implements Runnable {
		public void run() {
			Log.e("socket", "R_run"+AppTools.getCurrentTime());
			String strBuffer = "";
			byte[] b = new byte[1024];
			int len = 0;

			while (state==CONNECTSUCCESS) {
				try {
					if(inStream==null) {
						inStream = socket.getInputStream();
					}
					len = inStream.read(b);
					if (len > 0) {
						strBuffer = new String(b, 0, len, "UTF-8");
						strBuffer = SocketBuff + strBuffer;
						SocketBuff = "";
						String[] array = strBuffer.split("\n");
						if (array.length > 0) {
							for (int i = 0; i < array.length; i++) {
								strBuffer = array[i];
								if (strBuffer.length() > 0) {
									String LeadCode = strBuffer.substring(0, 2);
									if (LeadCode.equals("@@")||LeadCode.equals("$$")) {
										String EndCode = strBuffer
												.substring(strBuffer.length() - 1);
										if (EndCode.equals("\r")) {
											String tempStr = strBuffer
													.substring(2,strBuffer.length() - 1);
											if (_packet != null) {
												_packet.SocketPacket(tempStr);
											}
										} else {
											SocketBuff = array[i];
										}
									}
								}
							}
						}
					} else {
						if (len == -1) {
							state = DISCONNECT;
							isLinkFlag = 5;
							plusNumber = 0;
							if (CoallBack != null) {
								CoallBack.OnDisConnect(MyApplication.getInstance().getString(R.string.servicefail));
							}
						}
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				.getMaster(MainActivity.getInstance());
		String result = ISocketCode.setLine(msaterName.getMn(), msaterName.getPw(),
				msaterName.getOr(), msaterName.getOp(), 1);
		SenddData(result);
	}
	public void connectGateway(int IsSendCmd)	{
		List<GateWay> gateWayList = GateWayService.list;
		if (gateWayList != null && gateWayList.size() > 0) {
			for (int i = 0; i < gateWayList.size(); i++) {
				final GateWay gateWay = gateWayList.get(i);
				if (gateWay.getTypeId()<3) {
					Boolean SendCmdFlag=true;
					if(IsSendCmd==1){
						if (gateWay.getState().equals(MainActivity.getInstance()
								.getString(R.string.gateWayLine))) {
							SendCmdFlag=false;
						}
					}
					if (SystemInfo.getSSID(MyApplication.getInstance()).startsWith("RAK")) {
						_packet.SocketPacket(RAKParams.getParams("Connect",gateWay.getGatewayID(),100));
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
									if (SystemInfo.getSSID(MyApplication.getInstance()).startsWith("RAK")) {
										if (_packet != null) {
											_packet.SocketPacket(RAKParams.getParams("Login","00000000",1));
										}
									}else {
										Login();
									}
									Log.e("socket", "login_"+AppTools.getCurrentTime());
								}
								else
								{
									if (SystemInfo.getSSID(MyApplication.getInstance()).startsWith("RAK")) {
									Line();
									if (_packet != null) {
										_packet.SocketPacket(RAKParams.getParams("Line","00000000",1));
									}
								}else {
									Line();
								}
									Log.e("socket", "line"+AppTools.getCurrentTime());
								}
							}
						}
						if(isLinkFlag>10){
							CloudTime++;
							if(CloudTime>=120){
								CloudTime=0;
								connectGateway(0);
								Log.e("socket", "gateway_"+AppTools.getCurrentTime());
							}else{
								if(CloudTime==60){
									connectGateway(1);
									Log.e("socket", "gateway_"+AppTools.getCurrentTime());
								}
							}
						}
					}else{
						Log.e("socket", "connect_"+AppTools.getCurrentTime());
						Connection();
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