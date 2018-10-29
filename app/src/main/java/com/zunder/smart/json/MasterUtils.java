package com.zunder.smart.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.model.Master;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.tools.SystemInfo;
import com.zunder.smart.MyApplication;

import android.content.Context;
import android.text.TextUtils;

public class MasterUtils {

	public static Master masterInfo = null;
	private static File jsonFile = new File(Constants.REMOTE_PATH
			+ File.separator + "master_json");

	public static Master getMaster(Context context) {
		if (masterInfo == null) {
			String json = JSONFileUtils.getJsonStringFromFile(jsonFile)
					.replace("\r\n", "");
			if (TextUtils.isEmpty(json)) {
				masterInfo = new Master();
				masterInfo.setMc(SystemInfo.getMasterID(context));
				masterInfo.setMn(MyApplication.getInstance().getString(
						R.string.app_name));
				masterInfo.setWf(SystemInfo.getSSID(context));
				masterInfo.setSt(1);
				masterInfo.setUn("admin");
				masterInfo.setTy(1);
				masterInfo.setPw("");
				saveMasterInfo();
			} else {
				try {
					JSONObject object = new JSONObject(json);
					masterInfo = new Master();

					masterInfo.setMc(SystemInfo.getMasterID(context));
					masterInfo.setMn(object.getString("MasterName"));
					masterInfo.setWf(SystemInfo.getSSID(context));
					masterInfo.setSt(1);
					masterInfo.setUn(object.getString("UserName"));
					masterInfo.setPw(object.getString("PassWord"));
					masterInfo.setTy(1);

					saveMasterInfo();
				} catch (Exception e) {
					// TODO: handle exception
					masterInfo = new Master();
					masterInfo.setMc(SystemInfo.getMasterID(context));
					masterInfo.setMn(MyApplication.getInstance().getString(
							R.string.app_name));
					masterInfo.setWf(SystemInfo.getSSID(context));
					masterInfo.setSt(1);
					masterInfo.setUn("admin");
					masterInfo.setTy(1);
					masterInfo.setPw("");

					saveMasterInfo();
				}
			}
		}
		return masterInfo;

	}

	public static void saveMasterInfo() {
		try {
			if (masterInfo != null) {
				JSONObject bject = new JSONObject();
				bject.put("MasterMac", masterInfo.getMc());
				bject.put("UserName", masterInfo.getUn());
				bject.put("MasterName", masterInfo.getMn());
				bject.put("PassWord", masterInfo.getPw());
				bject.put("MasterType", masterInfo.getTy());
				JSONFileUtils.saveJSONToFile(jsonFile, bject.toString());
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}

	public static List<Master> Masters = new ArrayList<Master>();

	public static void addDevice(Master master) {
		if (Masters.size() > 0) {
			if (isExite(master.getMc())) {
				Masters.add(master);
			}
		} else {
			Masters.add(master);
		}
	}

	public static boolean isExite(String masterMac) {
		Boolean flag = true;
		for (int i = 0; i < Masters.size(); i++) {
			if (Masters.get(i).getMc().equals(masterMac)) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	public static void add(String params) {
		try {
			Masters = (List<Master>) JSONHelper.parseCollection(params,
					List.class, Master.class);
			if (Masters == null) {
				Masters = new ArrayList<Master>();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
