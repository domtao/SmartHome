package com.zunder.smart.webservice;


import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class ModeListServiceUtils {

	static String endPoint = Constants.HTTPS + "Service/ZunModeListService.asmx/";

	static String NAMESPACE = "http://tempuri.org/";

	public static String getModeLists(String ProjectKey) throws Exception {
		String methodName = "getModeLists";
		HashMap data=new HashMap();
		data.put("ProjectKey", ProjectKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String insertModeList(int Id, int DeviceId, int ModeId, String ModeAction, String ModeFunction, String ModeTime, String ModeDelayed, String ModePeriod, String BeginMonth, String EndMonth, int Seqencing, String ProjectKey)
	throws Exception {
		String methodName = "insertModeList";
		HashMap data=new HashMap();
		data.put("Id", Id);
		data.put("DeviceId", DeviceId);
		data.put("ModeId", ModeId);
		data.put("ModeAction", ModeAction);
		data.put("ModeFunction", ModeFunction);
		data.put("ModeTime", ModeTime);
		data.put("ModeDelayed", ModeDelayed);
		data.put("ModePeriod", ModePeriod);
		data.put("BeginMonth", BeginMonth);
		data.put("EndMonth", EndMonth);
		data.put("Seqencing", Seqencing);
		data.put("ProjectKey", ProjectKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String deleteModeList(int Id, String ProjectKey)
			throws Exception {
		String methodName = "deleteModeList";
		HashMap data=new HashMap();
		data.put("Id", Id);
		data.put("ProjectKey", ProjectKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

}
