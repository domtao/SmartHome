package com.zunder.smart.remote.webservice;


import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class IFileInfoServiceUtils {
	static String endPoint = Constants.HTTPS+"Service/IFileInfoService.asmx/";
	public static String createIFileInfo(String FileName,String FileUrl,int TypeId,String Extension,String MasterID) throws Exception {
		String methodName = "getFileTypes ";
		HashMap data=new HashMap();
		data.put("FileName",FileName);
		data.put("FileUrl",FileUrl);
		data.put("TypeId",TypeId);
		data.put("Extension",Extension);
		data.put("MasterID",MasterID);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
	public static String getIFileInfos(String MasterID,int TypeId,int PageNum, int PageCount){
		String methodName = "getIFileInfos ";
		HashMap data=new HashMap();
		data.put("MasterID",MasterID);
		data.put("TypeId",TypeId);
		data.put("PageNum",PageNum);
		data.put("PageCount",PageCount);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}


	public static String deleteIFileInfo(int Id,String MasterID){
		String methodName = "deleteIFileInfo";
		HashMap data=new HashMap();
		data.put("Id",Id);
		data.put("MasterID",MasterID);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
}
