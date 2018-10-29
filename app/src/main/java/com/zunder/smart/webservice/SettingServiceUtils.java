package com.zunder.smart.webservice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class SettingServiceUtils {

	static String endPoint = Constants.HTTPS + "Service/SettingService.asmx/";

	public static String getPassword(String password) throws Exception {
		String methodName = "getPassword";
		HashMap data=new HashMap();
		data.put("password", password);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
	public static String getVersion(String SetVersion)
			throws Exception {
		String methodName = "getVersion";
		HashMap data=new HashMap();
		data.put("SetVersion", SetVersion);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
	public static String setPushMasterState(String masterMac)
			throws Exception {
		String methodName = "setPushMasterState";
		HashMap data=new HashMap();
		data.put("masterMac", masterMac);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
}
