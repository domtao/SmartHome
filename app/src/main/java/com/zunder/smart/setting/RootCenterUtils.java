package com.zunder.smart.setting;

import android.text.TextUtils;

import com.zunder.smart.MyApplication;
import com.zunder.smart.gateway.bean.RootPath;
import com.zunder.smart.json.Constants;
import com.zunder.smart.json.JSONFileUtils;
import com.zunder.smart.model.RootCenter;

import org.json.JSONObject;

import java.io.File;

public class RootCenterUtils {

	public static RootCenter rootCenter = null;

	private static File jsonFile = new File(MyApplication.getInstance()
			.getRootPath() + File.separator + "root_center_json");

	public static RootCenter getRootCenter() {

		if (rootCenter == null) {
			rootCenter = new RootCenter();
			String json = JSONFileUtils.getJsonStringFromFile(jsonFile)
					.replace("\r\n", "");
			if (TextUtils.isEmpty(json)) {
				rootCenter.setPadding(0);
				rootCenter.setScreenOrientation(0);
				rootCenter.setTabPosition(0);
				rootCenter.setTabHeight(60);
				rootCenter.setMinTabCount(4);
				rootCenter.setColorString("#FFFFFF");
				saveRootPath();
			} else {
				try {
					JSONObject object = new JSONObject(json);
					rootCenter.setPadding(object.getInt("Padding"));
					rootCenter.setScreenOrientation(object.getInt("ScreenOrientation"));
					rootCenter.setTabPosition(object.getInt("TabPosition"));
					rootCenter.setTabHeight(object.getInt("TabHeight"));
					rootCenter.setMinTabCount(object.getInt("MinTabCount"));
					rootCenter.setColorString(object.getString("ColorString"));
				} catch (Exception e) {
					// TODO: handle exception
					rootCenter.setPadding(0);
					rootCenter.setScreenOrientation(0);
					rootCenter.setTabPosition(0);
					rootCenter.setTabHeight(60);
					rootCenter.setMinTabCount(4);
					rootCenter.setColorString("#FFFFFF");
					saveRootPath();
					return null;
				}
			}
		}
		return rootCenter;

	}

	public static void saveRootPath() {
		try {
			JSONObject bject = new JSONObject();
			bject.put("Padding", rootCenter.getPadding());
			bject.put("ScreenOrientation", rootCenter.getScreenOrientation());
			bject.put("TabPosition",rootCenter.getTabPosition());
			bject.put("TabHeight", rootCenter.getTabHeight());
			bject.put("MinTabCount", rootCenter.getMinTabCount());
			bject.put("ColorString", rootCenter.getColorString());
			JSONFileUtils.saveJSONToFile(jsonFile, bject.toString());
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}

}
