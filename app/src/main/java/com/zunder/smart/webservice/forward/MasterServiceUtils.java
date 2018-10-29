package com.zunder.smart.webservice.forward;

import com.zunder.smart.json.Constants;
import com.zunder.smart.json.MasterUtils;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.json.MasterUtils;
import com.zunder.smart.tools.HttpUrlPostJson;

import android.os.AsyncTask;

import java.util.HashMap;

public class MasterServiceUtils {

	public static MasterServiceUtils instance;
	static {
		if (instance == null) {
			instance = new MasterServiceUtils();
		}
	}
	static String endPoint = Constants.HTTPS + "Service/MasterService.asmx/";
	public String createMaster(String masterName, String masterMac,
			String masterWiFi, int masterType) throws Exception {
		String methodName = "createMaster";
		HashMap data=new HashMap();
		data.put("masterName", masterName);
		data.put("masterMac", masterMac);
		data.put("masterWiFi", masterWiFi);
		data.put("masterType", masterType);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public String getMaster(String masterWiFi, int masterType) throws Exception {
		String methodName = "getMaster";
		HashMap data=new HashMap();
		data.put("masterWiFi", masterWiFi);
		data.put("masterType", masterType);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public String updateMasterUser(String masterMac, String UserName,
			String passWord, String masterName, String masterOperator,
			String operatorPwd) throws Exception {
		String methodName = "updateMasterUser";
		HashMap data=new HashMap();
		data.put("masterMac", masterMac);
		data.put("UserName", UserName);
		data.put("passWord", passWord);
		data.put("masterName", masterName);
		data.put("masterOperator", masterOperator);
		data.put("operatorPwd", operatorPwd);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public String getMasterInfo(String masterMac) throws Exception {
		String methodName = "getMasterInfo";
		HashMap data=new HashMap();
		data.put("masterMac", masterMac);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public void postString(final String masterWiFi) {
		new AsynDataJsonArrayTask().execute(masterWiFi);
	}

	private class AsynDataJsonArrayTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String json = "";
			try {
				json = getMaster(params[0], 0);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(String resString) {
			// TODO Auto-generated method stub
			super.onPostExecute(resString);
			MasterUtils.add(resString);

		}
	}

}
