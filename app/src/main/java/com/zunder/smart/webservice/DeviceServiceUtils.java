package com.zunder.smart.webservice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class DeviceServiceUtils {

	static String endPoint = Constants.HTTPS + "Service/ZunDeviceService.asmx/";

	public static String getDevices(String ProjectKey) throws Exception {
		String methodName = "getDevices";
		HashMap data=new HashMap();
		data.put("ProjectKey", ProjectKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String insertDevice(int Id, String DeviceName, String DeviceImage, String DeviceID, String DeviceNickName, int Seqencing, int DeviceOnLine, String DeviceTimer,
							   String DeviceIO, String DeviceOrdered, String StartTime, String EndTime, int DeviceTypeKey, int ProductsKey, int RoomId,String Data1, String ProjectKey)
			throws Exception {
		String methodName = "insertDevice";
		HashMap data=new HashMap();
		data.put("Id", Id);
		data.put("DeviceName", DeviceName);
		data.put("DeviceImage", DeviceImage);
		data.put("DeviceID", DeviceID);
		data.put("DeviceNickName", DeviceNickName);
		data.put("Seqencing", Seqencing);
		data.put("DeviceOnLine", DeviceOnLine);
		data.put("DeviceTimer", DeviceTimer);
		data.put("DeviceIO", DeviceIO);
		data.put("DeviceOrdered", DeviceOrdered);
		data.put("StartTime", StartTime);
		data.put("EndTime", EndTime);
		data.put("DeviceTypeKey", DeviceTypeKey);
		data.put("ProductsKey", ProductsKey);
		data.put("RoomId", RoomId);
		data.put("Data1", Data1);
		data.put("ProjectKey", ProjectKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String deleteDevice(int Id, String ProjectKey)
			throws Exception {
		String methodName = "deleteDevice";
		HashMap data=new HashMap();
		data.put("Id", Id);
		data.put("ProjectKey", ProjectKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

}
