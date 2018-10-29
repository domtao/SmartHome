package com.zunder.smart.webservice.forward;

import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import android.os.AsyncTask;

import java.util.HashMap;

public class MasterDeviceServiceUtils {
	static String endPoint = Constants.HTTPS + "Service/MasterDeviceService.asmx";
	static String NAMESPACE = "http://tempuri.org/";

	public static String connectMaster(String masterMac, String masterDeviceId,
			String userName, String passWord, int masterType) throws Exception {

		String methodName = "connectMaster";
		HashMap data=new HashMap();
		data.put("masterMac", masterMac);
		data.put("userName", userName);
		data.put("passWord", passWord);
		data.put("masterDeviceId", masterDeviceId);
		data.put("masterType", masterType);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public void postString(String masterMac, String masterDeviceId,
			String userName, String passWord, String masterType) {

		new AsynDataJsonArrayTask().execute(masterMac, masterDeviceId,
				userName, passWord, masterType);

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

				json = connectMaster(params[0], params[1], params[2],
						params[3], Integer.parseInt(params[4]));
			} catch (Exception e) {
				json = "Connect|DAC294BEFFC092C8|74BC7A2E70AFFDE5|9|设备不在线!|DST";
			}
			return json;
		}

		@Override
		protected void onPostExecute(String resString) {
			// TODO Auto-generated method stub
			super.onPostExecute(resString);
			try {
				MainActivity.getInstance().ReceviceBroadcast(resString);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
