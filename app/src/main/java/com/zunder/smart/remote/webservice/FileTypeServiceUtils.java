package com.zunder.smart.remote.webservice;


import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class FileTypeServiceUtils {
//	http://112.74.64.82:99/Service/IFileInfoService.asmx
	static String endPoint = Constants.HTTPS+"Service/FileTypeService.asmx/";
	public static String getFileTypes(int PageNum,int PageCount) throws Exception {
		String methodName = "getFileTypes ";
		HashMap data=new HashMap();
		data.put("PageNum",PageNum);
		data.put("PageCount",PageCount);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
}
