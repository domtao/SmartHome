package com.zunder.smart.socket.info;

import com.zunder.smart.MyApplication;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.tools.SystemInfo;
import com.zunder.smart.utils.Base64;
import com.zunder.smart.MyApplication;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.tools.SystemInfo;
import com.zunder.smart.utils.Base64;

import android.content.Context;
import android.util.Log;

public class ISocketCode {

	public static String setLogin(String masterName, String passWord,
			String masterOperator, String operatorPwd, int masterType) {
		LoginInfo info = new LoginInfo();
		info.setMasterID(getMasterID());
		info.setMasterName(masterName);
		info.setMasterWiFi(SystemInfo.getSSID(MyApplication.getInstance()));
		info.setToID("0000000000");
		info.setMsgType("Login");
		info.setContent("user Login");
		info.setPassWord(passWord);
		info.setMasterOperator(masterOperator);
		info.setOperatorPwd(operatorPwd);
		info.setMasterType(1);
		String json = JSONHelper.toJSON(info);
		String base64String = Base64.encode(json.getBytes());
		return base64String;
	}

	public static String setLine(String masterName, String passWord,
			String masterOperator, String operatorPwd, int masterType) {
		LoginInfo info = new LoginInfo();
		info.setMasterID(getMasterID());
		info.setMasterName(masterName);
		info.setMasterWiFi(SystemInfo.getSSID(MyApplication.getInstance()));
		info.setToID("0000000000");
		info.setMsgType("Line");
		info.setContent("user Login");
		info.setPassWord(passWord);
		info.setMasterOperator(masterOperator);
		info.setOperatorPwd(operatorPwd);
		info.setMasterType(masterType);
		String json = JSONHelper.toJSON(info);
		String base64String = Base64.encode(json.getBytes());
		return base64String;
	}

	public static String setConnect(String toID, String userName,
			String passWord, int masterType) {
		ConnectInfo info = new ConnectInfo();
		info.setMasterID(getMasterID());
		info.setToID(toID);
		info.setMsgType("Connect");
		info.setMasterType(masterType);
		info.setPassWord(passWord);
		info.setUserName(userName);
		info.setContent("content");
		info.setCreateTime(AppTools.getCurrentTime());
		String json = JSONHelper.toJSON(info);
		String base64String = Base64.encode(json.getBytes());
		return base64String;
	}
	public static String setForwardOut() {
		return getForWardString("ForwardOut", "user out", "000",1);
	}
	
	public static String setForward(String content, String toID) {
		return getForWardString("Forward", content, toID,1);
	}

	public static String setAddArce(String content, String toID) {
		return getForWardString("ForwardAddArce", content, toID,1);
	}
	public static String setAddDevice(String content, String toID) {
		return getForWardString("ForwardAddDevice", content, toID,1);
	}

	public static String setForwardVersion(String content, String toID) {
		return getForWardString("ForwardUpdateVersion", content, toID,1);
	}

	public static String setForwardMasterInfo(String content, String toID) {
		return getForWardString("ForwardMasterInfo", content, toID,1);
	}

	public static String setForwardWifiInfo(String content, String toID) {
		return getForWardString("ForwardWifiInfo", content, toID,1);
	}
	public static String setAddVoice(String content, String toID) {
		return getForWardString("ForwardAddVoice", content, toID,1);
	}
	public static String setUpdateDevice(String content, String toID) {
		return getForWardString("ForwardUpdateDevice", content, toID,1);
	}
	public static String setUpdateAnHong(String content, String toID) {
		return getForWardString("ForwardUpdateAnHong", content, toID,1);
	}
	public static String setUpdateSecurity(String content, String toID) {
		return getForWardString("ForwardUpdateSecurity", content, toID,1);
	}
	public static String setUpdateApk(String content, String toID) {
		return getForWardString("ForwardUpdateApk", content, toID,1);
	}
	public static String setVersionApk(String content, String toID) {
		return getForWardString("ForwardVersionApk", content, toID,1);
	}


	public static String setUpdateMode(String content, String toID) {
		return getForWardString("ForwardUpdateMode", content, toID,1);
	}

	public static String setDelDevice(String content, String toID) {
		return getForWardString("ForwardDelDevice", content, toID,1);
	}
	public static String setDelMode(String content, String toID) {
		return getForWardString("ForwardDelMode", content, toID,1);
	}
	public static String setDelModeList(String content, String toID) {
		return getForWardString("ForwardDelModeList", content, toID,1);
	}

	public static String setDelAllDevice(String content, String toID) {
		return getForWardString("ForwardDelAllDevice", content, toID,1);
	}

	public static String setDelAllVoice(String content, String toID) {
		return getForWardString("ForwardDelAllVoice", content, toID,1);
	}
	public static String setBlueTooth(String content, String toID) {
		return getForWardString("ForwardBlueTooth", content, toID,1);
	}
	public static String setGateWay(String content, String toID) {
		return getForWardString("ForwardGateWay", content, toID,1);
	}
	public static String setGetDevice(String content, String toID) {
		return getForWardString("ForwardGetDevice", content, toID,1);
	}
	public static String setGetMode(String content, String toID) {
		return getForWardString("ForwardGetMode", content, toID,1);
	}
	public static String setGetVoice(String content, String toID) {
		return getForWardString("ForwardVoiceInfo", content, toID,1);
	}


	public static String setDelVoice(String content, String toID) {
		return getForWardString("ForwardDelVoice", content, toID,1);
	}

	public static String setGetSubscribe(String content, String toID) {
		return getForWardString("ForwardSubscribeInfo", content, toID,1);
	}
	public static String setDelSubscribe(String content, String toID) {
		return getForWardString("ForwardDelSubscribe", content, toID,1);
	}

	public static String setAddSubscribe(String content, String toID) {
		return getForWardString("ForwardAddSubscribe", content, toID,1);
	}
	public static String setGetModeList(String content, String toID) {
		return getForWardString("ForwardGetModeList", content, toID,1);
	}

	public static String setGetAnHong(String content, String toID) {
		return getForWardString("ForwardGetAnHong", content, toID,1);
	}

	public static String setAddMode(String content, String toID) {
		return getForWardString("ForwardAddMode", content, toID,1);
	}

	public static String setAddModeList(String content, String toID) {
		return getForWardString("ForwardAddModeList", content, toID,1);
	}

	public static String setAddRedInFra(String content, String toID) {
		return getForWardString("ForwardAddRedInFra", content, toID,1);
	}

	public static String setForwardNameControl(String content, String toID) {
		return getForWardString("ForwardNameControl", content, toID,1);
	}

	public static String setUpdateMasterInfo(String content, String toID) {
		return getForWardString("ForwardUpMasterInfo", content, toID,1);
	}

	public static String setGreeting(String content, String toID) {
		return getForWardString("ForwardGretting", content, toID,1);

	}
	public static String setForwardSecurity(String content, String toID) {
		return getForWardString("ForwardSecurity", content, toID,1);

	}
	public static String setForwardSong(String content, String toID) {
		return getForWardString("ForwardSong", content, toID,1);

	}
	public static String setForwardRemoteControl
			(String content, String toID,int masterType) {
		return getForWardString("ForwardRemoteControl", content, toID,masterType);
	}

	public static String getForWardString(String msgType, String content,
			String toID,int masterType) {
		ForWardInfo info = new ForWardInfo();
		info.setMsgType(msgType);
		info.setMasterID(getMasterID());
		info.setToID(toID);
		info.setMasterType(masterType);
		info.setContent(content);
		info.setCreateTime(AppTools.getCurrentTime());
		String json = JSONHelper.toJSON(info);
		Log.e("json",json);
		String base64String = Base64.encode(json.getBytes());
		return base64String;
	}
	static String masterID = "";

	public static String getMasterID() {
		if (masterID.equals("")) {
			masterID = SystemInfo.getMasterID(MyApplication.getInstance());
		}
		return masterID;
	}
}
