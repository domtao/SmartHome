package com.zunder.smart.model;

public class InfraCode {
	private int Id;
	private String infraName;
	private String infraKey;
	private String infraCode;
	private int versionID;
	private int controID;
	private int sendID;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		this.Id = id;
	}

	public String getInfraName() {
		return infraName;
	}

	public void setInfraName(String infraName) {
		this.infraName = infraName;
	}

	public String getInfraKey() {
		return infraKey;
	}

	public void setInfraKey(String infraKey) {
		this.infraKey = infraKey;
	}

	public String getInfraCode() {
		return infraCode;
	}

	public void setInfraCode(String infraCode) {
		this.infraCode = infraCode;
	}

	public int getVersionID() {
		return versionID;
	}

	public void setVersionID(int versionID) {
		this.versionID = versionID;
	}

	public int getControID() {
		return controID;
	}

	public void setControID(int controID) {
		this.controID = controID;
	}

	public int getSendID() {
		return sendID;
	}

	public void setSendID(int sendID) {
		this.sendID = sendID;
	}

}
