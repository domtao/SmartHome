package com.zunder.smart.webservice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class InfraServiceUtils {

	static String endPoint = Constants.HTTPS + "Service/InfraWebService.asmx/";

	public static String GetInfraNames(int PageNum, int PageCount)
			throws Exception {
		String methodName = "GetInfraNames";
		HashMap data=new HashMap();
		data.put("PageNum", PageNum);
		data.put("PageCount", PageCount);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String GetInfraVersions(int PageNum, int PageCount,
			int InfraID) throws Exception {
		String methodName = "GetInfraVersions";
		HashMap data=new HashMap();
		data.put("InfraID", InfraID);
		data.put("PageNum", PageNum);
		data.put("PageCount", PageCount);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String GetInfraCodes(int PageNum, int PageCount, int VersionID)
			throws Exception {
		String methodName = "GetInfraCodes";
		HashMap data=new HashMap();
		data.put("VersionID", VersionID);
		data.put("PageNum", PageNum);
		data.put("PageCount", PageCount);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

}
