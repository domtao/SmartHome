package com.zunder.smart.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bluecam.api.CameraPlayActivity;
import com.bluecam.api.utils.Contants;
import com.bluecam.bluecamlib.BCamera;
import com.bluecam.bluecamlib.Camera;
import com.bluecam.bluecamlib.CameraBean;
import com.bluecam.bluecamlib.CameraManager;
import com.bluecam.bluecamlib.listener.CameraEventListener;
import com.door.sdk.SettingListener;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.aiui.activity.AiuiMainActivity;
import com.zunder.smart.broadcast.ReceiverBroadcast;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.ContactFactory;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.listener.*;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.setting.ProjectUtils;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.socket.rak.RAKParams;
import com.zunder.smart.tools.SystemInfo;
import com.p2p.core.P2PHandler;

import hsl.p2pipcam.nativecaller.DeviceSDK;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class GateWayService extends Service implements DeviceStatusListener,
		DevicePramsListener, SnapShotListener, GateWayListener,CameraEventListener {
	String stata = "";
	public static long userid;
	public static List<GateWay> list = new ArrayList<GateWay>();
	public static List<String> list_userid = new ArrayList<String>();
	public static List<String> link_userid = new ArrayList<String>();
	public static Map<String, String> mp = new HashMap<String, String>();
	private static CameraHandleListener cameraHandleListener = null;
	private static GateWayService instance;
	public static int[] serial = new int[200];;
	private CameraManager cameraManager;
	public static GateWayService getInstance() {
		if (null == instance) {
			instance = new GateWayService();
		}
		return instance;
	}

	@Override
	public IBinder onBind(Intent intent) {
		instance = this;
		BridgeService.setDeviceStatusListener(this);
		BridgeService.setDevicePramsListener(this);
		BridgeService.setSnapShotListener(this);
		ReceiverBroadcast.setListener(this);
		SettingListener.setListener(this);
		cameraManager = CameraManager.getDeviceManager(this.getApplicationContext());
		cameraManager.registerEventListener(this);
		return new CameraBinder();
	}


	public class CameraBinder extends Binder {
		public void startFindDevice() {
			try{
				init();
			} catch (Exception e) {
			}
		}
		public void freshFindDevice() {
			try {
				reshListView();
			} catch (Exception e) {
			}
		}
		public void stopFindDevice() {

			if (list!=null&&list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					int userid = Integer.parseInt(mp
							.get(list.get(i).getGatewayID()));
					if (list.get(i).getTypeId() ==200 && userid > 0) {
						DeviceSDK.closeDevice(userid);
						DeviceSDK.destoryDevice(userid);
					}
				}
				if(cameraManager!=null)
				{
					cameraManager.remove();
				}
				list.clear();
				list_userid.clear();
				link_userid.clear();
				mp.clear();
			}
		}
	}

	public static void setCameraHandleListener(
			CameraHandleListener cameraHandleListener) {
		GateWayService.cameraHandleListener = cameraHandleListener;
	}
	private void init() {
		list.clear();
		list_userid.clear();
		link_userid.clear();
		mp.clear();
		List<GateWay> sqlList = GateWayFactory.getInstance().getAll();
		if (sqlList != null) {
			if (sqlList.size() > 0) {
				for (int i = 0; i < sqlList.size(); i++) {
					GateWay tempWay = sqlList.get(i);
					String wayName = tempWay.getGatewayName();
					final String did = tempWay.getGatewayID();
					final String userName = tempWay.getUserName();
					final String passWord = tempWay.getUserPassWord();
					int typeID = tempWay.getTypeId();
					int isCurrent = tempWay.getIsCurrent();
					userid = DeviceSDK.createDevice(userName, passWord, "", 0,
							did, 1);
					mp.put(did, userid + "");
					list_userid.add(userid + "");
					if (typeID <=3) {
						if(SystemInfo.getSSID(MyApplication.getInstance()).startsWith("RAK")) {
							if(typeID==1) {
								String resultStr = RAKParams.getParams("Connect", did, 100);
								MyApplication.getInstance().sendBroadCast(resultStr);
							}else{
								String resultStr = RAKParams.getParams("Connect", did, 9);
								MyApplication.getInstance().sendBroadCast(resultStr);
							}
						}else {
							String result = ISocketCode.setConnect(did, userName,
									passWord, isCurrent);
							MainActivity.getInstance().sendCode(result);
						}
						if (isCurrent == 1) {
							ProjectUtils.getRootPath().setRootID(did);
							ProjectUtils.saveRootPath();
						}
					}else if (typeID == 6) {
						BCamera bCamera=new Camera();
						CameraBean camBean=new CameraBean();
						camBean.setDevID(did);
						camBean.setUsername(userName);
						camBean.setPassword(passWord);
						camBean.setDevName(wayName);
						camBean.setDeviceType(typeID);
						bCamera.setCameraBean(camBean);
						bCamera.setStatus(0);
						cameraManager.addCamera(bCamera);
						Log.e("BCamera","开始连接");
					}
					GateWay gateWay = new GateWay();
					gateWay.setId(tempWay.getId());
					gateWay.setUserid((int) userid);
					gateWay.setGatewayName(wayName);
					gateWay.setState(getString(R.string.gateLink));
					gateWay.setUserName(userName);
					gateWay.setGatewayID(did);
					gateWay.setUserPassWord(passWord);
					gateWay.setTypeId(typeID);
					gateWay.setIsCurrent(isCurrent);
					gateWay.setPicData(tempWay.getPicData());
					list.add(gateWay);
				}
				for (int i = 0; i < list.size(); i++) {
					String deviceId=list.get(i).getGatewayID();
					if(mp.containsKey(deviceId)) {
						int userid = Integer.parseInt(mp.get(deviceId));
						if (list.get(i).getTypeId()==200&& userid > 0) {
							DeviceSDK.openDevice(userid);
						}
					}
				}
			}
			handler.sendEmptyMessage(2);
		}
	}

	private void reshListView() {
		Message msg = handler.obtainMessage(2, 1, 1);
		handler.sendMessage(msg);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				stata = "";
				switch (msg.arg1) {
					case 0:
						TcpSender.isGateWay = false;
						stata = getString(R.string.gateLink);
						break;
					case 1:
						TcpSender.isGateWay = true;
						stata = getString(R.string.gateUserError);
						break;
					case 2:
						stata = getString(R.string.gateUserCount);
						break;
					case 3:
						stata = getString(R.string.gateStream);
						break;
					case 4:
						stata = getString(R.string.videoLose);
						break;
					case 5:
						TcpSender.isGateWay = false;
						stata = getString(R.string.gateInvalidId);
						break;
					case 6:
						break;
					case 7:
						break;
					case 8:
						stata = getString(R.string.gateLinkLose);
						break;
					case 9:
						TcpSender.isGateWay = true;
						stata = getString(R.string.gateWayUnLine);
						break;
					case 10:
						stata = getString(R.string.gateLinkPass);
						break;
					case 11:
						stata = getString(R.string.lose);
						break;
					case 12:
						stata = getString(R.string.checkuser);
						break;
					case 101:
						stata = getString(R.string.gateLinkError);
						break;
					case 100:
						TcpSender.isGateWay = true;
						stata = getString(R.string.gateWayLine);
						break;
				}
				for (int i = 0; i < list_userid.size(); i++) {
					int getid = Integer.parseInt(list_userid.get(i));
					if (msg.arg2 == getid) {
						if(list==null||i>=list.size()){
							break;
						}
						GateWay cb = list.get(i);
						if (!cb.getState().equals(stata)) {
							if (cb.getTypeId() < 3) {
								if (cb.getIsCurrent() == 1) {
									MainActivity.getInstance().setTip(cb.getGatewayName()+stata);
								}
							} else {
								if (msg.arg1 == 100) {
									DeviceSDK.getDeviceParam(getid, 0x2713);
									if (cb.getUserName() == "") {
										DeviceSDK.getDeviceParam(getid, 0x2003);
									}
									DeviceSDK.getDeviceParam(getid, 0x270e);
								}
								if (cb.getIsCurrent() == 1) {
									MainActivity.getInstance().setTip(cb.getGatewayName()+stata);
								}
							}
							cb.setState(stata);
							if (cameraHandleListener != null) {
								cameraHandleListener.changeList();
							}
						}else{
							if (cb.getIsCurrent() == 1) {
								MainActivity.getInstance().setTip(cb.getGatewayName()+stata);
							}
						}
					}
				}
			} else if (msg.what == 1) {
				if(list!=null&&list.size()>0) {
					if (list.size() == list_userid.size()) {
						for (int i = 0; i < list_userid.size(); i++) {
							int getid = Integer.parseInt(list_userid.get(i));
							if (msg.arg2 == getid) {
								link_userid.remove(getid + "");
								link_userid.add(getid + "");
								GateWay cb = list.get(i);
								cb.setPicData((Drawable) msg.obj);
								if (cameraHandleListener != null) {
									cameraHandleListener.changeList();
								}
							}
						}
					}else{
						init();

					}
				}
			} else if (msg.what == 2) {
				for (int j = 0; j < list.size(); j++) {
					GateWay gateWay = list.get(j);
					if (gateWay.getTypeId()<3) {
						gateWay.setState(getString(R.string.gateLink));
						String deviceID = SystemInfo
								.getMasterID(MyApplication.getInstance());
						if(SystemInfo.getSSID(MyApplication.getInstance()).startsWith("RAK")) {
							if(gateWay.getTypeId() ==1) {
								String resultStr = RAKParams.getParams("Connect", gateWay.getUserName(), 100);
								MyApplication.getInstance().sendBroadCast(resultStr);
							}else{
								String resultStr = RAKParams.getParams("Connect", gateWay.getUserName(), 9);
								MyApplication.getInstance().sendBroadCast(resultStr);
							}
						}else {
							String result = ISocketCode.setConnect(
									gateWay.getGatewayID(), gateWay.getUserName(),
									gateWay.getUserPassWord(),
									gateWay.getIsCurrent());
							MainActivity.getInstance().sendCode(result);
						}
					}else {
						String deviceId=gateWay.getGatewayID();
						if(mp.containsKey(deviceId)){
							int userid = Integer.parseInt(mp.get(deviceId));
							if (userid > 0) {
								DeviceSDK.closeDevice(userid);
								DeviceSDK.openDevice(userid);
							}
						}
					}
					if (cameraHandleListener != null) {
						cameraHandleListener.changeList();
					}
				}
			}
		}
	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void getSnapShot(long UserID, byte[] nType, int len) {
		// TODO Auto-generated method stub
		try {
			BitmapDrawable drawable = new BitmapDrawable(
					BitmapFactory.decodeByteArray(nType, 0, len));
			Message msg = handler.obtainMessage(1, 1, (int) UserID);
			msg.obj = drawable;
			handler.sendMessage(msg);
		} catch (Exception exception) {
		}
	}
	@Override
	public void getDevicePrams(long UserID, long nType, String param) {
		// TODO Auto-generated method stub
		try {
			for (int i = 0; i < list_userid.size(); i++) {
				int getid = Integer.parseInt(list_userid.get(i));
				if (UserID == getid) {
					if (nType == 10022) {
						if (param.endsWith("")) {
							List<GateWay> gateWayList = GateWayService.list;
							for (GateWay gateway : gateWayList) {
								if (gateway.getTypeId() == 200) {
									long userId = Integer
											.parseInt(GateWayService.mp
													.get(gateway.getGatewayID()));
									if (userId == UserID){
											//&& (gateway.getIsCurrent() > 0)) {
										if (param.startsWith("DST")) {
											return;
										}
										int nowSerial = Integer.valueOf(
												param.substring(2, 4), 16);
										if (serial[(int) UserID] != nowSerial) {
											serial[(int) UserID] = nowSerial;
											String cmd= param.substring(0,param.length() - 1);
											String result=	"{\"MasterID\":\""+gateway.getGatewayID()+"\",\"ToID\":\"000000\",\"MsgType\":\"Response\",\"MsgState\":1,\"Content\":\""+cmd+"\"}";
											ReceviceBroadcast(result);
											//TcpSender.dataProcess(cmd);

										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void receiveDeviceStatus(long userid, int status) {
		// TODO Auto-generated method stub
		if (list_userid != null && list_userid.size() > 0) {
			for (int i = 0; i < list_userid.size(); i++) {
				int getid = Integer.parseInt(list_userid.get(i));
				if (userid == getid) {
					if (list == null || i >= list.size()) {
						break;
					}
					GateWay cb = list.get(i);
					if (cb.getTypeId() == 200) {
						Message msg = handler.obtainMessage(0, status, (int) userid);
						handler.sendMessage(msg);
					}
					break;
				}
			}
		}

//		Message msg = handler.obtainMessage(0, status, (int) userid);
//		handler.sendMessage(msg);

	}

	@Override
	public void receiveGateWayStatus(int type,long userid, int status) {
		// TODO Auto-generated method stub
		if(type==2) {
			if (list_userid != null && list_userid.size() > 0) {
				for (int i = 0; i < list_userid.size(); i++) {
					int getid = Integer.parseInt(list_userid.get(i));
					if (userid == getid) {
						if (list == null || i >= list.size()) {
							break;
						}
						GateWay cb = list.get(i);
						if (cb.getTypeId() == 3) {
							Message msg = handler.obtainMessage(0, status, (int) userid);
							handler.sendMessage(msg);
						}
						break;
					}
				}
			}
		}else if(type==1){
			Message msg = handler.obtainMessage(0, status, (int) userid);
			handler.sendMessage(msg);
		}
	}

	public IWidgetDAO Sqlite() {
		return MyApplication.getInstance().getWidgetDataBase();
	}

	public static List<GateWay> getList() {
		List<GateWay> gateWayList = new ArrayList<GateWay>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getTypeId() <3
					&& !list.get(i).getGatewayID()
					.equals(AiuiMainActivity.deviceID)) {
				gateWayList.add(list.get(i));
			}
		}
		return gateWayList;
	}

//Bula
public static void setDevicePramsListener() {
	BridgeService.setDevicePramsListener(instance);
}

	@Override
	public void onGetParamsEvent(long camHandler, long paramKey, String params) {

	}

	@Override
	public void onSetParamsEvent(long camHandler, long paramKey, int nResult) {

	}

	@Override
	public void onCameraStatusChangeEvent(long camHandler, int status) {
		Log.e("BCamera", "StatusChangeEvent1:"+camHandler+ "_________" + status + Contants.getOnlineStatusString(status, MyApplication.getInstance()));

		BCamera camera = cameraManager.getCamera(camHandler);
		if(camera!=null) {
			if (mp.containsKey(camera.getCameraBean().getDevID())) {
				int Id = Integer.parseInt(mp.get(camera.getCameraBean().getDevID()));
				Log.e("BCamera",  "StatusChangeEvent2:"+camHandler + "___" + Id + "_________" + status + Contants.getOnlineStatusString(status, MyApplication.getInstance()));
				Message msg = handler.obtainMessage(0, status, Id);
				handler.sendMessage(msg);
			}
		}
	}

	@Override
	public void onCameraSnapShotEvent(long camHandler, byte[] imgBuf, int len) {
		BCamera camera = cameraManager.getCamera(camHandler);
		if(camera!=null) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(imgBuf, 0, len);

			BitmapDrawable drawable = new BitmapDrawable(
					BitmapFactory.decodeByteArray(imgBuf, 0, len));
			if (mp.containsKey(camera.getCameraBean().getDevID())) {
				int Id = Integer.parseInt(mp.get(camera.getCameraBean().getDevID()));
				Message msg = handler.obtainMessage(1, 1, Id);
				msg.obj = drawable;
				handler.sendMessage(msg);
				// msg = handler.obtainMessage(0, camera.getStatus(), Id);
				//	handler.sendMessage(msg);
			}
		}
	}

	@Override
	public void onSerchEvent(CameraBean cameraBean) {
		Log.e("hsl",cameraBean.toString());
	}

	@Override
	public void onCameraAddEvent(BCamera camera) {

		Log.e("hsl",camera.toString());
	}

	@Override
	public void onAudioDataEvent(long camHandler, byte[] pcm, int size) {

	}

	@Override
	public void onAlarmEvent(long camHandler, int nType) {
		Log.e("BCamera",  "onAlarmEvent"+camHandler + "___" + nType + "_________" );
		BCamera camera = cameraManager.getCamera(camHandler);
		if(camera!=null) {
			if (CameraPlayActivity.start == 0) {
				Intent intent = new Intent(MyApplication.getInstance(), CameraPlayActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("camID", camera.getCameraBean().getDevID());
				intent.putExtra("type", nType);
				MyApplication.getInstance().startActivity(intent);
			}
		}
	}

	@Override
	public void onRecordFileList(long camHandler, int filecount, String fileName, String strDate, int size) {

	}

	@Override
	public void onRecordPlayPos(long camHandler, int pos) {

	}
	public void ReceviceBroadcast(String result) {
		Log.e("cmdCode",result);
		Intent intent = new Intent("com.zunder.smart.receiver");
		Bundle bundle = new Bundle();
		bundle.putString("str", result);
		intent.putExtras(bundle);
		sendBroadcast(intent);
	}

	public List<GateWay> getCameraList(){
        List<GateWay> result=new ArrayList <GateWay>();
	    for (int i=0;i<list.size();i++){
            GateWay gateWay=list.get(i);
	        if(gateWay.getTypeId()==200){
	            result.add(gateWay);
            }
        }
	    return result;
    }
    public List<GateWay> getGatewayList(){
        List<GateWay> result=new ArrayList <GateWay>();
        for (int i=0;i<list.size();i++){
            GateWay gateWay=list.get(i);
            if(gateWay.getTypeId()<=3){
                result.add(gateWay);
            }
        }
        return result;
    }
}
