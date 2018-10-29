package com.zunder.smart.model;

import com.zunder.smart.tools.AppTools;

import android.graphics.Bitmap;
import android.util.Log;

public class ModeList {
	private int Id;
	private String ModeAction="";
	private String ModeFunction="";
	private String ModeTime="0";
	private String ModeDelayed="0";
	private String ModePeriod="0";
	private String CreationTime="2018-07-01 12:00:00";
	private int Seqencing;
	private int DeviceId;
	private int ModeId;
	private String BeginMonth="0";
	private String EndMonth="0";

	private int LanguageId;
	private int CompanyId;
	private String Data1="0";
	private String Data2="0";


	private String DeviceName="0";
	private String DeviceNickName="";
	private int DeviceOnLine;
	private int DeviceTypeKey;
	private String DeviceTypeName="0";
	private String DeviceTimer="0";
	private String DeviceIO="0";
	private String DeviceOrdered="0";

	private String StartTime="00:00";
	private String EndTime="00:00";
	private int ProductsKey;
	private String ProductsName="0";
	private int RoomId;
	private String RoomName="其它";
	private String ProductsCode="0";
	private String ModeEvent="0";
	private String DeviceImage="0";
	private String DeviceID="0";
	private int DeviceTypeClick;
	private  int IsSwitch;
	private int ProductsIO;
	private int ActionShow;
	private int FunctionShow;
	private int ActionPramShow;
	private int FunctionParamShow;
	private String Primary_Key="000000";


	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getCreationTime() {
		return CreationTime;
	}

	public void setCreationTime(String creationTime) {
		CreationTime = creationTime;
	}

	public int getSeqencing() {
		return Seqencing;
	}

	public void setSeqencing(int seqencing) {
		Seqencing = seqencing;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public int getDeviceId() {
		return DeviceId;
	}

	public void setDeviceId(int deviceId) {
		DeviceId = deviceId;
	}

	public int getModeId() {
		return ModeId;
	}

	public void setModeId(int modeId) {
		ModeId = modeId;
	}

	public String getModeDelayed() {
		return ModeDelayed;
	}

	public void setModeDelayed(String modeDelayed) {
		ModeDelayed = modeDelayed;
	}

	public String getModeEvent() {
		return ModeEvent;
	}

	public void setModeEvent(String modeEvent) {
		ModeEvent = modeEvent;
	}

	public String getDeviceName() {
		return DeviceName;
	}

	public void setDeviceName(String deviceName) {
		DeviceName = deviceName;
	}

	public String getDeviceImage() {
		return DeviceImage;
	}

	public void setDeviceImage(String deviceImage) {
		DeviceImage = deviceImage;
	}

	public String getDeviceID() {
		return DeviceID;
	}

	public void setDeviceID(String deviceID) {
		DeviceID = deviceID;
	}

	public String getDeviceNickName() {
		return DeviceNickName;
	}

	public void setDeviceNickName(String deviceNickName) {
		DeviceNickName = deviceNickName;
	}

	public int getDeviceOnLine() {
		return DeviceOnLine;
	}

	public void setDeviceOnLine(int deviceOnLine) {
		DeviceOnLine = deviceOnLine;
	}

	public String getDeviceTimer() {
		return DeviceTimer;
	}

	public void setDeviceTimer(String deviceTimer) {
		DeviceTimer = deviceTimer;
	}

	public String getDeviceIO() {
		return DeviceIO;
	}

	public void setDeviceIO(String deviceIO) {
		DeviceIO = deviceIO;
	}

	public String getProductsCode() {
		return ProductsCode;
	}

	public void setProductsCode(String productsCode) {
		ProductsCode = productsCode;
	}

	public String getDeviceTypeName() {
		return DeviceTypeName;
	}

	public void setDeviceTypeName(String deviceTypeName) {
		DeviceTypeName = deviceTypeName;
	}

	public int getDeviceTypeClick() {
		return DeviceTypeClick;
	}

	public void setDeviceTypeClick(int deviceTypeClick) {
		DeviceTypeClick = deviceTypeClick;
	}

	public int getDeviceTypeKey() {
		return DeviceTypeKey;
	}

	public void setDeviceTypeKey(int deviceTypeKey) {
		DeviceTypeKey = deviceTypeKey;
	}

	public String getDeviceOrdered() {
		return DeviceOrdered;
	}

	public void setDeviceOrdered(String deviceOrdered) {
		DeviceOrdered = deviceOrdered;
	}

	public int getIsSwitch() {
		return IsSwitch;
	}

	public void setIsSwitch(int isSwitch) {
		IsSwitch = isSwitch;
	}

	public int getProductsKey() {
		return ProductsKey;
	}

	public void setProductsKey(int productsKey) {
		ProductsKey = productsKey;
	}

	public int getProductsIO() {
		return ProductsIO;
	}

	public void setProductsIO(int productsIO) {
		ProductsIO = productsIO;
	}

	public String getModePeriod() {
		return ModePeriod;
	}

	public void setModePeriod(String modePeriod) {
		ModePeriod = modePeriod;
	}

	public String getModeAction() {
		return ModeAction;
	}

	public void setModeAction(String modeAction) {
		ModeAction = modeAction;
	}

	public String getModeFunction() {
		if(ModeFunction=="null"||ModeFunction.equals("null")||ModeFunction==null){
			ModeFunction="";
		}
		return ModeFunction;
	}

	public void setModeFunction(String modeFunction) {
		ModeFunction = modeFunction;
	}

	public String getModeTime() {
		return ModeTime;
	}

	public void setModeTime(String modeTime) {
		ModeTime = modeTime;
	}

	public int getActionShow() {
		return ActionShow;
	}

	public void setActionShow(int actionShow) {
		ActionShow = actionShow;
	}

	public int getFunctionShow() {
		return FunctionShow;
	}

	public void setFunctionShow(int functionShow) {
		FunctionShow = functionShow;
	}

	public int getActionPramShow() {
		return ActionPramShow;
	}

	public void setActionPramShow(int actionPramShow) {
		ActionPramShow = actionPramShow;
	}

	public int getFunctionParamShow() {
		return FunctionParamShow;
	}

	public void setFunctionParamShow(int functionParamShow) {
		FunctionParamShow = functionParamShow;
	}

	public String getPrimary_Key() {
		return Primary_Key;
	}

	public void setPrimary_Key(String primary_Key) {
		Primary_Key = primary_Key;
	}

	public String convertTostring() {
		String result = getId() + ";" + getModeAction() + ";"
				+ getModeFunction() + ";" + getModeTime() + ";"
				+ getModeDelayed() + ";" + getModePeriod() + ";"
				+ getDeviceId() + ";" + getModeId();
		Log.e("infoCmd",result);
		return result;
	}

	public String getBeginMonth() {
		return BeginMonth;
	}

	public void setBeginMonth(String beginMonth) {
		BeginMonth = beginMonth;
	}

	public String getEndMonth() {
		return EndMonth;
	}

	public void setEndMonth(String endMonth) {
		EndMonth = endMonth;
	}

	public int getLanguageId() {
		return LanguageId;
	}

	public void setLanguageId(int languageId) {
		LanguageId = languageId;
	}

	public int getCompanyId() {
		return CompanyId;
	}

	public void setCompanyId(int companyId) {
		CompanyId = companyId;
	}

	public String getData1() {
		return Data1;
	}

	public void setData1(String data1) {
		Data1 = data1;
	}

	public String getData2() {
		return Data2;
	}

	public void setData2(String data2) {
		Data2 = data2;
	}

	public String getProductsName() {
		return ProductsName;
	}

	public void setProductsName(String productsName) {
		ProductsName = productsName;
	}

	public int getRoomId() {
		return RoomId;
	}

	public void setRoomId(int roomId) {
		RoomId = roomId;
	}

	public String getRoomName() {
		return RoomName;
	}

	public void setRoomName(String roomName) {
		RoomName = roomName;
	}
}
