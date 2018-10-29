package com.zunder.smart.net;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.zunder.smart.MyApplication;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.netty.MockLoginNettyClient;


public class WifiReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String wifiInfoName = "";
		if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
		} else if (intent.getAction().equals(
				ConnectivityManager.CONNECTIVITY_ACTION)) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager
					.getActiveNetworkInfo();
			if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
				WifiManager wifiManager = (WifiManager) context
						.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				wifiInfoName = wifiInfo.getSSID();
				if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
					if(!wifiInfoName.equals(MyApplication.getInstance().getWifiName())){
						MockLoginNettyClient.getInstans().isLinkFlag=5;
						MockLoginNettyClient.getInstans().plusNumber=0;
						MyApplication.getInstance().setWifiName(wifiInfoName);
					}
				} else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
					if(!"phone".equals(MyApplication.getInstance().getWifiName())){
						MockLoginNettyClient.getInstans().isLinkFlag=5;
						MockLoginNettyClient.getInstans().plusNumber=0;
						MyApplication.getInstance().setWifiName("phone");
					}
				}
					MainActivity.getInstance().setTip("正在连接网络");

			} else {
				MyApplication.getInstance().setWifiName("none");

					MainActivity.getInstance().setTip("当前无网络连接");

				MockLoginNettyClient.getInstans().isLinkFlag=5;
				MockLoginNettyClient.getInstans().plusNumber=0;
//				MainActivity.getInstance().freshFindDevice();

			}
		} else if (intent.getAction().equals(
				WifiManager.WIFI_STATE_CHANGED_ACTION)) {// wifi打开与否
			int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
					WifiManager.WIFI_STATE_DISABLED);

			if (wifistate == WifiManager.WIFI_STATE_DISABLED) {

			} else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
			}
		}
	}
}