package com.zunder.smart.model;

import android.graphics.Bitmap;

public class DeviceType {
	private int Id;
	private String DeviceTypeName;
	private String DeviceTypeImage;
	private int DeviceTypeKey;
	private int DeviceTypeClick;
	private String CreationTime;
	private int IsShow;
	private int Seqencing;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getDeviceTypeName() {
		return DeviceTypeName;
	}

	public void setDeviceTypeName(String deviceTypeName) {
		DeviceTypeName = deviceTypeName;
	}

	public String getDeviceTypeImage() {
		return DeviceTypeImage;
	}

	public void setDeviceTypeImage(String deviceTypeImage) {
		DeviceTypeImage = deviceTypeImage;
	}


	public int getDeviceTypeClick() {
		return DeviceTypeClick;
	}

	public void setDeviceTypeClick(int deviceTypeClick) {
		DeviceTypeClick = deviceTypeClick;
	}

	public String getCreationTime() {
		return CreationTime;
	}

	public void setCreationTime(String creationTime) {
		CreationTime = creationTime;
	}

	public int getIsShow() {
		return IsShow;
	}

	public void setIsShow(int isShow) {
		IsShow = isShow;
	}

	public int getSeqencing() {
		return Seqencing;
	}

	public void setSeqencing(int seqencing) {
		Seqencing = seqencing;
	}




	public int getDeviceTypeKey() {
		return DeviceTypeKey;
	}

	public void setDeviceTypeKey(int deviceTypeKey) {
		DeviceTypeKey = deviceTypeKey;
	}
}
