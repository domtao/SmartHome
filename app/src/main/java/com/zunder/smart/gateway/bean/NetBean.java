package com.zunder.smart.gateway.bean;

public class NetBean {

	private int netState;
	private int conectState;

	private String ip;
	private int port;
	private String outIp;
	private int outPort;
	private String machCode;

	public String getMachCode() {
		return machCode;
	}

	public void setMachCode(String machCode) {
		this.machCode = machCode;
	}

	public String getOutIp() {
		return outIp;
	}

	public void setOutIp(String outIp) {
		this.outIp = outIp;
	}

	public int getOutPort() {
		return outPort;
	}

	public void setOutPort(int outPort) {
		this.outPort = outPort;
	}

	public int getNetState() {
		return netState;
	}

	public void setNetState(int netState) {
		this.netState = netState;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getConectState() {
		return conectState;
	}

	public void setConectState(int conectState) {
		this.conectState = conectState;
	}

}
