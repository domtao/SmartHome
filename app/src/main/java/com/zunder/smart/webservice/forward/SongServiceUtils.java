package com.zunder.smart.webservice.forward;


import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class SongServiceUtils {

	static String endPoint = Constants.HTTPS + "Service/SongService.asmx/";

	public static String getSongs(String songType, int PageNum, int PageCount)
			throws Exception {
		String methodName = "getSongs";
		HashMap data=new HashMap();
		data.put("songType", songType);
		data.put("PageNum", PageNum);
		data.put("PageCount", PageCount);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

}
