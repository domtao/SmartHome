package com.zunder.smart.webservice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class ParamsServiceUtils {

	static String endPoint = Constants.HTTPS + "Service/ParamsService.asmx/";

	static String NAMESPACE = "http://tempuri.org/";

	public static String getActions() throws Exception {
		String methodName = "getActions";
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,null);
	}

	public static String getActionParams()
			throws Exception {
		String methodName = "getActionParams";
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,null);
	}

	public static String getFunctions()
			throws Exception {
		String methodName = "getFunctions";
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,null);

	}

	public static String getFunctionParams() throws Exception {
		String methodName = "createProject";
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,null);
	}
}
