package com.zunder.smart.model;

import com.zunder.smart.tools.AppTools;

import android.graphics.drawable.Drawable;

public class GateWay {
	private int Id;
	private String GatewayName;
	private String GatewayID;
	private String UserName;
	private String UserPassWord;
	private int TypeId;
	private String State;
	private int IsCurrent;
	private String CreationTime;
	private int Seqencing;
	private String PrimaryKey;
	private Drawable picData;
	private int userid;
	private String GateWayPoint;
	private int handler=0;

	public String getGateWayPoint() {
		if (GateWayPoint == null || GateWayPoint.equals("null")) {
			return "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
		}
		return GateWayPoint;
	}

	public void setGateWayPoint(String gateWayPoint) {
		GateWayPoint = gateWayPoint;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return Id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		Id = id;
	}

	/**
	 * @return the gatewayName
	 */
	public String getGatewayName() {
		if (GatewayName == null || GatewayName.equals("null")) {
			return "";
		}
		return GatewayName;
	}

	/**
	 * @param gatewayName
	 *            the gatewayName to set
	 */
	public void setGatewayName(String gatewayName) {
		GatewayName = gatewayName;
	}

	/**
	 * @return the gatewayID
	 */
	public String getGatewayID() {
		if (GatewayID == null || GatewayID.equals("null")) {
			return "";
		}
		return GatewayID;
	}

	/**
	 * @param gatewayID
	 *            the gatewayID to set
	 */
	public void setGatewayID(String gatewayID) {
		GatewayID = gatewayID;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		if(UserName==null) {
			UserName="admin";
		}
		return UserName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getUserPassWord() {
		if (UserPassWord == null || UserPassWord.equals("null")) {
			return "";
		}
		return UserPassWord;
	}

	public void setUserPassWord(String userPassWord) {
		UserPassWord = userPassWord;
	}

	public int getTypeId() {
		return TypeId;
	}

	public void setTypeId(int typeId) {
		TypeId = typeId;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		if (State == null || State.equals("null")) {
			return "line";
		}
		return State;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		State = state;
	}

	/**
	 * @return the isCurrent
	 */
	public int getIsCurrent() {
		return IsCurrent;
	}

	/**
	 * @param isCurrent
	 *            the isCurrent to set
	 */
	public void setIsCurrent(int isCurrent) {
		IsCurrent = isCurrent;
	}

	/**
	 * @return the creationTime
	 */
	public String getCreationTime() {
		if (CreationTime == null || CreationTime.equals("null")) {
			return AppTools.getCurrentTime();
		}
		return CreationTime;
	}

	/**
	 * @param creationTime
	 *            the creationTime to set
	 */
	public void setCreationTime(String creationTime) {
		CreationTime = creationTime;
	}

	/**
	 * @return the seqencing
	 */
	public int getSeqencing() {
		return Seqencing;
	}

	/**
	 * @param seqencing
	 *            the seqencing to set
	 */
	public void setSeqencing(int seqencing) {
		Seqencing = seqencing;
	}

	/**
	 * @return the primaryKey
	 */
	public String getPrimaryKey() {
		if (PrimaryKey == null || PrimaryKey.equals("null")) {
			return "11111111111";
		}
		return PrimaryKey;
	}

	/**
	 * @param primaryKey
	 *            the primaryKey to set
	 */
	public void setPrimaryKey(String primaryKey) {
		PrimaryKey = primaryKey;
	}

	/**
	 * @return the picData
	 */
	public Drawable getPicData() {
		return picData;
	}

	/**
	 * @param picData
	 *            the picData to set
	 */
	public void setPicData(Drawable picData) {
		this.picData = picData;
	}

	/**
	 * @return the userid
	 */
	public int getUserid() {
		return userid;
	}

	/**
	 * @param userid
	 *            the userid to set
	 */
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String convertTostring() {
		String result = getId() + ";" + getGatewayName() + ";" + getGatewayID()
				+ ";" +getUserName()+";"+getUserPassWord()+";"+ getTypeId() + ";"+getIsCurrent()+";"+getState()+";" + getSeqencing();
		return result;
	}

	public int getHandler() {
		return handler;
	}

	public void setHandler(int handler) {
		this.handler = handler;
	}
}
