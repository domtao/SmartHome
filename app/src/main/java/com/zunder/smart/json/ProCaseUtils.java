package com.zunder.smart.json;

import android.text.TextUtils;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.model.Archive;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.ProCase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProCaseUtils {
	private static File jsonFile = new File(MyApplication.getInstance()
			.getRootPath() + File.separator + "procase_json");
	public static List<ProCase> list = new ArrayList<ProCase>();
	private volatile static ProCaseUtils install;
	public static ProCaseUtils getInstance() {
		if (null == install) {
			install = new ProCaseUtils();
		}
		return install;
	}

	public List<ProCase> getAll(){
		if(list.size()==0) {
		String json = JSONFileUtils.getJsonStringFromFile(jsonFile)
				.replace("\r\n", "");
		if (!TextUtils.isEmpty(json)) {
			try {
				JSONArray jsonArray = new JSONArray(json);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					ProCase project = new ProCase();
					project.setProCaseName(object.getString("ProCaseName"));
					project.setProCaseImage(object.getString("ProCaseImage"));
					project.setProCaseAlia(object.getString("ProCaseAlia"));
					project.setProCaseIndex(object.getInt("ProCaseIndex"));
					project.setProCaseKey(object.getString("ProCaseKey"));
					list.add(project);
				}
			} catch (Exception e) {
				// TODO: handle exceptionus
				ProCase project = new ProCase();
				project.setProCaseName("默认家庭");
				project.setProCaseAlia("Root");
				project.setProCaseImage(Constants.PROCASEIMAGEPATH);
				project.setProCaseIndex(1);
				project.setProCaseKey("20180801120000");
				list.add(project);
				save();
			}
		} else {
			ProCase project = new ProCase();
			project.setProCaseName("默认家庭");
			project.setProCaseAlia("Root");
			project.setProCaseImage(Constants.PROCASEIMAGEPATH);
			project.setProCaseIndex(1);
			project.setProCaseKey("20180801120000");
			list.add(project);
			save();
		}
		}
		return list;
	}

	public  void addProCase(ProCase proCase) {
		list.add(proCase);
		save();
	}

	public int addProCase(String proCaseKey) {
		int result = 0;
		for (ProCase _project : list) {
			if (_project.getProCaseKey().equals(proCaseKey)) {
			list.remove(_project);
			save();
			result=1;
			break;
			}
		}
		return result;
	}
	public void delProCase(String proCaseKey) {
		for (ProCase _project : list) {
			if (_project.getProCaseKey().equals(proCaseKey)) {
				list.remove(_project);
				save();
				break;
			}
		}
	}
	public   int judgeName(String proCaseName,String proCaseKey) {

		int result =0;
		for (ProCase proCase : list) {
			if (!proCase.getProCaseKey().equals(proCaseKey)) {
				if(proCase.getProCaseName().equals(proCaseName)){
					result=1;
					break;
				}
			}else{
				result=1;
				break;
			}
		}
		return result;
	}
	public   int updateName(String oldName,String proCaseName,String proCaseKey) {

		int result =0;
		if(oldName.equals(proCaseName)){
			result=1;
			return result;
		}
		for (ProCase proCase : list) {
			if (!proCase.getProCaseKey().equals(proCaseKey)) {
				if(proCase.getProCaseName().equals(proCaseName)){
					result=1;
					break;
				}
			}
		}
		return result;
	}
	public   ProCase judgeName(String proCaseName) {
		ProCase result =null;
		for (ProCase proCase : list) {
			if (proCase.getProCaseName().equals(proCaseName)) {
				result=proCase;
				break;
			}
		}
		return result;
	}


	public  void updateProCase(ProCase proCase) {

		for (ProCase _proCase : list) {
			if (_proCase.getProCaseKey().equals(proCase.getProCaseKey())) {
				_proCase.setProCaseName(proCase.getProCaseName());
				_proCase.setProCaseAlia(proCase.getProCaseAlia());
				_proCase.setProCaseImage(proCase.getProCaseImage());
				save();
				break;
			}
		}
	}

	public  void updateProCaseIndex(int index, String ProCaseKey) {

		for (ProCase _proCase : list) {
			if (_proCase.getProCaseKey().equals(ProCaseKey)) {
				_proCase.setProCaseIndex(1);
			}else{
				_proCase.setProCaseIndex(0);
			}
		}
		save();
	}
	public void save() {
		try {
			JSONArray array = new JSONArray();
			for (ProCase project : list) {
				JSONObject bject = new JSONObject();
				bject.put("ProCaseName", project.getProCaseName());
				bject.put("ProCaseAlia",project.getProCaseAlia());
				bject.put("ProCaseIndex",project.getProCaseIndex());
				bject.put("ProCaseImage", project.getProCaseImage());
				bject.put("ProCaseKey", project.getProCaseKey());
				array.put(bject);
			}
			JSONFileUtils.saveJSONToFile(jsonFile, array.toString());
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}
	public ProCase addManger(){
		ProCase project = new ProCase();
		project.setProCaseName("家庭管理");
		project.setProCaseAlia("家庭管理");
		project.setProCaseImage("ImagesFile/Picture/2018071714145936.png");
		project.setProCaseIndex(0);
		project.setProCaseKey("00000000000000");
		return project;
	}

}
