package com.zunder.smart.webservice.forward;

import android.os.AsyncTask;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class AiuiErrServiceUtils {
	public static AiuiErrServiceUtils instance;
	static {
		if (instance == null) {
			instance = new AiuiErrServiceUtils();
		}
	}
	static String endPoint = Constants.HTTPS + "Service/AiuiErrService.asmx";
	static String NAMESPACE = "http://tempuri.org/";

	public String insertAiuiErr(String ErrID, String ErrName,
			String ErrData, String CreateTime)
			throws Exception {
		String methodName = "insertAiuiErr";
		HashMap data=new HashMap();
		data.put("ErrID", ErrID);
		data.put("ErrName", ErrName);
		data.put("ErrData", ErrData);
		data.put("CreateTime", CreateTime);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);

	}

	public void postString1(String ErrID, String ErrName,
							String ErrData, String CreateTime) {

		new AsynDataJsonArrayTask().execute(ErrID,ErrName,ErrData, CreateTime);
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
				json = insertAiuiErr(params[0], params[1], params[2], params[3]);
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

		}
	}

}
