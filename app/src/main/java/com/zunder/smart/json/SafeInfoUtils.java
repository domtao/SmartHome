package com.zunder.smart.json;

import android.text.TextUtils;

import com.zunder.smart.MyApplication;
import com.zunder.smart.model.Archive;
import com.zunder.smart.model.History;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SafeInfoUtils {

	public static List<History> list1 = new ArrayList<History>();

	private static File jsonFile = new File(MyApplication.getInstance()
			.getRootPath() + File.separator + "project_json");
	static {
		if (list1.size() == 0) {
			String json = JSONFileUtils.getJsonStringFromFile(jsonFile)
					.replace("\r\n", "");
			if (!TextUtils.isEmpty(json)) {
				try {
					JSONArray jsonArray = new JSONArray(json);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = jsonArray.getJSONObject(i);
						History project = new History();
						project.setCreateTime(object.getString("CreateTime"));
						project.setHistoryName(object.getString("HistoryName"));
						list1.add(project);
					}
				} catch (Exception e) {
					// TODO: handle exceptionus
				}
			}
		}
	}

	public static void addProject(History project) {
			list1.add(project);
			save();

	}
	public static void clear() {
		list1.clear();
		save();

	}
	public static void save() {
		try {
			JSONArray array = new JSONArray();
			for (History project : list1) {
				JSONObject bject = new JSONObject();
				bject.put("CreateTime", project.getCreateTime());
				bject.put("HistoryName", project.getHistoryName());
				array.put(bject);
			}
			JSONFileUtils.saveJSONToFile(jsonFile, array.toString());
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}

}
