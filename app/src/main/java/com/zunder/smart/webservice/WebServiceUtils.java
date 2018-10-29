package com.zunder.smart.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Handler;
import android.os.Message;

public class WebServiceUtils {
	public static final String endPoint = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx";
	private static final String NAMESPACE = "http://WebXml.com.cn/";

//获取省份
	public static List<String> getSupportProvince() throws Exception {
		List<String> list = new ArrayList<String>();
//		String methodName = "getSupportProvince";
//		String soapAction = NAMESPACE + methodName;
//		SoapObject rpc = new SoapObject(NAMESPACE, methodName);
//		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//				SoapEnvelope.VER11);
//		envelope.bodyOut = rpc;
//		envelope.dotNet = true;
//		envelope.setOutputSoapObject(rpc);
//		HttpTransportSE transport = new HttpTransportSE(endPoint);
//		transport.call(soapAction, envelope);
//		SoapObject object = (SoapObject) envelope.bodyIn;
//		SoapObject provinceSoapObject = (SoapObject) object.getProperty("getSupportProvinceResult");
//			for(int i=0; i<provinceSoapObject.getPropertyCount(); i++){
//				list.add(provinceSoapObject.getProperty(i).toString());
//			}
		return list;
	}
//获取城市
	public static List<String> getSupportCity(String byProvinceName) throws Exception {
		List<String> list = new ArrayList<String>();
//		String methodName = "getSupportCity";
//		String soapAction = NAMESPACE + methodName;
//		SoapObject rpc = new SoapObject(NAMESPACE, methodName);
//		data.put("byProvinceName", byProvinceName);
//		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//				SoapEnvelope.VER11);
//		envelope.bodyOut = rpc;
//		envelope.dotNet = true;
//		envelope.setOutputSoapObject(rpc);
//		HttpTransportSE transport = new HttpTransportSE(endPoint);
//		transport.call(soapAction, envelope);
//		SoapObject object = (SoapObject) envelope.bodyIn;
//		SoapObject provinceSoapObject = (SoapObject) object.getProperty("getSupportCityResult");
//		for(int i=0; i<provinceSoapObject.getPropertyCount(); i++){
//			list.add(provinceSoapObject.getProperty(i).toString());
//		}
		return list;
	}

	//获取信息
	public static List<String> getWeatherbyCityName(String theCityName) throws Exception {
		List<String> list = new ArrayList<String>();
		String methodName = "getWeatherbyCityName";
//		String soapAction = NAMESPACE + methodName;
//		SoapObject rpc = new SoapObject(NAMESPACE, methodName);
//		data.put("theCityName", theCityName);
//
//		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//				SoapEnvelope.VER11);
//		envelope.bodyOut = rpc;
//		envelope.dotNet = false;
//		envelope.setOutputSoapObject(rpc);
//		HttpTransportSE transport = new HttpTransportSE(endPoint);
//		transport.call(soapAction, envelope);
//		SoapObject object = (SoapObject) envelope.bodyIn;
//		SoapObject provinceSoapObject = (SoapObject) object.getProperty("getWeatherbyCityNameResult");
//		for(int i=0; i<provinceSoapObject.getPropertyCount(); i++){
//			list.add(provinceSoapObject.getProperty(i).toString());
//		}
		return list;
	}
}
