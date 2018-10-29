package com.zunder.smart.webservice.forward;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class VoiceServiceUtils {

	static String endPoint = Constants.HTTPS + "Service/VoiceService.asmx/";

	public static String createVoice(String voiceName, String voiceAnswer,
			String voiceAction, String userName) throws Exception {
		String methodName = "createVoice";
		HashMap data=new HashMap();
		data.put("voiceName", voiceName);
		data.put("voiceAnswer", voiceAnswer);
		data.put("voiceAction", voiceAction);
		data.put("userName", userName);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
	public static String updateVoice(int id,String voiceName, String voiceAnswer,
									 String voiceAction, String userName) throws Exception {
		String methodName = "updateVoice";
		HashMap data=new HashMap();
		data.put("id", id);
		data.put("voiceName", voiceName);
		data.put("voiceAnswer", voiceAnswer);
		data.put("voiceAction", voiceAction);
		data.put("userName", userName);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
	public static String getVoice(String userName, int PageNum, int PageCount)
			throws Exception {
		String methodName = "getVoice";
		HashMap data=new HashMap();
		data.put("userName", userName);
		data.put("PageNum", PageNum);
		data.put("PageCount", PageCount);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String detVoice(int id) throws Exception {
		String methodName = "detVoice";
		HashMap data=new HashMap();
 		data.put("id", id);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
}
