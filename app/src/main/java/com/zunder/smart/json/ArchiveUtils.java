package com.zunder.smart.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import com.zunder.smart.MyApplication;
import com.zunder.smart.model.Archive;
import com.zunder.smart.MyApplication;

import android.text.TextUtils;

public class ArchiveUtils {

	public static List<Archive> list1 = new ArrayList<Archive>();

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
						Archive project = new Archive();
						project.setProjectName(object.getString("ProjectName"));
						project.setProjectTime(object.getString("ProjectTime"));
						project.setProjectKey(object.getString("ProjectKey"));
						project.setPrimaryKey(object.getString("PrimaryKey"));
						list1.add(project);
					}
				} catch (Exception e) {
					// TODO: handle exceptionus
				}
			}
		}
	}

	public static int addProject(Archive project) {
		int result = 0;
		for (Archive _project : list1) {
			if (_project.getProjectName().equals(project.getProjectName())) {
				result = 1;
				break;
			}
		}
		if (result == 0) {
			list1.add(project);
			save();
		}
		return result;
	}

	public static void save() {
		try {
			JSONArray array = new JSONArray();
			for (Archive project : list1) {
				JSONObject bject = new JSONObject();
				bject.put("ProjectName", project.getProjectName());
				bject.put("ProjectTime", project.getProjectTime());
				bject.put("ProjectKey", project.getProjectKey());
				bject.put("PrimaryKey", project.getPrimaryKey());
				array.put(bject);
			}
			JSONFileUtils.saveJSONToFile(jsonFile, array.toString());
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}

}
