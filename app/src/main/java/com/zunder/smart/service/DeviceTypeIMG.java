package com.zunder.smart.service;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.setting.ProjectUtils;

public class DeviceTypeIMG {
	static int pImage[] = { R.mipmap.mc_on,
			R.mipmap.mc_off, R.mipmap.key_on_1,
			R.mipmap.key_on_2, R.mipmap.key_on_3,
			R.mipmap.key_off_1, R.mipmap.key_off_2,
			R.mipmap.key_off_3, R.mipmap.k_1527,R.mipmap.key_sensor,R.mipmap.sl_img,R.mipmap.sl_img,R.mipmap.sl_img,R.mipmap.sl_img,R.mipmap.sl_img,R.mipmap.sl_img,R.mipmap.mc_16,R.mipmap.mc_17,R.mipmap.mc_18};


	public static int initBlueToothImg(int deviceType) {
		int imgID=R.mipmap.bluetooth_img;
		switch (deviceType){
			case 524:
				imgID=R.mipmap.phone_img;
				break;
			case 1028:
				imgID=R.mipmap.mic_img;
				break;
		}
		return imgID;
	}
}
