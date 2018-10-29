package com.zunder.smart.listener;

import com.zunder.smart.aiui.info.BlueDevice;
import com.zunder.smart.aiui.info.BlueDevice;

public interface BlueToothListener {
	public void SearchDevice(BlueDevice blueDevice);
	public void OnBondState(String address,int state);
	public void OnConnectState(String address,int state);
}
