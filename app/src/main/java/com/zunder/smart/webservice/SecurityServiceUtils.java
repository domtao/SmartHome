package com.zunder.smart.webservice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class SecurityServiceUtils {

	static String endPoint = Constants.HTTPS + "Service/SecurityService.asmx/";


	public static String getSecuritys(String MasterMac) throws Exception {
		String methodName = "getSecuritys";
		HashMap data=new HashMap();
		data.put("MasterMac", MasterMac);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String updateSecurity(String MsgName,String MsgInfo, int Id,String MasterMac)throws Exception {
		String methodName = "updateSecurity";
		HashMap data=new HashMap();
		data.put("MsgName", MsgName);
		data.put("MsgInfo", MsgInfo);
		data.put("Id", Id);
		data.put("MasterMac", MasterMac);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String deleteArce(int id, String projectKey) throws Exception {
		String methodName = "deleteArce";
		HashMap data=new HashMap();
		data.put("id", id);
		data.put("projectKey", projectKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
}
