package com.zunder.smart.socket.result;

import java.util.HashMap;

import org.json.JSONObject;

public abstract class SocketMainResult {

	public static enum ServiceType {
		LOGIN, DOWNINFO, APKINFO, GETANHONGINFO, CONNECT,GETSUBSCRIBEINFO, RESPONSE,
		GETVOICEINFO,FORWARD, LINE,GETMODEINFO,GETMODELIST, OTHER, ERROR, CLOUDRESPONSE,
		GETDEVICEINFO,GETWIFIINFO, GETMASTERINFO,SECURITYINFO,GETBLUETOOTHINFO,GETGATEWAYINFO,REMOTECONTROLINFO,FORWARDGREETING
	}

	static HashMap<String, ServiceType> serviceMap = new HashMap<String, ServiceType>();

	static {
		serviceMap.put("GetSubscribeInfo", ServiceType.GETSUBSCRIBEINFO);
		serviceMap.put("GetVoiceInfo", ServiceType.GETVOICEINFO);
		serviceMap.put("SecurityInfo", ServiceType.SECURITYINFO);
		serviceMap.put("GetWifiInfo", ServiceType.GETWIFIINFO);
		serviceMap.put("GetMasterInfo", ServiceType.GETMASTERINFO);
		serviceMap.put("GetAnHongInfo", ServiceType.GETANHONGINFO);
		serviceMap.put("ApkInfo", ServiceType.APKINFO);
		serviceMap.put("DownInfo", ServiceType.DOWNINFO);
		serviceMap.put("GetDeviceInfo", ServiceType.GETDEVICEINFO);

		serviceMap.put("GetModeInfo", ServiceType.GETMODEINFO);
		serviceMap.put("GetModeList", ServiceType.GETMODELIST);
		serviceMap.put("Login", ServiceType.LOGIN);
		serviceMap.put("Connect", ServiceType.CONNECT);
		serviceMap.put("Response", ServiceType.RESPONSE);
		serviceMap.put("Forward", ServiceType.FORWARD);
		serviceMap.put("Line", ServiceType.LINE);
		serviceMap.put("Error", ServiceType.ERROR);
		serviceMap.put("Other", ServiceType.OTHER);
		serviceMap.put("CloudResponse", ServiceType.CLOUDRESPONSE);

		serviceMap.put("BlueToothInfo", ServiceType.GETBLUETOOTHINFO);
		serviceMap.put("GateWayInfo", ServiceType.GETGATEWAYINFO);
		serviceMap.put("RemoteControlInfo", ServiceType.REMOTECONTROLINFO);



	}

	public final static String KEY_TEXT = "text";
	public final static String KEY_ANSWER = "answer";
	public final static String KEY_RDN = "rdn";
	public final static int RDN_VALUE = 1;
	public final static int RDN_STRING = 2;
	protected String service;
	protected String answerText = "";
	protected JSONObject json = null;

	public static ServiceType getServiceType(String service) {
		ServiceType type = serviceMap.get(service);
		if (null == type) {
			type = ServiceType.OTHER;
		}

		return type;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getAnswerText() {
		return answerText;
	}

	public ServiceType getServiceType() {
		return getServiceType(service);
	}

	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}

}
