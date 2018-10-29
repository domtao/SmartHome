package com.zunder.smart.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zunder.smart.onekey.SettingWifiActivity;
import com.zunder.smart.onekey.SettingWifiActivity;

public class UDPServer implements Runnable {

	private static final String TAG = "UDPServer";

	private byte[] mMsg = new byte[100];
	private boolean mRunFlag = true;

	// 等待客户端发送消息，默认60秒超
	private int mRecvTimeout = 60000;

	private Handler mHandler;

	private DatagramSocket mSocket = null;

	public UDPServer(Handler handler, DatagramSocket dSocket) {
		this.mHandler = handler;
		this.mSocket = dSocket;
	}

	public boolean isFlag() {
		return mRunFlag;
	}

	public void setFlag(boolean flag) {
		this.mRunFlag = flag;
	}

	@SuppressLint("NewApi")
	@Override
	public void run() {

		long startRecvTime = System.currentTimeMillis();

		Log.d(TAG, "mRunFlag " + mRunFlag);

		while (mRunFlag) {

			DatagramPacket dPacket = new DatagramPacket(mMsg, mMsg.length);

			int timeout = (int) (mRecvTimeout - System.currentTimeMillis() + startRecvTime);
			if (timeout <= 0) {
				timeout = 10;
			}
			try {
				mSocket.setSoTimeout(timeout);
				mSocket.receive(dPacket);

				String result = new String(dPacket.getData());

				if (null != result && !result.isEmpty()) {
					Bundle bundle = new Bundle();
					bundle.putString("msg", result);

					Message msg = new Message();
					msg.setData(bundle);
					mHandler.sendMessage(msg);

					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			long curTime = System.currentTimeMillis();
			if (curTime - startRecvTime > mRecvTimeout) {
				mHandler.sendEmptyMessage(SettingWifiActivity.MESSAGE_TIMEOUT);
				break;
			}
		}
		mSocket.close();
	}
}
