package com.zunder.smart.webservice.forward;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;


public class SubscribeServiceUtils {

	static String endPoint = Constants.HTTPS + "Service/SubscribeService.asmx/";

	public static String createSubscribe(String SubscribeName, String SubscribeDate, String SubscribeTime, String SubscribeEvent, String SubscribeAction, String userName)
	throws Exception {
		String methodName = "createSubscribe";
		HashMap data=new HashMap();
		data.put("SubscribeName", SubscribeName);
		data.put("SubscribeDate", SubscribeDate);
		data.put("SubscribeTime", SubscribeTime);
		data.put("SubscribeEvent", SubscribeEvent);
		data.put("SubscribeAction", SubscribeAction);
		data.put("userName", userName);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
	public static String updateSubscribe(int id, String SubscribeName, String SubscribeDate, String SubscribeTime, String SubscribeEvent, String SubscribeAction, String userName)
	throws Exception {
		String methodName = "updateSubscribe";
		HashMap data=new HashMap();
		data.put("id", id);
		data.put("SubscribeName", SubscribeName);
		data.put("SubscribeDate", SubscribeDate);
		data.put("SubscribeTime", SubscribeTime);
		data.put("SubscribeEvent", SubscribeEvent);
		data.put("SubscribeAction", SubscribeAction);
		data.put("userName", userName);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
	public static String getSubscribe(String userName, int PageNum, int PageCount)
	throws Exception {
		String methodName = "getSubscribe";
		HashMap data=new HashMap();
		data.put("userName", userName);
		data.put("PageNum", PageNum);
		data.put("PageCount", PageCount);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String detSubscribe(int id) throws Exception {
		String methodName = "detSubscribe";
		HashMap data=new HashMap();
		data.put("id", id);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
}
