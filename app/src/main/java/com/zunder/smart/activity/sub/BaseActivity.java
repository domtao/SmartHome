package com.zunder.smart.activity.sub;


import android.app.Activity;

import com.zunder.smart.listener.DeviceStateListener;
import com.zunder.smart.listener.ElectricListeener;

public class BaseActivity extends Activity implements DeviceStateListener,
		ElectricListeener {

	public int infraID = 0;
	public int versionID = 0;;
	public String sendStr = "00000";

	public static String toHex1602(String string) {
		int a = Integer.parseInt(string);
		String ss = Integer.toHexString(a).toUpperCase();
		if (ss.length() < 2) {
			ss = "0" + ss;
		}
		return ss;
	}
	public static String toHex1604(String num) {
		String temp = "0000";

		String ss = Integer.toHexString(Integer.parseInt(num)).toUpperCase();
		ss = temp.substring(0, temp.length() - ss.length()) + ss;
		return ss;
	}

	@Override
	public void setElectric(String cmd) {
		// TODO Auto-generated method stub
	}

	@Override
	public void receiveDeviceStatus(String cmd) {
		// TODO Auto-generated method stub
		
	}
}
