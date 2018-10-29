package com.zunder.smart.model;

import com.zunder.smart.json.Constants;
import com.zunder.smart.tools.AppTools;

import android.graphics.Bitmap;
import android.util.Log;

public class Device {
	private int Id;
	private String DeviceName;
	private String DeviceImage="";
	private String DeviceID;
	private String DeviceNickName="";
	private String CreationTime;
	private int Seqencing;
	private int DeviceOnLine;
	private String DeviceTimer;
	private String DeviceIO;
	private String DeviceOrdered;
	private String StartTime;
	private String EndTime;
	private int SceneId;
	private int DeviceTypeClick;
	private int DeviceTypeKey;
	private int ProductsKey;
	private int GradingId;
	private int RoomId;
	private int LanguageId;
	private int CompanyId;
	private String Data1="0";
	private String Data2="0";
	private String DeviceTypeName="0";
	private String RoomName="0";
	private String ProductsName="0";
	private String ProductsCode="0";
	private String DeviceBackCode="0";
	private String DeviceDigtal="0";
	private int CmdDecodeType;
	private int OpenStart;
	private int DeviceAnalogVar2;
	private int DeviceAnalogVar3;
	private int loadImageIndex;
	private int IsSwitch;
	private int ActionPramShow;
	private int ActionShow;
	private int FunctionShow;
	private int FunctionParamShow;
	private int Device_change;
	private int ModeId;
	private int ProductsIO;
	private int SetTime;
	private int ControlState;
	private String Primary_Key="000000";
	private String ModeDelayed = "0";
	private String ModeEvent="0";
	private String ModePeriod="0";
	private int RouteIndex;
	private int  RelayIndex;
	private int TranceType;
	private  int IsBackCode;
	private String OldName;

	public int getIsShow() {
		return IsShow;
	}

	public void setIsShow(int isShow) {
		IsShow = isShow;
	}

	private int IsShow;

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
		this.Data1 = data1;
	}

	public String getData2() {
		return Data2;
	}

	public void setData2(String data2) {
		this.Data2 = data2;
	}

	public int getOpenStart() {
		return OpenStart;
	}

	public void setOpenStart(int openStart) {
		OpenStart = openStart;
	}

	public String getDeviceDigtal() {
		return DeviceDigtal;
	}

	public void setDeviceDigtal(String deviceDigtal) {
		DeviceDigtal = deviceDigtal;
	}

	public String getDeviceBackCode() {
		return DeviceBackCode;
	}

	public void setDeviceBackCode(String deviceBackCode) {
		DeviceBackCode = deviceBackCode;
	}

	public int getCmdDecodeType() {
		return CmdDecodeType;
	}

	public void setCmdDecodeType(int cmdDecodeType) {
		CmdDecodeType = cmdDecodeType;
	}

	public int getDeviceAnalogVar2() {
		return DeviceAnalogVar2;
	}

	public void setDeviceAnalogVar2(int deviceAnalogVar2) {
		DeviceAnalogVar2 = deviceAnalogVar2;
	}

	public int getDeviceAnalogVar3() {
		return DeviceAnalogVar3;
	}

	public void setDeviceAnalogVar3(int deviceAnalogVar3) {
		DeviceAnalogVar3 = deviceAnalogVar3;
	}

	public int getState() {
		return OpenStart;
	}

	public void setState(int state) {
		OpenStart = state;
	}

	public int getRouteIndex() {
		return RouteIndex;
	}

	public void setRouteIndex(int routeIndex) {
		RouteIndex = routeIndex;
	}

	public int getRelayIndex() {
		return RelayIndex;
	}

	public void setRelayIndex(int relayIndex) {
		RelayIndex = relayIndex;
	}

	public int getTranceType() {
		return TranceType;
	}

	public void setTranceType(int tranceType) {
		TranceType = tranceType;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getDeviceName() {
		return DeviceName;
	}

	public void setDeviceName(String deviceName) {
		DeviceName = deviceName;
	}

	public String getRoomName() {
		if(RoomName==null||RoomName=="null"||RoomName.equals("null")){
			RoomName="";
		}
		return RoomName;
	}

	public void setRoomName(String roomName) {
		RoomName = roomName;
	}

	public String getDeviceImage() {
		if(DeviceImage=="null"||DeviceImage==null||DeviceImage.equals("null")){
			DeviceImage= Constants.MODEIMAGEPATH;
		}
		return DeviceImage;
	}

	public void setDeviceImage(String deviceImage) {
		DeviceImage = deviceImage;
	}

	public String getDeviceID() {
		if(DeviceID=="null"||DeviceID==null||DeviceID.equals("null")){
			DeviceID= "";
		}
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

	public int getDeviceOnLine() {
		return DeviceOnLine;
	}

	public void setDeviceOnLine(int deviceOnLine) {
		DeviceOnLine = deviceOnLine;
	}

	public String getDeviceTimer() {
		if(DeviceTimer=="null"||DeviceTimer==null||DeviceTimer.equals("null")){
			DeviceTimer="";
		}
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

	public String getDeviceOrdered() {
		if(DeviceOrdered=="null"||DeviceOrdered==null||DeviceOrdered.equals("null")){
			DeviceOrdered="";
		}
		return DeviceOrdered;
	}

	public void setDeviceOrdered(String deviceOrdered) {
		DeviceOrdered = deviceOrdered;
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

	public int getDeviceTypeKey() {
		return DeviceTypeKey;
	}

	public void setDeviceTypeKey(int deviceTypeKey) {
		DeviceTypeKey = deviceTypeKey;
	}

	public int getGradingId() {
		return GradingId;
	}

	public void setGradingId(int gradingId) {
		GradingId = gradingId;
	}

	public int getIsSwitch() {
		return IsSwitch;
	}

	public void setIsSwitch(int isSwitch) {
		IsSwitch = isSwitch;
	}

	public int getRoomId() {
		return RoomId;
	}

	public void setRoomId(int roomId) {
		RoomId = roomId;
	}

	public int getProductsIO() {
		return ProductsIO;
	}

	public void setProductsIO(int productsIO) {
		ProductsIO = productsIO;
	}

	public int getDeviceTypeClick() {
		return DeviceTypeClick;
	}

	public void setDeviceTypeClick(int deviceTypeClick) {
		DeviceTypeClick = deviceTypeClick;
	}

	public String getProductsCode() {
		if(ProductsCode=="null"||ProductsCode==null||ProductsCode.equals("null")){
			ProductsCode= "";
		}
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

	public String getProductsName() {
		return ProductsName;
	}

	public void setProductsName(String productsName) {
		ProductsName = productsName;
	}


	public int getLoadImageIndex() {
		return loadImageIndex;
	}

	public void setLoadImageIndex(int loadImageIndex) {
		this.loadImageIndex = loadImageIndex;
	}

	public int getSceneId() {
		return SceneId;
	}

	public void setSceneId(int sceneId) {
		SceneId = sceneId;
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

	public int getProductsKey() {
		return ProductsKey;
	}

	public void setProductsKey(int productsKey) {
		ProductsKey = productsKey;
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

	public String getModePeriod() {
		return ModePeriod;
	}

	public void setModePeriod(String modePeriod) {
		ModePeriod = modePeriod;
	}

	public String convertTostring() {
		String result = getId() + ";" + getDeviceName() + ";" + getDeviceID()
				+ ";" + getDeviceNickName() + ";" + getCreationTime() + ";"
				+ getSeqencing() + ";" + getDeviceOnLine() + ";"
				+ getDeviceTimer() + ";" + getDeviceIO() + ";"
				+ getDeviceOrdered() + ";" + getStartTime() + ";"
				+ getEndTime() + ";" + getSceneId() + ";" + getDeviceTypeKey()
				+ ";" + getProductsKey() + ";" + getGradingId() + ";"
				+ getRoomId();
		Log.e("infoCmd",result);
		return result;
	}

	public int getDevice_change() {
		return Device_change;
	}

	public void
	setDevice_change(int device_change) {
		Device_change = device_change;
	}

	public int getModeId() {
		return ModeId;
	}

	public void setModeId(int modeId) {
		ModeId = modeId;
	}

	public int getSetTime() {
		return SetTime;
	}

	public void setSetTime(int setTime) {
		SetTime = setTime;
	}

	public int getControlState() {
		return ControlState;
	}

	public void setControlState(int controlState) {
		ControlState = controlState;
	}

	public int getIsBackCode() {
		return IsBackCode;
	}

	public void setIsBackCode(int isBackCode) {
		IsBackCode = isBackCode;
	}

	public String getOldName() {
		return OldName;
	}

	public void setOldName(String oldName) {
		OldName = oldName;
	}
}
