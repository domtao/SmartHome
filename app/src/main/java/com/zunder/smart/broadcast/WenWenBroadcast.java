package com.zunder.smart.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.SendCMD;

public class WenWenBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String result = intent.getExtras().getString("str");
		if(!result.trim().equals("")){
			SendCMD sendCMD = new SendCMD().getInstance();
			sendCMD.sendCMD(0, result, null);
		}

	}
}
