package com.zunder.smart.webservice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class ProjectorServiceUtils {

	static String endPoint = Constants.HTTPS + "Service/ProjectorService.asmx/";
	public static String getProjectors(int proType) throws Exception {
		String methodName = "getProjectors";
		HashMap data=new HashMap();
		data.put("proType", proType);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
	public static String createProjectors(String proName, String versionName, String baudRate, String checkBit, String hexCode, String sendCode,int  proType) throws Exception {
		String methodName = "createProjectors";
		HashMap data=new HashMap();
		data.put("proName", proName);
		data.put("versionName", versionName);
		data.put("baudRate", baudRate);
		data.put("checkBit", checkBit);
		data.put("hexCode", hexCode);
		data.put("sendCode", sendCode);
		data.put("proType", proType);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String getProjectorVersions(int versionId) throws Exception {
		String methodName = "getProjectorVersions";
		HashMap data=new HashMap();
		data.put("versionId", versionId);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String getProjectorCode(int versionId) throws Exception {
		String methodName = "getProjectorCode";
		HashMap data=new HashMap();
		data.put("versionId", versionId);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String  deleteProjector(int Id) throws Exception {
		String methodName = "deleteProjector";
		HashMap data=new HashMap();
		data.put("Id", Id);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String deleteProjectorVersion(int VersionId)throws Exception {
		String methodName = "deleteProjectorVersion";
		HashMap data=new HashMap();
		data.put("VersionId", VersionId);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
}
