package com.zunder.smart.webservice.forward;


import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class MasterCodeServiceUtils {
	static String endPoint = Constants.HTTPS
			+ "Service/MasterCodeService.asmx/";

	public static String createMasterCode(String masterMac, String masterCode)
			throws Exception {
		String methodName = "createMasterCode";
		HashMap data=new HashMap();
		data.put("masterCode", masterCode);
		data.put("masterMac", masterMac);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String getMasterCode(String masterMac) throws Exception {
		String methodName = "getMasterCode";
		HashMap data=new HashMap();
		data.put("masterMac", masterMac);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

}
