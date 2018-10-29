package com.zunder.smart.broadcast;

import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.model.GateWay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

public class WifiReceiver extends BroadcastReceiver {
	static boolean isConect = true;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
		}
		else if (intent.getAction().equals(
				ConnectivityManager.CONNECTIVITY_ACTION)) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo activeNetworkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
				WifiManager wifiManager = (WifiManager) context
						.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				if (isConect) {
					isConect = false;
					List<GateWay> list = GateWayFactory.getInstance().getAll();
					for (int i = 0; i < list.size(); i++) {
						GateWay gateWay = list.get(i);
						if (gateWay.getTypeId() <3) {
//							if(wifiInfo.getSSID().startsWith("RAK")){
//								MainActivity.getInstance().closeClient();
//								MainActivity.getInstance().initRakSocket();
//							}else{
//								MainActivity.getInstance().closeRakClient();
//								MainActivity.getInstance().initSocket();
//							}
							break;
						}
					}
				}
			}
		} else if (intent.getAction().equals(
				WifiManager.WIFI_STATE_CHANGED_ACTION)) {// wifi打开与否
			int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
					WifiManager.WIFI_STATE_DISABLED);

			if (wifistate == WifiManager.WIFI_STATE_DISABLED) {

			} else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
				isConect = true;

			}
		}
	}
}