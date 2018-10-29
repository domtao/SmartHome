package com.zunder.smart.webservice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class RedFraServiceUtils {

	static String endPoint = Constants.HTTPS + "Service/ZunRedFraService.asmx/";
	public static String getRedFras(String ProjectKey) throws Exception {
		String methodName = "getRedFras";
		HashMap data=new HashMap();
		data.put("ProjectKey", ProjectKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String insertRedFra(int Id, String InfraredName, String InfraredImage, String DeviceId, int FatherId, int InfraredIndex, int InfraredBrandId, String InfraredBrandName, int InfraredVersionId, String InfraredVersionName,
									  int InfraredKey, String InfraredCode, int InfraredButtonId, int InfraredStudyType, int Seqencing, String ProjectKey) throws Exception {
		String methodName = "insertRedFra";
		HashMap data=new HashMap();
		data.put("Id", Id);
		data.put("InfraredName", InfraredName);
		data.put("InfraredImage", InfraredImage);
		data.put("DeviceId", DeviceId);
		data.put("FatherId", FatherId);
		data.put("InfraredIndex", InfraredIndex);
		data.put("InfraredBrandId", InfraredBrandId);
		data.put("InfraredBrandName", InfraredBrandName);
		data.put("InfraredVersionId", InfraredVersionId);
		data.put("InfraredVersionName", InfraredVersionName);
		data.put("InfraredKey", InfraredKey);
		data.put("InfraredCode", InfraredCode);
		data.put("InfraredButtonId", InfraredButtonId);
		data.put("InfraredStudyType", InfraredStudyType);
		data.put("Seqencing", Seqencing);
		data.put("ProjectKey", ProjectKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String deleteRedFra(int Id, String ProjectKey)
			throws Exception {
		String methodName = "deleteRedFra";
		HashMap data=new HashMap();
		data.put("Id", Id);
		data.put("ProjectKey", ProjectKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

}
