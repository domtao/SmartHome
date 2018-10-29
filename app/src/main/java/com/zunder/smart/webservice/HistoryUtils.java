package com.zunder.smart.webservice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class HistoryUtils {

	static String endPoint = Constants.HTTPS + "Service/HistoryService.asmx/";

	public static String getHistorys(int PageNum, int PageCount, String PrimaryKey) throws Exception {
		String methodName = "getHistorys";
		HashMap data=new HashMap();
		data.put("PageNum", PageNum);
		data.put("PageCount", PageCount);
		data.put("MasterMac", PrimaryKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
	public static String insertHistorys(String HistoryName,String HistoryCode,String MasterMac,String PrimaryKey) throws Exception {
		String methodName = "insertHistorys";
		HashMap data=new HashMap();
		data.put("HistoryName", HistoryName);
		data.put("HistoryCode", HistoryCode);
		data.put("MasterMac", MasterMac);
		data.put("PrimaryKey", PrimaryKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
	public static String deleteHistorys(String PrimaryKey) throws Exception {
		String methodName = "deleteHistorys";
		HashMap data=new HashMap();
		data.put("PrimaryKey", PrimaryKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
	public static String deleteHistorysByMasterMac(String MasterMac,String PrimaryKey) throws Exception {
		String methodName = "deleteHistorysByMasterMac";
		HashMap data=new HashMap();
		data.put("MasterMac", MasterMac);
		data.put("PrimaryKey", PrimaryKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
}
