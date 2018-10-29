package com.zunder.smart.model;

public class ServiceInfo {
	private String IP;
	private int Port;
	private String ServiceName;

	public String getServiceName() {
		return ServiceName;
	}

	public void setServiceName(String serviceName) {
		ServiceName = serviceName;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public int getPort() {
		return Port;
	}
	public void setPort(int port) {
		Port = port;
	}

}
