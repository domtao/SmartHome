package com.zunder.smart.webservice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class DeviceTypeServiceUtils {

	static String endPoint = Constants.HTTPS + "Service/DeviceTypeService.asmx/";

	public static String getProductsCode() throws Exception {
		String methodName = "getProductsCode";
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,null);
	}

	public static String insertDeviceType(int id, String typeName, int typeKey,
			int language, int click, int seqencing, int isShow)
			throws Exception {
		String methodName = "insertDeviceType";
		HashMap data=new HashMap();
		data.put("id", id);
		data.put("typeName", typeName);
		data.put("typeKey", typeKey);
		data.put("language", language);
		data.put("click", click);
		data.put("seqencing", seqencing);
		data.put("isShow", isShow);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

}
