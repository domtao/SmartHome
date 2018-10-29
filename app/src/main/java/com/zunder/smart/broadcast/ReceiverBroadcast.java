package com.zunder.smart.broadcast;
//joe 18_0919
import java.util.List;

import org.json.JSONObject;

import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.aiui.info.AnHong;
import com.zunder.smart.aiui.info.BlueDevice;
import com.zunder.smart.aiui.info.SubscribeInfo;
import com.zunder.smart.dao.impl.factory.DeviceTypeFactory;
import com.zunder.smart.json.MasterUtils;
import com.zunder.smart.listener.AnHongListener;
import com.zunder.smart.listener.BlueToothListener;
import com.zunder.smart.listener.DownListener;
import com.zunder.smart.listener.GateWayListener;
import com.zunder.smart.listener.GetDeviceListener;
import com.zunder.smart.listener.HostListener;
import com.zunder.smart.listener.MachineCode;
import com.zunder.smart.listener.RemoteControlListener;
import com.zunder.smart.listener.SecurityListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.Master;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.VoiceInfo;
import com.zunder.smart.netty.MockLoginNettyClient;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.setting.ProjectUtils;
import com.zunder.smart.socket.SocketClient;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.socket.info.ResponseInfo;
import com.zunder.smart.socket.result.SocketMainResult;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.JSONHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

public class ReceiverBroadcast extends BroadcastReceiver {
	private String K_SERVICE = "Service";
	private static GateWayListener listener;
	private static AnHongListener anHongListener;
	public static void setSecurityListener(SecurityListener securityListener) {
		ReceiverBroadcast.securityListener = securityListener;
	}
	private static SecurityListener securityListener;
	public static void setAnHongListener(AnHongListener anHongListener) {
		ReceiverBroadcast.anHongListener = anHongListener;
	}
	private static BlueToothListener blueToothListener;
	/**
	 * @param listener
	 *            the listener to set
	 */
	public static void setListener(GateWayListener listener) {
		ReceiverBroadcast.listener = listener;
	}

	private static GetDeviceListener getDeviceListener;

	private static DownListener downListener;

	private static MachineCode machineCode;
	private static HostListener hostListener;
	private static RemoteControlListener remoteControlListener;

	public static void setMachineCode(MachineCode machineCode) {
		ReceiverBroadcast.machineCode = machineCode;
	}
	/**
	 * @param downListener
	 *            the downListener to set
	 */
	public static void setDownListener(DownListener downListener) {
		ReceiverBroadcast.downListener = downListener;
	}
	/**
	 * @param getDeviceListener
	 *            the getDeviceListener to set
	 */
	public static void setGetDeviceListener(GetDeviceListener getDeviceListener) {
		ReceiverBroadcast.getDeviceListener = getDeviceListener;
	}

	public static void setBlueToothListener(BlueToothListener blueToothListener) {
		ReceiverBroadcast.blueToothListener = blueToothListener;
	}

	public static void setHostListener(HostListener hostListener) {
		ReceiverBroadcast.hostListener = hostListener;
	}

	public static void setRemoteControlListener(RemoteControlListener remoteControlListener) {
		ReceiverBroadcast.remoteControlListener = remoteControlListener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String result = intent.getExtras().getString("str");
		// {"MasterID":"342E31DF34EA0F23","ToID":"00000000","MsgType":"Login","MsgState":1,"Content":"登录成功","CreateTime":"2017-07-16 10:34:21"}
		try {
			ResponseInfo info = JSONHelper.parseObject(result,
					ResponseInfo.class);

			String service = info.getMsgType();
			if (!TextUtils.isEmpty(service)) {
				SocketMainResult.ServiceType serviceType = SocketMainResult
						.getServiceType(service);

				switch (serviceType) {
					case LOGIN:
						if (info != null) {
							if (info.getMsgState() == 2) {
								showToast(info.getContent());
								ProjectUtils.getRootPath().setControlState(2);
							} else if(info.getMsgState() == 1){
								showToast(info.getContent());
								ProjectUtils.getRootPath().setControlState(0);
							}
						}
						if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) {
							MockLoginNettyClient.getInstans().isLinkFlag = 120;
							MockLoginNettyClient.getInstans().plusNumber = 30;
							MockLoginNettyClient.getInstans().CloudTime = 180;
							Log.e("NetCmd", AppTools.getCurrentTime()+"________nettyLogin");
						}else{
							SocketClient.isLinkFlag=120;
							SocketClient.plusNumber=30;
							SocketClient.CloudTime=180;
							Log.e("NetCmd",AppTools.getCurrentTime()+"________rakLogin");
						}
						break;
					case LINE:
						if (info != null) {
							if (info.getMsgState() == 1) {
								if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) {
									MockLoginNettyClient.getInstans().isLinkFlag = 120;	
									MockLoginNettyClient.getInstans().plusNumber = 30;
								}else{
									SocketClient.isLinkFlag = 120;
									SocketClient.plusNumber = 30;
								}
								Log.e("NetCmd",AppTools.getCurrentTime()+"________nettyLine");
							} else {
								showToast(context.getString(R.string.login_fail));
							}
						}
						break;
					case CONNECT:
						if (info != null && listener != null) {
							String toID = info.getToID();
							if (GateWayService.mp.containsKey(toID)) {
								listener.receiveGateWayStatus(1,Integer
												.parseInt(GateWayService.mp.get(toID)),
										info.getMsgState());
								Log.e("BCameraID","设备ID:"+toID);
							}
						}
						Log.e("NetCmd",AppTools.getCurrentTime()+"________nettyConn");
						break;

					case GETANHONGINFO:
						if (info != null) {
							// 1:火警1
							String[] results = info.getContent().split(":");
							if (results.length == 3) {
								AnHong anHong = new AnHong();
								anHong.setId(Integer.parseInt(results[0]));

								anHong.setMsgName(results[1]);
								anHong.setMsgInfo(results[2]);
								if (anHongListener != null) {
									anHongListener.getAnHong(anHong);
								}
							}
						}
						break;
					case FORWARD:
						if (info != null) {
							if (info.getMsgState() == 1) {
								showToast(info.getToID() +"网关"+context.getString(R.string.line));
							} else {
								showToast(info.getToID() + context.getString(R.string.gateWayUnLine));

								List<GateWay> list = GateWayService.list;
								if (list != null) {
									for (int i = 0; i < list.size(); i++) {
										final GateWay gateWay = list.get(i);
										if (gateWay.getGatewayID().equals(
												info.getToID())) {
											String result1 = ISocketCode
													.setConnect(
															gateWay.getGatewayID(),
															gateWay.getUserName(),
															gateWay.getUserPassWord(),
															gateWay.getIsCurrent());
											MainActivity.getInstance().sendCode(result1);

											break;
										}
									}
								}
							}
						}
						break;
					case RESPONSE:

						if (info != null) {
							//判斷是否當前网關列表
//							List<GateWay> gateWayList = GateWayService.list;
//							for (int i = 0; i < gateWayList.size(); i++) {
//								GateWay gateway = gateWayList.get(i);
//								int gatewayTypeID = gateway.getTypeId();
//								if(SystemInfo.getSSID(MyApplication.getInstance()).startsWith("RAK")&&gatewayTypeID==5) {
//									TcpSender.dataProcess(info.getContent());
//									break;
//								}else if (gateway.getGatewayID().equals(info.getToID()) && gatewayTypeID!=3) {
//										TcpSender.dataProcess(info.getContent());
//										break;
//								}
//							}
							TcpSender.dataProcess(info.getContent());
							if (machineCode != null) {
								machineCode.setCode(info);
							}
						}
						break;
					case ERROR:
						MainActivity.getInstance().freshFindDevice();
						break;
					case GETMODELIST:
						if (info != null) {
							String result1 = info.getContent();
							if (getDeviceListener != null) {
								getDeviceListener.getModeList(result1);
							}
						}
						break;
					case GETDEVICEINFO: {
						Log.d("infoDevice", info.getContent());
						String results = info.getContent();
						try {
							Device device = JSONHelper.parseObject(results, Device.class);

							if (device != null) {
								if (getDeviceListener != null) {
									getDeviceListener.getDevice(device);
								}
							} else {
								if (getDeviceListener != null) {
									getDeviceListener.getMessage(results);
								}
							}
						}catch (Exception e){
							if (getDeviceListener != null) {
								getDeviceListener.getMessage(results);
							}
						}
					}
						break;
					case GETMODEINFO: {
						String results= info.getContent();
						try {
						Mode mode=JSONHelper.parseObject(results,Mode.class);
						if (mode!=null) {
							if (getDeviceListener != null) {
								getDeviceListener.getMode(mode);
							}
						} else {
							if (getDeviceListener != null) {
								getDeviceListener.getMessage(results);
							}
						}
						}catch (Exception e){
							if (getDeviceListener != null) {
								getDeviceListener.getMessage(results);
							}
						}
					}
						break;
					case DOWNINFO:
						if (info != null) {
							if (downListener != null) {
								if(AppTools.isNumeric(info
										.getContent())) {
									downListener.count(info
											.getContent());
								}
							}
						}
						break;

					case APKINFO:
						if (info != null) {
							if (downListener != null) {
								downListener.count(info
										.getContent());
							}
						}

						break;
					case GETMASTERINFO:
						if (machineCode != null) {
							machineCode.setCode(info);
						}
						break;
					case GETVOICEINFO:
						if (info != null) {
							String[] voices = info.getContent().split(";");
							if (voices.length == 5) {
								VoiceInfo voice = new VoiceInfo();
								voice.setId(Integer.parseInt(voices[0]));
								voice.setVoiceName(voices[1]);
								voice.setVoiceAnswer(voices[2]);
								voice.setVoiceAction(voices[3]);
								voice.setUserName(voices[4]);
								if (getDeviceListener != null) {
									getDeviceListener.getVoice(voice);
								}
							} else {
								if (getDeviceListener != null) {
									getDeviceListener.getMessage(info.getContent());
								}
							}
						}
						break;
					case GETSUBSCRIBEINFO:
						if (info != null) {
							String[] voices = info.getContent().split(";");
							if (voices.length == 7) {
								SubscribeInfo voice = new SubscribeInfo();
								voice.setId(Integer.parseInt(voices[0]));
								voice.setSubscribeName(voices[1]);
								voice.setSubscribeDate(voices[2]);
								voice.setSubscribeTime(voices[3]);
								voice.setSubscribeEvent(voices[4]);
								voice.setSubscribeAction(voices[5]);
								voice.setUserName(voices[6]);
								if (getDeviceListener != null) {
									getDeviceListener.getSubscribe(voice);
								}
							} else {
								if (getDeviceListener != null) {
									getDeviceListener.getMessage(info.getContent());
								}
							}
						}
						break;
					case GETWIFIINFO:
						if (info != null) {
							JSONObject jsonObject=new JSONObject(info.getContent());
							Master master=new Master();
							master.setMn(jsonObject.getString("MasterName"));
							master.setMc(jsonObject.getString("MasterMac"));
							master.setWf(jsonObject.getString("MasterWiFi"));
							MasterUtils.addDevice(master);
						}
						break;
					case SECURITYINFO:
						if (info != null) {
						if(securityListener!=null){
							securityListener.getSecurity(info.getContent());
						}
						}
						break;
					case GETBLUETOOTHINFO:
						if(info!=null){
							String[] content=info.getContent().split(":");
							if(content[0].equals("device")) {
								if (blueToothListener != null) {
									BlueDevice blueDevice=new BlueDevice();
									blueDevice.setDeviceName(content[1]);
									blueDevice.setAddress(content[2]);
									blueDevice.setDeviceType(Integer.parseInt(content[3]));
									blueDevice.setBoundState(Integer.parseInt(content[4]));
									blueDevice.setConnectState(0);
									blueToothListener.SearchDevice(blueDevice);
								}
							}else if(content[0].equals("connect")){
								if (blueToothListener != null) {
									blueToothListener.OnConnectState(content[1],Integer.parseInt(content[2]));
								}
							}else if(content[0].equals("bound")){
								if (blueToothListener != null) {
									blueToothListener.OnBondState(content[1],Integer.parseInt(content[2]));
								}
							}
						}
						break;
					case GETGATEWAYINFO:
						if(info!=null){
							if(hostListener!=null){
								hostListener.getMsg(info.getContent());
							}
						}
					break;
					case REMOTECONTROLINFO:
						if (remoteControlListener != null) {
							remoteControlListener.setMsg(info.getMasterType(),info.getContent());
						}
						break;
					default:
						break;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showToast(String message) {
		MainActivity.getInstance().showToast(message, 5000);

	}
}
