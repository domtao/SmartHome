package com.zunder.smart.webservice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class ProjectServiceUtils {

	static String endPoint = Constants.HTTPS + "Service/ZunProjectService.asmx/";

	public static String getProjects(String PrimaryKey) throws Exception {
		String methodName = "getProjects";
		HashMap data=new HashMap();
		data.put("PrimaryKey", PrimaryKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String getClientProjects(String UserName, String PassWord)
			throws Exception {
		String methodName = "getClientProjects";
		HashMap data=new HashMap();
		data.put("UserName", UserName);
		data.put("PassWord", PassWord);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String deleteProject(String ProjectKey, String PrimaryKey)
			throws Exception {
		String methodName = "deleteProject";
		HashMap data=new HashMap();
		data.put("ProjectKey", ProjectKey);
		data.put("PrimaryKey", PrimaryKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);

	}
	public static String createProject(String ProjectName, String ProjectPwd,
			int ProjectNum, String PrimaryKey) throws Exception {
		String methodName = "createProject";
		HashMap data=new HashMap();
		data.put("ProjectName", ProjectName);
		data.put("ProjectPwd", ProjectPwd);
		data.put("ProjectNum", ProjectNum);
		data.put("PrimaryKey", PrimaryKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String updateProject(String ProjectPwd, int ProjectNum,
			String ProjectKey) throws Exception {
		String methodName = "updateProject";
		HashMap data=new HashMap();
		data.put("ProjectPwd", ProjectPwd);
		data.put("ProjectNum", ProjectNum);
		data.put("ProjectKey", ProjectKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
}
