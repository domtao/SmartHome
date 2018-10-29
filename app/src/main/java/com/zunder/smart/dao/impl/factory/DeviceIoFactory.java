package com.zunder.smart.dao.impl.factory;

import java.util.ArrayList;
import java.util.List;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.model.DeviceIo;
import com.zunder.smart.model.Mode;
import com.zunder.smart.MyApplication;

public class DeviceIoFactory {
	public static List<DeviceIo> list = new ArrayList<DeviceIo>();

	public static List<DeviceIo> getDeviceIos(int io) {
		list.clear();
		for (int i = 0; i < io; i++) {
			DeviceIo deviceIo = new DeviceIo();
			deviceIo.setId(i);
			deviceIo.setIoName(MyApplication.getInstance().getString(
					R.string.Io)
					+ (i + 1));
			list.add(deviceIo);
		}
		DeviceIo deviceIoAll = new DeviceIo();
		deviceIoAll.setId(io);
		deviceIoAll.setIoName(MyApplication.getInstance().getString(
				R.string.All));
		list.add(deviceIoAll);
		return list;
	}

	public static List<DeviceIo> getModeIo() {
		list.clear();
		for (int i = 0; i < 8; i++) {
			DeviceIo deviceIo = new DeviceIo();
			deviceIo.setId(i);
			deviceIo.setIoName(MyApplication.getInstance().getString(
					R.string.Io)
					+ (i + 1));
			list.add(deviceIo);
		}

		return list;
	}

	public static List<DeviceIo> getDeviceModeIo() {
		list.clear();
		List<Mode> listMode = ModeFactory.getInstance().getAll();
		for (int i = 0; i < listMode.size(); i++) {
			DeviceIo deviceIo = new DeviceIo();
			deviceIo.setId(listMode.get(i).getId());
			deviceIo.setIoName(listMode.get(i).getModeName());
			list.add(deviceIo);
		}

		return list;
	}

	public static List<String> getDeviceIoNames(int io) {
		List<String> result=new ArrayList<String>();

		for (int i = 0; i < io; i++) {
			result.add(MyApplication.getInstance().getString(
					R.string.Io)
					+ (i + 1));
		}
		result.add(MyApplication.getInstance().getString(
				R.string.All));
		return result;
	}
}
