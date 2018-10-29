package com.zunder.smart.dao.impl.factory;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.model.DeviceType;
import com.zunder.smart.MyApplication;

public class DeviceTypeFactory {

	private volatile static DeviceTypeFactory install;
	public static DeviceTypeFactory getInstance() {
		if (null == install) {
			install = new DeviceTypeFactory();
		}
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getDeviceTypes();
		}
		return install;
	}
	private static  List<DeviceType> list = new ArrayList<DeviceType>();

	public  List<DeviceType> getAll() {
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getDeviceTypes();
		}
		return list;
	}

	public  String getByName(int id) {

		String bitmap = null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getDeviceTypes();
		}

		for (DeviceType deviceType : list) {
			if (deviceType.getId() == id) {
				bitmap = deviceType.getDeviceTypeName();
				break;
			}
		}
		return bitmap;
	}
	public  DeviceType getByName(String deviceName) {

		DeviceType deviceType = null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getDeviceTypes();
		}

		for (DeviceType _deviceType : list) {
			if (_deviceType.getDeviceTypeName().equals(deviceName)) {
				deviceType=_deviceType;
				break;
			}
		}
		return deviceType;
	}
	
	public  String getDeviceTypeName(int deviceTypeKey) {

		String bitmap = null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getDeviceTypes();
		}

		for (DeviceType deviceType : list) {
			if (deviceType.getDeviceTypeKey() == deviceTypeKey) {
				bitmap = deviceType.getDeviceTypeName();
				break;
			}
		}
		return bitmap;
	}

	public  String getBitmapByID(int id) {

		String bitmap = null;
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getDeviceTypes();
		}

		for (DeviceType deviceType : list) {
			if (deviceType.getId() == id) {
				bitmap = deviceType.getDeviceTypeImage();
				break;
			}
		}
		return bitmap;
	}

	public  void clearList() {
		list = MyApplication.getInstance().getWidgetDataBase().getDeviceTypes();
	}

	public  List<DeviceType> getDeviceTypes(int isShow) {

		List<DeviceType> resultList = new ArrayList<DeviceType>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getDeviceTypes();
		}

		if (list.size() > 0) {
			for (DeviceType deviceType : list) {
				if (deviceType.getIsShow() == isShow) {
					resultList.add(deviceType);
				}
			}
		}
		return resultList;
	}

	public  List<String> getDeviceTypeNames(int isShow) {

		List<String> resultList = new ArrayList<String>();
		if (list.size() == 0) {
			list = MyApplication.getInstance().getWidgetDataBase()
					.getDeviceTypes();
		}

		if (list.size() > 0) {
			for (DeviceType deviceType : list) {
				if (deviceType.getIsShow() == isShow) {
					resultList.add(deviceType.getDeviceTypeName());
				}
			}
		}
		return resultList;
	}

	public  List<DeviceType> getRedFra() {
		List<DeviceType> resultList = new ArrayList<DeviceType>();
		DeviceType deviceType = new DeviceType();
		deviceType.setId(1);
		deviceType.setDeviceTypeName(MyApplication.getInstance().getString(R.string.rfx)); //local
		deviceType.setDeviceTypeImage("");
		deviceType.setSeqencing(1);
		deviceType.setDeviceTypeKey(1);
		deviceType.setDeviceTypeClick(1);
		deviceType.setIsShow(1);
		resultList.add(deviceType);
		/*
		DeviceType deviceType1 = new DeviceType();
		deviceType1.setId(2);
		deviceType1.setDeviceTypeName(MyApplication.getInstance().getString(R.string.service));
		deviceType1.setDeviceTypeImage(null);
		deviceType1.setSeqencing(1);
		deviceType1.setDeviceTypeKey(1);
		deviceType1.setDeviceType_Language(1);
		deviceType1.setDeviceTypeClick(1);
		deviceType1.setIsShow(1);
		resultList.add(deviceType1);
		*/
		DeviceType deviceType2 = new DeviceType();
		deviceType2.setId(2);
		deviceType2.setDeviceTypeName(MyApplication.getInstance().getString(R.string.serial));
		deviceType2.setDeviceTypeImage(null);
		deviceType2.setSeqencing(1);
		deviceType2.setDeviceTypeKey(1);
		deviceType2.setDeviceTypeClick(1);
		deviceType2.setIsShow(1);
		resultList.add(deviceType2);
		return resultList;
	}
}
