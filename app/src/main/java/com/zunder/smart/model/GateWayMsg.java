package com.zunder.smart.model;

public class GateWayMsg {
	private String DeviceID;// " : "HSL-231603-TZTSJ",
	private String DeviceName;// " : "WIFICAM",
	private int DeviceType;// : 3,
	private String IP; // : "192.168.0.100",
	private String Mac;// : "00-02-B2-10-12-36",
	private int Port;// 81,
	private int SmartConnect;// : 0

	public String getDeviceID() {
		return DeviceID;
	}

	public void setDeviceID(String deviceID) {
		DeviceID = deviceID;
	}

	public String getDeviceName() {
		return DeviceName;
	}

	public void setDeviceName(String deviceName) {
		DeviceName = deviceName;
	}

	public int getProductsCode() {
		return DeviceType;
	}

	public void setDeviceType(int deviceType) {
		DeviceType = deviceType;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getMac() {
		return Mac;
	}

	public void setMac(String mac) {
		Mac = mac;
	}

	public int getPort() {
		return Port;
	}

	public void setPort(int port) {
		Port = port;
	}

	public int getSmartConnect() {
		return SmartConnect;
	}

	public void setSmartConnect(int smartConnect) {
		SmartConnect = smartConnect;
	}

}
