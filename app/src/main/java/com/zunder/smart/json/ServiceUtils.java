package com.zunder.smart.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.model.Master;
import com.zunder.smart.model.ServiceInfo;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.tools.SystemInfo;
import com.zunder.smart.MyApplication;

import android.content.Context;
import android.text.TextUtils;

public class ServiceUtils {

	public static ServiceInfo serviceInfo = null;
	private static File jsonFile = new File(MyApplication.getInstance()
			.getRootPath() + File.separator + "service_json");

	public static ServiceInfo getServiceInfo() {
		if (serviceInfo == null) {
			String json = JSONFileUtils.getJsonStringFromFile(jsonFile)
					.replace("\r\n", "");
			if (TextUtils.isEmpty(json)) {
				serviceInfo = new ServiceInfo();
				serviceInfo.setServiceName(MyApplication.getInstance().getString(R.string.servicehly));
				serviceInfo.setIP("112.74.64.82");
				serviceInfo.setPort(2017);
				saveServiceInfo();
			} else {
				try {
					JSONObject object = new JSONObject(json);
					serviceInfo = new ServiceInfo();
					serviceInfo.setServiceName(object.getString("ServiceName"));
					serviceInfo.setIP(object.getString("IP"));
					serviceInfo.setPort(object.getInt("Port"));
				} catch (Exception e) {
					// TODO: handle exception
					serviceInfo = new ServiceInfo();
					serviceInfo.setServiceName(MyApplication.getInstance().getString(R.string.servicehly));
					serviceInfo.setIP("112.74.64.82");
					serviceInfo.setPort(2017);
					saveServiceInfo();
				}
			}
		}
		return serviceInfo;
	}

	public static void saveServiceInfo() {
		try {
			if (serviceInfo != null) {
				JSONObject bject = new JSONObject();
				bject.put("ServiceName", serviceInfo.getServiceName());
				bject.put("IP", serviceInfo.getIP());
				bject.put("Port", serviceInfo.getPort());
				JSONFileUtils.saveJSONToFile(jsonFile, bject.toString());
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		}
	}

	public static List<ServiceInfo> getServiceList() {
		List<ServiceInfo> list = new ArrayList<ServiceInfo>();
		ServiceInfo serviceInfo = new ServiceInfo();

		ServiceInfo serviceInfo1 = new ServiceInfo();
		serviceInfo1.setServiceName(MyApplication.getInstance().getString(R.string.servicehly));
		serviceInfo1.setIP("112.74.64.82");
		serviceInfo1.setPort(2017);
		list.add(serviceInfo1);

		serviceInfo.setServiceName(MyApplication.getInstance().getString(R.string.serviceclt));
		serviceInfo.setIP("60.251.48.20");
		serviceInfo.setPort(2017);
		list.add(serviceInfo);

		ServiceInfo serviceInfo2 = new ServiceInfo();
		serviceInfo2.setServiceName(MyApplication.getInstance().getString(R.string.servicesl));
		serviceInfo2.setIP("47.94.154.118");
		serviceInfo2.setPort(2017);
		list.add(serviceInfo2);

		ServiceInfo serviceInfo3 = new ServiceInfo();
		serviceInfo3.setServiceName(MyApplication.getInstance().getString(R.string.services2));
		serviceInfo3.setIP("112.74.56.95");
		serviceInfo3.setPort(2017);
		list.add(serviceInfo3);

		ServiceInfo serviceInfo4 = new ServiceInfo();
		serviceInfo4.setServiceName("自訂義");
		serviceInfo4.setIP("192.168.2.116");
		serviceInfo4.setPort(2017);
		list.add(serviceInfo4);
		return list;

	}
}
