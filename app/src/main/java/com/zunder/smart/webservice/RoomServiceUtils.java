package com.zunder.smart.webservice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class RoomServiceUtils {

	static String endPoint = Constants.HTTPS + "Service/ZunRoomService.asmx/";


	public static String getRooms(String ProjectKey) throws Exception {
		String methodName = "getRooms";
		HashMap data=new HashMap();
		data.put("ProjectKey",ProjectKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String insertRoom(int Id, String RoomName, String RoomImage, int IsShow, int Seqencing, String ProjectKey) throws Exception {
		String methodName = "insertRoom";
		String url=endPoint+methodName;
		HashMap data=new HashMap();
		data.put("Id", Id);
		data.put("RoomName", RoomName);
		data.put("RoomImage", RoomImage);
		data.put("IsShow", IsShow);
		data.put("Seqencing", Seqencing);
		data.put("ProjectKey", ProjectKey);
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String deleteRoom(int Id, String ProjectKey) throws Exception {
		String methodName = "deleteRoom";
		String url=endPoint+methodName;
		HashMap data=new HashMap();
		data.put("Id", Id);
		data.put("ProjectKey", ProjectKey);
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}
}
