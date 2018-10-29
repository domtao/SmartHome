package com.zunder.smart.setting;

import java.io.File;
import org.json.JSONObject;

import com.zunder.smart.MyApplication;
import com.zunder.smart.gateway.bean.RootPath;
import com.zunder.smart.json.Constants;
import com.zunder.smart.json.JSONFileUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.gateway.bean.RootPath;
import com.zunder.smart.json.JSONFileUtils;

import a.a.a.a.b;
import android.text.TextUtils;

public class ProjectUtils {

	public static RootPath rootPath = null;

	private static File jsonFile = new File(MyApplication.getInstance()
			.getRootPath() + File.separator + "root_json");

	public static RootPath getRootPath() {

		if (rootPath == null) {
			rootPath = new RootPath();
			String json = JSONFileUtils.getJsonStringFromFile(jsonFile)
					.replace("\r\n", "");
			if (TextUtils.isEmpty(json)) {
				rootPath.setRootPath(MyApplication.getInstance().getRootPath()
						+ File.separator + "Root/homedatabases.db");
				rootPath.setRootName("默认家庭");
				rootPath.setRootImage(Constants.PROCASEIMAGEPATH);
				rootPath.setRootWay(0);
				rootPath.setRootID("");
				rootPath.setRootVersion(0);
				rootPath.setConnectState(1);
				rootPath.setMessageBack(0);
				rootPath.setLanguage(0);
				rootPath.setPlaySource(0);
				rootPath.setControlState(0);
				rootPath.setAlarmId("");
				saveRootPath();
			} else {
				try {
					JSONObject object = new JSONObject(json);
					rootPath.setRootPath(object.getString("RootPath"));
					rootPath.setRootName(object.getString("RootName"));
					rootPath.setRootImage(object.getString("RootImage"));
					rootPath.setRootWay(object.getInt("rootWay"));
					rootPath.setRootVersion(object.getInt("rootVersion"));
					rootPath.setRootID(object.getString("rootID"));
					rootPath.setConnectState(object.getInt("connectState"));
					rootPath.setMessageBack(object.getInt("messageBack"));
					rootPath.setLanguage(object.getInt("language"));
					rootPath.setPlaySource(object.getInt("playsource"));
					rootPath.setControlState(object.getInt("controlState"));
					rootPath.setAlarmId(object.getString("AlarmId"));
				} catch (Exception e) {
					// TODO: handle exception
					rootPath.setRootPath(MyApplication.getInstance().getRootPath()
							+ File.separator + "Root/homedatabases.db");
					rootPath.setRootName("默认家庭");
					rootPath.setRootImage(Constants.PROCASEIMAGEPATH);
					rootPath.setRootWay(0);
					rootPath.setRootID("");
					rootPath.setRootVersion(0);
					rootPath.setConnectState(1);
					rootPath.setMessageBack(0);
					rootPath.setLanguage(0);
					rootPath.setPlaySource(0);
					rootPath.setControlState(0);
					rootPath.setAlarmId("");
					saveRootPath();
					return null;
				}
			}
		}
		return rootPath;

	}

	public static void saveRootPath() {
		try {
			JSONObject bject = new JSONObject();
			bject.put("RootPath", rootPath.getRootPath());
			bject.put("RootName", rootPath.getRootName());
			bject.put("RootImage",rootPath.getRootImage());
			bject.put("rootWay", rootPath.getRootWay());
			bject.put("rootVersion", rootPath.getRootVersion());
			bject.put("rootID", rootPath.getRootID());
			bject.put("connectState", rootPath.getConnectState());
			bject.put("messageBack", rootPath.getMessageBack());
			bject.put("language", rootPath.getLanguage());
			bject.put("playsource", rootPath.getPlaySource());
			bject.put("controlState", rootPath.getControlState());
			bject.put("AlarmId", rootPath.getAlarmId());
			JSONFileUtils.saveJSONToFile(jsonFile, bject.toString());
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}

}
