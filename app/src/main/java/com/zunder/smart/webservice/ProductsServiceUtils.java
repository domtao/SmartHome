package com.zunder.smart.webservice;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;
import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.HttpUrlPostJson;

import java.util.HashMap;

public class ProductsServiceUtils {

	static String endPoint = Constants.HTTPS + "Service/ProductsService.asmx/";

	public static String getProducts() throws Exception {
		String methodName = "getProducts";
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,null);
	}

	public static String insertProducts(int id, String productName,
			String productCode, int productVersion, int productIo,
			int productId, int productKey, int actionShow, int functionShow,
			int actionParamShow, int functionParamShow) throws Exception {
		String methodName = "insertProducts";
		HashMap data=new HashMap();
		data.put("id", id);
		data.put("productName", productName);
		data.put("productCode", productCode);
		data.put("productVersion", productVersion);
		data.put("productIo", productIo);
		data.put("productId", productId);
		data.put("productKey", productKey);
		data.put("actionShow", actionShow);
		data.put("functionShow", functionShow);
		data.put("actionParamShow", actionParamShow);
		data.put("functionParamShow", functionParamShow);
		String url=endPoint+methodName;
		return HttpUrlPostJson.getInstance().getConnectionJson(url,data);
	}

}
