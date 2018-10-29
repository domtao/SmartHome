package com.zunder.smart.listener;

import com.zunder.smart.aiui.info.SubscribeInfo;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.VoiceInfo;
import com.zunder.smart.aiui.info.SubscribeInfo;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.VoiceInfo;

import android.os.Message;

public interface GetDeviceListener {
	public void getDevice(Device device);
	public void getMode(Mode mode);
	public void getMessage(String iinfo);
	public void getVoice(VoiceInfo voiceInfo);
	public void getModeList(String  param);
	public void getSubscribe(SubscribeInfo subscribeInfo);

}
