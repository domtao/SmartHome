package com.zunder.smart.webservice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class ImageServiceUtils {

	static String endPoint = Constants.HTTPS + "Service/ImageService.asmx/";

	static String NAMESPACE = "http://tempuri.org/";
	public static String getImages(int imageIndex) throws Exception {
		String methodName = "getImages";
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,null);
	}
	public static String getImagesByType(int TypeId) throws Exception {
		//模式23
		String methodName = "getImagesByType";
		String url=endPoint+methodName;
		HashMap data=new HashMap();
		data.put("TypeId", TypeId);
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

}
