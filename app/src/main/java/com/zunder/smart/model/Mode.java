package com.zunder.smart.model;

import com.zunder.smart.tools.AppTools;

import android.graphics.Bitmap;
import android.util.Log;

public class Mode {
	private int Id;
	private String ModeName;
	private String ModeImage;
	private String ModeType;
	private int ModeCode;
	private String CreationTime="2018-07-01 12:00:00";
	private int ModeLoop;
	private int Seqencing;
	private String StartTime="00:00";
	private String EndTime="00:00";
	private String ModeNickName="";
	private String GatewayID="";
	private int LanguageId;

	private int CompanyId;
	private int IsShow;
	private String Data1="0";
	private String Data2="0";
	private String Primary_Key="000000";
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getModeName() {
		return ModeName;
	}

	public void setModeName(String modeName) {
		ModeName = modeName;
	}

	public String getModeImage() {
		return ModeImage;
	}

	public void setModeImage(String modeImage) {
		ModeImage = modeImage;
	}

	public String getModeType() {
		return ModeType;
	}

	public void setModeType(String modeType) {
		ModeType = modeType;
	}

	public int getModeCode() {
		return ModeCode;
	}

	public void setModeCode(int modeCode) {
		ModeCode = modeCode;
	}

	public String getCreationTime() {
		return CreationTime;
	}

	public void setCreationTime(String creationTime) {
		CreationTime = creationTime;
	}

	public int getModeLoop() {
		return ModeLoop;
	}

	public void setModeLoop(int modeLoop) {
		ModeLoop = modeLoop;
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

	public String getModeNickName() {

		return ModeNickName;
	}

	public void setModeNickName(String modeNickName) {
		ModeNickName = modeNickName;
	}

	public String getGatewayID() {
		return GatewayID;
	}

	public void setGatewayID(String gatewayID) {
		GatewayID = gatewayID;
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

	public String getPrimary_Key() {
		return Primary_Key;
	}

	public void setPrimary_Key(String primary_Key) {
		Primary_Key = primary_Key;
	}

	public int getIsShow() {
		return IsShow;
	}

	public void setIsShow(int isShow) {
		IsShow = isShow;
	}


	public String convertTostring() {
		String result = getId() + ";" + getModeName() + ";" + getModeType() + ";"
				+ getModeCode() + ";" + getModeLoop() + ";" + getCreationTime() + ";"
				+ getSeqencing() + ";"
				+ getModeNickName()+ ";" + getStartTime() + ";" + getEndTime() ;
		return result;
	}
}
