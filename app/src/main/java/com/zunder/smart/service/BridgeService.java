/**
 * 
 */
package com.zunder.smart.service;


import com.zunder.smart.listener.*;

import hsl.p2pipcam.nativecaller.DeviceSDK;
import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothClass.Device;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author wang.jingui
 */
public class BridgeService extends Service {
	private static DeviceStatusListener deviceStatusListener;
	private static PlayListener playListener;
	private static RecorderListener recorderListener;
	private static AlarmMessageListener alarmMessageListener;
	private static SearchDeviceListener deviceListener;
	private static DevicePramsListener devicePramsListener;
	private static SnapShotListener snapShotListener;
	@SuppressWarnings("unused")


	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		DeviceSDK.initialize("");
		DeviceSDK.SearchDeviceInit();
		DeviceSDK.setCallback(this);
		DeviceSDK.setSearchCallback(this);
		DeviceSDK.networkDetect();
	}

	@Override
	public void onDestroy() {
		DeviceSDK.unInitSearchDevice();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	public static void setDeviceStatusListener(
			DeviceStatusListener deviceStatusListener) {
		BridgeService.deviceStatusListener = deviceStatusListener;
	}

	public static void setPlayListener(PlayListener playListener) {
		BridgeService.playListener = playListener;
	}

	public void setRecorderListener(RecorderListener recorderListener) {
		BridgeService.recorderListener = recorderListener;
	}

	public static void setAlarmMessageListener(
			AlarmMessageListener alarmMessageListener) {
		BridgeService.alarmMessageListener = alarmMessageListener;
	}

	public static void setSearchDeviceListener(
			SearchDeviceListener deviceListener) {
		BridgeService.deviceListener = deviceListener;
	}

	public static void setDevicePramsListener(
			DevicePramsListener devicePramsListener) {
		BridgeService.devicePramsListener = devicePramsListener;
	}

	public static void setSnapShotListener(SnapShotListener snapShotListener) {
		BridgeService.snapShotListener = snapShotListener;
	}

	// -------------------------------------------------------------------------
	// ---------------------------以下是JNI层回调的接口-------------------------------
	// -------------------------------------------------------------------------

	public void CallBack_SnapShot(long UserID, byte[] buff, int len) {
		if (snapShotListener != null) {
			snapShotListener.getSnapShot(UserID, buff, len);
		}
	}

	public void CallBack_GetParam(long UserID, long nType, String param) {
		if (devicePramsListener != null) {
			devicePramsListener.getDevicePrams(UserID, nType, param);
		}
	}

	public void CallBack_SetParam(long UserID, long nType, int nResult) {

	}

	@SuppressLint("UseValueOf")
	public void CallBack_Event(long UserID, long nType) {
		int status = new Long(nType).intValue();
		if (deviceStatusListener != null)
			deviceStatusListener.receiveDeviceStatus(UserID, status);
		if (playListener != null)
			playListener.receiveNowDeviceStatus(UserID, status);
	}

	public void VideoData(long UserID, byte[] VideoBuf, int h264Data, int nLen,
			int Width, int Height, int time) {

	}

	public void callBackAudioData(long nUserID, byte[] pcm, int size) {
		if (playListener != null)
			playListener.callBackAudioData(nUserID, pcm, size);
		if (recorderListener != null)
			recorderListener.callBackAudioData(nUserID, pcm, size);
	}

	public void CallBack_RecordFileList(long UserID, int filecount,
			String fname, String strDate, int size) {
	}

	public void CallBack_P2PMode(long UserID, int nType) {
	}

	public void CallBack_RecordPlayPos(long userid, int pos) {
	}

	public void CallBack_VideoData(long nUserID, byte[] data, int type, int size) {
		if (playListener != null)
			playListener.callBackVideoData(nUserID, data, type, size);
	}

	@SuppressLint("UseValueOf")
	public void CallBack_AlarmMessage(long UserID, int nType) {
		int status = new Long(nType).intValue();
		if (alarmMessageListener != null)
			alarmMessageListener.receiveAlarmMessage(UserID, status);
	}

	public void CallBack_SearchDevice(String DeviceInfo) {
		if (deviceListener != null) {
			deviceListener.callBack_SearchDevice(DeviceInfo);
		}
	}

	public void showNotification(String message, Device device, int nType) {
	}

	public void CallBack_RecordPicture(long UserID, byte[] buff, int len) {
	}

	public void CallBack_RecordFileListV2(long UserID, String param) {

	}
}
