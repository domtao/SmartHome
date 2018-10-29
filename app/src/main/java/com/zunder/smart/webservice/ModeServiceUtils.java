package com.zunder.smart.webservice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class ModeServiceUtils {

	static String endPoint = Constants.HTTPS + "Service/ZunModeService.asmx/";


	public static String getModes(String ProjectKey) throws Exception {
		String methodName = "getModes";
		HashMap data=new HashMap();
		data.put("ProjectKey", ProjectKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String insertMode(int Id, String ModeName, String ModeImage, int ModeCode, String ModeType, int ModeLoop, int Seqencing, String StartTime, String EndTime, String ModeNickName,int IsShow, String ProjectKey)
	 throws Exception {
		String methodName = "insertMode";
		HashMap data=new HashMap();
		data.put("Id", Id);
		data.put("ModeName", ModeName);
		data.put("ModeImage", ModeImage);
		data.put("ModeCode", ModeCode);
		data.put("ModeType", ModeType);
		data.put("ModeLoop", ModeLoop);
		data.put("Seqencing", Seqencing);
		data.put("StartTime", StartTime);
		data.put("EndTime", EndTime);
		data.put("ModeNickName", ModeNickName);
		data.put("IsShow",IsShow);
		data.put("ProjectKey", ProjectKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String deleteMode(int Id, String ProjectKey) throws Exception {
		String methodName = "deleteMode";
		HashMap data=new HashMap();
		data.put("Id", Id);
		data.put("ProjectKey", ProjectKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
}
