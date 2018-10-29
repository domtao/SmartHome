package com.zunder.smart.json;

import android.text.TextUtils;

import com.zunder.smart.MyApplication;
import com.zunder.smart.model.Archive;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.tools.JSONHelper;
import com.iflytek.cloud.thirdparty.T;
import com.zunder.smart.MyApplication;
import com.zunder.smart.model.Device;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.zunder.smart.tools.JSONHelper.*;

public class SearchDeviceUtils {

	public static List<Device> list = new ArrayList<Device>();

	private static File jsonFile = new File(MyApplication.getInstance()
			.getRootPath() + File.separator + "search_json");
	static {
		if (list.size() == 0) {
			String json = JSONFileUtils.getJsonStringFromFile(jsonFile)
					.replace("\r\n", "");
			if (!TextUtils.isEmpty(json)) {
				try {
					JSONArray jsonArray = new JSONArray(json);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = jsonArray.getJSONObject(i);
						Device project = new Device();
						project.setPrimary_Key(object.getString("Primary_Key"));
						project.setDeviceTimer(object.getString("DeviceTimer"));
						project.setDeviceID(object.getString("DeviceID"));
						project.setDeviceImage(object.getString("DeviceImage"));
						list.add(project);
					}
				} catch (Exception e) {
					// TODO: handle exceptionus

				}
			}
		}
	}

	public static void add(Device device){
		int result=0;
		if(list.size()>0){
			for (int i=0;i<list.size();i++){
				Device _device=list.get(i);
				if(_device.getPrimary_Key()==device.getPrimary_Key()){
					result=1;
					break;
				}
			}
		}
		if(result==0) {
			list.add(device);
		}
		save();
	}
	public static void removeAll(){
		list.clear();
		save();
	}

	public static void save() {
		try {
			JSONArray array = new JSONArray();
			for (Device project : list) {
				JSONObject bject = new JSONObject();
				bject.put("Primary_Key", project.getPrimary_Key());
				bject.put("DeviceTimer", project.getDeviceTimer());
				bject.put("DeviceID", project.getDeviceID());
				bject.put("DeviceImage", project.getDeviceImage());
				array.put(bject);
			}
			JSONFileUtils.saveJSONToFile(jsonFile, array.toString());
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}
}
