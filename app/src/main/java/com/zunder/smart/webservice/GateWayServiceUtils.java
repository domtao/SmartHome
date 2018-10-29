package com.zunder.smart.webservice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class GateWayServiceUtils {
	static String endPoint = Constants.HTTPS + "Service/ZunGatewayService.asmx/";

	public static String getGateWays(String ProjectKey) throws Exception {
		String methodName = "getGateWays";
		HashMap data=new HashMap();
		data.put("ProjectKey", ProjectKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String insertGateway(int Id, String GatewayName, String GatewayID, String UserName, String UserPassWord, int TypeId, String State, int IsCurrent, int Seqencing, String ProjectKey) throws Exception {
		String methodName = "insertGateway";
		HashMap data=new HashMap();
		data.put("Id", Id);
		data.put("GatewayName", GatewayName);
		data.put("GatewayID", GatewayID);
		data.put("UserName", UserName);
		data.put("UserPassWord", UserPassWord);
		data.put("TypeId", TypeId);
		data.put("State", State);
		data.put("IsCurrent", IsCurrent);
		data.put("Seqencing", Seqencing);
		data.put("ProjectKey", ProjectKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

	public static String deleteGateway(int Id, String ProjectKey)
			throws Exception {
		String methodName = "deleteGateway";
		HashMap data=new HashMap();
		data.put("Id", Id);
		data.put("ProjectKey", ProjectKey);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

}
