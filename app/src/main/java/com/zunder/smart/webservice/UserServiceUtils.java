package com.zunder.smart.webservice;


import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class UserServiceUtils {

	static String endPoint = Constants.HTTPS + "Service/UserService.asmx/";

	public static String RegisterUser(String userName, String userPassWord,
			int grad_ID, int loginType, String userNick) throws Exception {
		String methodName = "RaegisterUser";
		HashMap data=new HashMap();
		data.put("userName", userName);
		data.put("userPassWord", userPassWord);
		data.put("grad_ID", grad_ID);
		data.put("loginType", loginType);
		data.put("userNick", userNick);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String isUserInfo(String userName) throws Exception {
		String methodName = "isUserInfo";
		HashMap data=new HashMap();
		data.put("userName", userName);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String updatePassword(String userName, String userPassWord)
			throws Exception {
		String methodName = "updatePassword";
		HashMap data=new HashMap();
		data.put("userName", userName);
		data.put("userPassWord", userPassWord);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String LoginUser(String userName, String userPassWord)
			throws Exception {
		String methodName = "LoginUser";
		HashMap data=new HashMap();
		data.put("userName", userName);
		data.put("userPassWord", userPassWord);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

}
